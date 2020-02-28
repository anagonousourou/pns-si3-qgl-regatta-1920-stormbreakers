package fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CheckpointsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.OarsConfig;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;

public class Captain {

    private Boat boat;

    private CheckpointsManager checkpointsManager;
    private Coordinator coordinator;
    private Navigator navigator;
    private static final double EPS = 0.001;
    private static final double SPEED = 165;

    private WeatherAnalyst weatherAnalyst;

    public Captain(Boat boat, CheckpointsManager checkpointsManager, Navigator navigator, WeatherAnalyst weatherAnalyst,
            Coordinator coordinator) {
        this.boat = boat;
        this.checkpointsManager = checkpointsManager;
        this.navigator = navigator;
        this.coordinator = coordinator;
        this.weatherAnalyst = weatherAnalyst;

    }

    /**
     * Principal point d'entrée de la classe
     */
    public List<SailorAction> nextRoundActions() {
        // On remet le statut doneTurn de tous les marins à false
        this.coordinator.resetAvailability();
        this.checkpointsManager.updateCheckpoint(boat.getPosition());

        Checkpoint chpoint = this.checkpointsManager.nextCheckpoint();
        if (chpoint == null) {
            return List.of();
        }

        double orientation = this.navigator.additionalOrientationNeeded(boat.getPosition(),
                chpoint.getPosition().getPoint2D());
        double distance = boat.getPosition().distanceTo(chpoint.getPosition());

        List<SailorAction> actionsOrientation = this.actionsToOrientate(orientation);

        double currentSpeed = this.calculateSpeedFromOarsAction(actionsOrientation);
        List<SailorAction> actionsToAdjustSpeed = this.adjustSpeed(distance, currentSpeed);

        return Captain.<SailorAction>concatenate(actionsOrientation, actionsToAdjustSpeed);

    }

    public double calculateSpeedFromOarsAction(List<SailorAction> actions) {
        int nbActivatedOars = (int) actions.stream()
                .filter(action -> action.getType().equals(ActionType.OAR.actionCode)).count();

        return SPEED * ((double) nbActivatedOars / this.coordinator.nbOars());
    }

    /**
     * Renvoie les actions à effectuer pour que le bateau prenne l'orientation
     * passée en parametre
     * 
     * @param orientation
     * @return
     */
    List<SailorAction> actionsToOrientate(double orientation) {
    	// NO ORIENTATION NEEDED
        if (Math.abs(orientation) < Captain.EPS) {
            return List.of();
        }
        
        // Find an "exact" match of the orientation within the angles possible with oars only
        Optional<OarsConfig> possibleOarsConfig = navigator.possibleOarConfigs(coordinator.nbLeftOars(), coordinator.nbRightOars())
        		.stream()
        		.filter(oc -> oc.getAngle() == orientation)
        		.findFirst();
        if(possibleOarsConfig.isPresent()) {
        	int diff = this.navigator.fromAngleToDiff(orientation, this.coordinator.nbLeftOars(),
                    this.coordinator.nbRightOars());
            return this.validateActions(this.coordinator.activateOarsNotStrict(diff));
        }
        
        List<SailorAction> actions = List.of(); 
        
        // No match found, so we take the greatest angle possible (with oars only) as a reference for later
    	Optional<OarsConfig> maxOarsConfig = navigator.possibleOarConfigs(coordinator.nbLeftOars(),
                coordinator.nbRightOars())
    			.stream()
        		.filter(oc -> oc.getAngle() > 0 && orientation > 0 || oc.getAngle() < 0 && orientation < 0)
        		.max((oc1, oc2) -> (int)(Math.abs(oc1.getAngle()) - Math.abs(oc2.getAngle())));   
    	// If the rudder is present, accessible, and there is at least 1 OarsConfig possible
		if (maxOarsConfig.isPresent()) {
			double chosenAngleOarsConfig = 0;
			int diff;
			
			// If "orientation" is smaller than the maxOarsConfig angle,
			// then we choose the closest oarsConfig angle
			if(Math.abs(orientation) < Math.abs(maxOarsConfig.get().getAngle())){
				Optional<OarsConfig> closestOarsConfig = navigator.possibleOarConfigs(coordinator.nbLeftOars(),
		                coordinator.nbRightOars())
		    			.stream()
		    			.filter(oc -> oc.getAngle() > 0 && orientation > 0 || oc.getAngle() < 0 && orientation < 0)
		        		.min((oc1, oc2) -> (int)(Math.abs(oc1.getAngle()) - Math.abs(oc2.getAngle() - (Math.abs(orientation)))));
				diff = (closestOarsConfig.isPresent() ? closestOarsConfig.get().getOarSidesDifference(): 0);
				chosenAngleOarsConfig = (closestOarsConfig.isPresent() ? closestOarsConfig.get().getAngle(): 0);
				actions = new ArrayList<>(validateActions(coordinator.activateOarsNotStrict(diff)));
				
			// If "orientation" is bigger than the maxOarsConfig angle,
			// then we use maxOarsConfig alongside the rudder for a bigger orientation.
			} else {
				diff = maxOarsConfig.get().getOarSidesDifference();
				chosenAngleOarsConfig = maxOarsConfig.get().getAngle();
				actions = new ArrayList<>(validateActions(coordinator.activateOarsNotStrict(diff)));	
			}
			double restOrientation = orientation - chosenAngleOarsConfig;

			// If the rudder is necessary, then we use it alongside the chosen OarsConfig
			if(coordinator.rudderIsPresent() && coordinator.rudderIsAccesible() && restOrientation != 0 && !actions.isEmpty()) {
				actions.addAll(validateActions(this.coordinator.activateRudder(restOrientation)));
			}
		}
		return actions;
	}

