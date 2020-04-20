package fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CheckpointsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.OarsConfig;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.TargetDefiner;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.TupleDistanceOrientation;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;

public class Captain {

    private Boat boat;

    private CheckpointsManager checkpointsManager;
    private Coordinator coordinator;
    private Navigator navigator;

    private static final double SPEED = 165;

    private WeatherAnalyst weatherAnalyst;

    private TargetDefiner targetDefiner;
    private TupleDistanceOrientation objectif;

    public Captain(Boat boat, CheckpointsManager checkpointsManager, Navigator navigator, WeatherAnalyst weatherAnalyst,
            Coordinator coordinator, TargetDefiner targetDefiner) {
        this.boat = boat;
        this.checkpointsManager = checkpointsManager;
        this.navigator = navigator;
        this.coordinator = coordinator;
        this.weatherAnalyst = weatherAnalyst;
        this.targetDefiner = targetDefiner;

    }

    /**
     * Principal point d'entrée de la classe
     */
    public List<SailorAction> nextRoundActions() {
        // On remet le statut doneTurn de tous les marins à false
        this.coordinator.resetAvailability();
        this.checkpointsManager.updateCheckpoint(boat);

        var destination = this.targetDefiner.defineNextTarget();

        if (destination == null) {
            return List.of();
        }
        this.objectif=destination;
        double orientation = destination.getOrientation();
        double distance = destination.getDistance();

        
        if (Math.abs(orientation) >= 0.05 && this.coordinator.rudderIsPresent() && this.coordinator.rudderIsAccesible() && !targetDefiner.curveTrajectoryIsSafe(objectif)) {
            
            distance = 0.0;
        }

        List<SailorAction> actionsOrientation = this.actionsToOrientate(orientation, distance);

        double currentSpeed = this.calculateSpeedFromOarsAction(actionsOrientation);
        List<SailorAction> actionsToAdjustSpeed = this.adjustSpeed(distance, currentSpeed);
        
        List<SailorAction> actionsToUseWatch = this.validateActions(this.coordinator.setSailorToWatch());
        
        List<SailorAction> actionsForUnusedSailors = this.validateActions(this.coordinator.manageUnusedSailors());

        return Utils.<SailorAction>concatenate(actionsOrientation, actionsToAdjustSpeed, actionsToUseWatch, actionsForUnusedSailors);

    }

    public double calculateSpeedFromOarsAction(List<SailorAction> actions) {
        int nbActivatedOars = (int) actions.stream()
                .filter(action -> action.getType().equals(ActionType.OAR.actionCode)).count();

        return SPEED * ((double) nbActivatedOars / this.coordinator.nbOars());
    }

    List<SailorAction> orientateWithRudder(double orientation, double vitesse) {
        if (Utils.within(orientation, Utils.MAX_RUDDER_ROTATION)) {
            // rudder suffisant
            return this.validateActions(this.coordinator.activateRudder(orientation));
        } else {// rudder insuffisant ou la vitesse voulue est nulle
            if (this.coordinator.nbOars() == 0 || vitesse <= 1) {
                // pas de rames
                return this.validateActions(
                        this.coordinator.activateRudder(Utils.MAX_RUDDER_ROTATION * Math.signum(orientation)));
            } else {
                // on a des rames et le rudder
                Optional<OarsConfig> possibleOarsConfig = this.navigator
                        .possibleOarConfigs(coordinator.nbLeftOars(), coordinator.nbRightOars()).stream()
                        .filter(oc -> oc.getAngle() * orientation > 0)
                        .max((oc1, oc2) -> Double.compare(oc1.getAngle(), oc2.getAngle()));

                if (possibleOarsConfig.isPresent()) {
                    int diff = this.navigator.fromAngleToDiff(possibleOarsConfig.get().getAngle(),
                            this.coordinator.nbLeftOars(), this.coordinator.nbRightOars());

                    var rudderActions = this.validateActions(this.coordinator
                            .activateRudder(Utils.clamp(orientation - possibleOarsConfig.get().getAngle(),
                                    -Utils.MAX_RUDDER_ROTATION, Utils.MAX_RUDDER_ROTATION)));

                    var oarsActions = this.validateActions(this.coordinator.activateOarsNotStrict(diff));
                    return Utils.<SailorAction>concatenate(rudderActions, oarsActions);
                } else {

                    int diff = this.navigator.fromAngleToDiff(orientation, this.coordinator.nbLeftOars(),
                            this.coordinator.nbRightOars());
                    List<SailorAction> actions = this.coordinator.activateOarsNotStrict(diff);
                    return this.validateActions(actions);
                }
            }
        }
    }

