package fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CheckpointsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.OarsConfig;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.TargetDefiner;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;

public class Captain {

    private Boat boat;

    private CheckpointsManager checkpointsManager;
    private Coordinator coordinator;
    private Navigator navigator;
    private static final double EPS = 0.001;
    private static final double SPEED = 165;

    private WeatherAnalyst weatherAnalyst;

    private TargetDefiner targetDefiner;

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

        double orientation = destination.getOrientation();
        double distance = destination.getDistance();

        List<SailorAction> actionsOrientation = this.actionsToOrientate(orientation);

        double currentSpeed = this.calculateSpeedFromOarsAction(actionsOrientation);
        List<SailorAction> actionsToAdjustSpeed = this.adjustSpeed(distance, currentSpeed);

        List<SailorAction> actionsForUnusedSailors = this.validateActions(this.coordinator.manageUnusedSailors());

        return Utils.<SailorAction>concatenate(actionsOrientation, actionsToAdjustSpeed, actionsForUnusedSailors);

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

        else if (this.coordinator.rudderIsPresent() && this.coordinator.rudderIsAccesible()) {
            if (Math.abs(orientation) <= Math.PI / 4) {
                // rudder suffisant
                return this.validateActions(this.coordinator.activateRudder(orientation));
            } else {// rudder insuffisant
                if (this.coordinator.nbOars() == 0) {
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
                        Logger.getInstance().log(possibleOarsConfig.toString());
                        int diff = this.navigator.fromAngleToDiff(possibleOarsConfig.get().getAngle(),
                                this.coordinator.nbLeftOars(), this.coordinator.nbRightOars());

                        var rudderActions = this.validateActions(this.coordinator.activateRudder(Utils.clamp
                                (orientation - possibleOarsConfig.get().getAngle(),-Utils.MAX_RUDDER_ROTATION,Utils.MAX_RUDDER_ROTATION )));
                                
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
                return Utils.<SailorAction>concatenate(

                        actionsToUseWeather, this.accelerate(distance, currentSpeed + expectedSpeedFromWind));

            }

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
            if (Utils.almostEquals(currentExternalSpeed, 0.0) && this.weatherAnalyst.potentialSpeedAcquirable() <= 0.0) {
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

        if (distance <= currentSpeed && currentSpeed > 0) {
            return List.of();// RIEN A FAIRE
        }
        double minAdditionalSpeed = SPEED * (2.0 / this.coordinator.nbOars());
        if (currentSpeed + minAdditionalSpeed <= distance) {
            List<SailorAction> actions = this.validateActions(this.coordinator.addOaringSailorsOnEachSide());
            return Utils.<SailorAction>concatenate(actions,
                    this.accelerate(distance, currentSpeed + minAdditionalSpeed));

        }
        if (Utils.almostEquals(currentSpeed, 0)) {
            return this.validateActions(this.coordinator.addOaringSailorsOnEachSide());
        }
        return List.of();

    }

}