    /**
     * Fonction qui envoie des actions pour ajouter des marins pour faire avancer le
     * bateau sans changer l'orientation on doit donc absolument veiller à garder
     * l'équilibre et aussi à ne pas utiliser les marins déja dispatchés par la
     * fonction actionsToOrientate pour régler l'angle
     * 
     * @param distance
     * @param busy
     * @return
     */
    List<SailorAction> adjustSpeed(double distance, double currentSpeed) {

        return this.adjustSpeedTakingIntoAccountWind(distance, currentSpeed);

    }

    List<SailorAction> adjustSpeedTakingIntoAccountWind(double distance, double currentSpeed) {
        double currentExternalSpeed = this.weatherAnalyst.currentExternalSpeed();

        if (this.weatherAnalyst.additionalSpeedExists()) {
            double potentialSpeedAcquirable = this.weatherAnalyst.potentialSpeedAcquirable();
            if (potentialSpeedAcquirable > 0.0 && (currentSpeed + potentialSpeedAcquirable) <= distance
                    && potentialSpeedAcquirable != currentExternalSpeed) {

                List<SailorAction> actionsToUseWeather = new ArrayList<>();
                int nbOpenned = this.coordinator.liftSailsPartially(actionsToUseWeather);
                this.validateActions(actionsToUseWeather);
                double expectedSpeedFromWind = this.weatherAnalyst.externalSpeedGivenNbOpennedSails(nbOpenned);
                return Captain.<SailorAction>concatenate(

                        actionsToUseWeather,
                        this.accelerate(distance, currentSpeed + expectedSpeedFromWind));

            }

            if (currentExternalSpeed < 0.0 || currentSpeed + currentExternalSpeed > distance) {

                List<SailorAction> actionsToCancelWeather = new ArrayList<>();
                int nbOpenned = this.coordinator.lowerSailsPartially(actionsToCancelWeather);
                this.validateActions(actionsToCancelWeather);

                double expectedSpeedFromWind = this.weatherAnalyst.externalSpeedGivenNbOpennedSails(nbOpenned);

                return Captain.<SailorAction>concatenate(

                        actionsToCancelWeather,
                        this.accelerate(distance, currentSpeed + expectedSpeedFromWind));

            }

            // voir si on peut pas ouvrir des voiles sups, sans dépasser bien sur
            if (currentSpeed + currentExternalSpeed <= distance && currentExternalSpeed > 0) {

                return this.accelerate(distance, currentSpeed + currentExternalSpeed);
            }
            // voiles fermées et les ouvrir vous ralentissent
            if (currentExternalSpeed == 0.0 && this.weatherAnalyst.potentialSpeedAcquirable() <= 0.0) {
                return this.accelerate(distance, currentSpeed);
            }

        }
        return this.accelerate(distance, currentSpeed);

    }

    /**
     * Cette fonction "valide" les actions en marquant les sailors concernes comme
     * occupée
     * 
     * @param actions
     * @return le parametre inchangé pour permettre d'enchainer les méthodes
     */
    List<SailorAction> validateActions(List<SailorAction> actions) {
        this.coordinator.validateActions(actions);
        this.coordinator.executeSailorsActions(actions);
        return actions;

    }

    /**
     * Cette méthode permet d'augmenter la vitesse du bateau tout en
     * respectant la distance
     * 
     * @param distance
     * @param currentSpeed
     * @return
     */
    List<SailorAction> accelerate(double distance, double currentSpeed) {

        if (distance <= currentSpeed && currentSpeed > 0) {
            return List.of();// RIEN A FAIRE
        }
        double minAdditionalSpeed = SPEED * (2.0 / this.coordinator.nbOars());
        if (currentSpeed + minAdditionalSpeed <= distance) {
            List<SailorAction> actions = this.validateActions(this.coordinator.addOaringSailorsOnEachSide());
            return Captain.<SailorAction>concatenate(actions,
                    this.accelerate(distance, currentSpeed + minAdditionalSpeed));

        }
        if(currentSpeed==0){
            return this.validateActions(this.coordinator.addOaringSailorsOnEachSide());
        }
        return List.of();
        
    }

    /** Generic function to concatenate 2 lists in Java */
    private static <T> List<T> concatenate(List<T> list1, List<T> list2) {
        return Stream.of(list1, list2).flatMap(List::stream).collect(Collectors.toList());
    }
}