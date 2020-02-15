package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;

public class Captain {

    private Boat boat;

    private CheckpointManager checkpointManager;
    private MediatorCrewEquipment mediatorCrewEquipment;
    private Navigator navigator;
    private static final double EPS = 0.001;
    private static final double SPEED = 165;

    private WeatherAnalyst weatherAnalyst;

    public Captain(Boat boat, CheckpointManager checkpointManager, Crew crew, Navigator navigator,
            WeatherAnalyst weatherAnalyst, MediatorCrewEquipment mediatorCrewEquipment) {
        this.boat = boat;
        this.checkpointManager = checkpointManager;
        this.navigator = navigator;
        this.mediatorCrewEquipment = mediatorCrewEquipment;
        this.weatherAnalyst = weatherAnalyst;

    }

    /**
     * Principal point d'entrée de la classe
     */
    public List<SailorAction> nextRoundActions() {
        // On remet le statut doneTurn de tout les marins à false
        this.mediatorCrewEquipment.resetAvailability();
        this.checkpointManager.updateCheckpoint(boat.getPosition());

        Checkpoint chpoint = this.checkpointManager.nextCheckpoint();
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

        return SPEED * ((double) nbActivatedOars / this.mediatorCrewEquipment.nbOars());
    }

    /**
     * Renvoie les actions à effectuer pour que le bateau prenne l'orientation
     * passée en parametre
     * 
     * @param orientation
     * @return
     */
    List<SailorAction> actionsToOrientate(double orientation) {
        if (Math.abs(orientation) < Captain.EPS) {// pas besoin de pivoter
            return List.of();
        }
        // le gouvernail existe, est accessible et suffit
        else if (mediatorCrewEquipment.rudderIsPresent() && mediatorCrewEquipment.rudderIsAccesible()
                && (Math.abs(orientation) <= Math.PI / 4)) {

            return this.validateActions(this.mediatorCrewEquipment.activateRudder(orientation));
            /**
             * TODO Plus tard, rajouter le cas où le gouvernail existe, est accessible mais
             * ne suffit pas
             */

        }
        // le gouvernail n'existe pas
        else {

            int diff = this.fromAngleToDiff(orientation);
            List<SailorAction> actions = this.mediatorCrewEquipment.activateOarsNotStrict(diff);
            return this.validateActions(actions);
        }
    }

    int fromAngleToDiff(double orientation) {
        Map<Fraction, Integer> correspondances = this.navigator.pairAngleDifference(this.mediatorCrewEquipment.nbLeftOars(),
                this.mediatorCrewEquipment.nbRightOars());
        Set<Fraction> eventailAngles = correspondances.keySet();
        Optional<Fraction> optAngle = eventailAngles.stream().filter(a -> a.eval() * orientation > 0.0)

                .min((a, b) -> Double.compare(Math.abs(a.eval() * Math.PI - orientation),
                        Math.abs(b.eval() * Math.PI - orientation)));
        if (optAngle.isPresent()) {
            Fraction approachingAngle = optAngle.get();
            return correspondances.get(approachingAngle);
        }
        return 0;
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

            if (this.weatherAnalyst.potentialSpeedAcquirable() > 0.0
                    && (currentSpeed + this.weatherAnalyst.potentialSpeedAcquirable()) <= distance) {


                
                    List<SailorAction> actionsToUseWeather = new ArrayList<>();
                    int nbOpenned= this.mediatorCrewEquipment.liftSailsPartially(actionsToUseWeather);
                    this.validateActions(actionsToUseWeather);
                    double expectedSpeedFromWind = this.weatherAnalyst.externalSpeedGivenNbOpennedSails(nbOpenned);
                    return Captain.<SailorAction>concatenate(

                            actionsToUseWeather, this.validateActions(this.accelerate(distance,
                                    currentSpeed + expectedSpeedFromWind)));
                

            }

            if (currentExternalSpeed < 0.0 || currentSpeed + currentExternalSpeed > distance) {

                List<SailorAction> actionsToCancelWeather = new ArrayList<>();
                int nbOpenned = this.mediatorCrewEquipment.lowerSailsPartially(actionsToCancelWeather);
                this.validateActions(actionsToCancelWeather);

                double expectedSpeedFromWind = this.weatherAnalyst.externalSpeedGivenNbOpennedSails(nbOpenned);

                return Captain.<SailorAction>concatenate(

                        actionsToCancelWeather,
                        this.validateActions(this.accelerate(distance, currentSpeed + expectedSpeedFromWind)));

            }

            // TODO voir si on peut pas ouvrir des voiles sups, sans dépasser bien sur
            if (currentSpeed + currentExternalSpeed <= distance) {

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
        this.mediatorCrewEquipment.validateActions(actions);
        return actions;

    }

    /**
     * Cette méthode permet essaie d'augmenter la vitesse du bateau tout en
     * respectant la distance
     * 
     * @param distance
     * @param currentSpeed
     * @return
     */
    List<SailorAction> accelerate(double distance, double currentSpeed) {

        if (distance <= currentSpeed) {
            return List.of();// RIEN A FAIRE
        }
        double minAdditionalSpeed = SPEED * (2.0 / this.mediatorCrewEquipment.nbOars());
        if (currentSpeed + minAdditionalSpeed <= distance && this.mediatorCrewEquipment.canAccelerate()) {
            List<SailorAction> actions = this.validateActions(this.mediatorCrewEquipment.activateOarsEachSide());
            return Captain.<SailorAction>concatenate(actions,
                    this.accelerate(distance, currentSpeed + minAdditionalSpeed));

        }
        return List.of();
    }

    /** Generic function to concatenate 2 lists in Java */
    private static <T> List<T> concatenate(List<T> list1, List<T> list2) {
        return Stream.of(list1, list2).flatMap(List::stream).collect(Collectors.toList());
    }
}