    /**
     * Renvoie les actions à effectuer pour que le bateau prenne l'orientation
     * passée en parametre
     * 
     * @param orientation
     * @return
     */
    List<SailorAction> actionsToOrientate(double orientation, double vitesse) {
        

        // NO ORIENTATION NEEDED
        if (Utils.within(orientation, Utils.MIN_ROTATION)) {
            return List.of();
        }
        
        // orientation needed is low but the rudder can be used for better adjustment so we use it:) 
        if (Utils.within(orientation, Utils.EPS) && this.coordinator.rudderIsPresent()
                && this.coordinator.rudderIsAccesible()) {
            return this.orientateWithRudder(orientation, vitesse);
        }

        
        //on a le rudder et il peut etre utilisé
        else if (this.coordinator.rudderIsPresent() && this.coordinator.rudderIsAccesible()) {
            return this.orientateWithRudder(orientation, vitesse);
        } else {
            int diff = this.navigator.fromAngleToDiff(orientation, this.coordinator.nbLeftOars(),
                    this.coordinator.nbRightOars());
            List<SailorAction> actions = this.coordinator.activateOarsNotStrict(diff);
            return this.validateActions(actions);
        }
    }

    /**
     * Fonction qui envoie des actions pour ajouter des marins pour faire avancer le
     * bateau sans changer l'orientation on doit donc absolument veiller à garder
     * l'équilibre et aussi à ne pas utiliser les marins déja dispatchés par la
     * fonction actionsToOrientate pour régler l'angle
     * 
     * @param distance
     * @param currentSpeed
     * @return
     */
    List<SailorAction> adjustSpeed(double distance, double currentSpeed) {

        return this.adjustSpeedTakingIntoAccountWind(distance, currentSpeed);

    }

    List<SailorAction> adjustSpeedTakingIntoAccountWind(double distance, double currentSpeed) {
        double currentExternalSpeed = this.weatherAnalyst.currentExternalSpeed();

        if (this.weatherAnalyst.speedFromWindExists()) {
            // si on a du vent
            double potentialSpeedAcquirable = this.weatherAnalyst.potentialSpeedAcquirable();
            // le vent va dans le bon sens et ne nous fait pas dépasser
            if (potentialSpeedAcquirable > 0.0 && (currentSpeed + potentialSpeedAcquirable) <= distance) {

                List<SailorAction> actionsToUseWeather = new ArrayList<>();
                int nbOpenned = this.coordinator.liftSailsPartially(actionsToUseWeather);
                this.validateActions(actionsToUseWeather);
                double expectedSpeedFromWind = this.weatherAnalyst.externalSpeedGivenNbOpennedSails(nbOpenned);
                return Utils.<SailorAction>concatenate(

                        actionsToUseWeather, this.accelerate(distance, currentSpeed + expectedSpeedFromWind));

            }
            // vent dans le mauvais sens || vent nous fait dépasser
            if (currentExternalSpeed < 0.0 || currentSpeed + currentExternalSpeed > distance) {

                List<SailorAction> actionsToCancelWeather = new ArrayList<>();
                int nbOpenned = this.coordinator.lowerSailsPartially(actionsToCancelWeather);
                this.validateActions(actionsToCancelWeather);

                double expectedSpeedFromWind = this.weatherAnalyst.externalSpeedGivenNbOpennedSails(nbOpenned);

                return Utils.<SailorAction>concatenate(

                        actionsToCancelWeather, this.accelerate(distance, currentSpeed + expectedSpeedFromWind));

            }

            // voir si on peut pas ouvrir des voiles sups, sans dépasser bien sur
            if (currentSpeed + currentExternalSpeed <= distance && currentExternalSpeed > 0) {

                return this.accelerate(distance, currentSpeed + currentExternalSpeed);
            }
            // voiles fermées et les ouvrir vous ralentissent
            if (Utils.almostEquals(currentExternalSpeed, 0.0)
                    && this.weatherAnalyst.potentialSpeedAcquirable() <= 0.0) {
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
     * Cette méthode permet d'augmenter la vitesse du bateau tout en respectant la
     * distance
     * 
     * @param distance
     * @param currentSpeed
     * @return
     */
    List<SailorAction> accelerate(double distance, double currentSpeed) {
        // distance nulle ou inférieure à la vitesse acquise
        if (distance <= Utils.EPS || distance <= currentSpeed) {
            return List.of();
        }

        double minAdditionalSpeed = SPEED * (2.0 / this.coordinator.nbOars());
        if (currentSpeed + minAdditionalSpeed <= distance) {
            List<SailorAction> actions = this.validateActions(this.coordinator.addOaringSailorsOnEachSide());
            return Utils.<SailorAction>concatenate(actions,
                    this.accelerate(distance, currentSpeed + minAdditionalSpeed));

        }

        if(Utils.almostEquals(currentSpeed, 0.0) && objectif!=null && !objectif.isStrict()){
            return this.validateActions(this.coordinator.addOaringSailorsOnEachSide());
        }
        
        return List.of();

    }


    
}