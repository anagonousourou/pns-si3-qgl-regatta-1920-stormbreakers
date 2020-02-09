package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

public class Captain {

    private Boat boat;
    
    private CheckpointManager checkpointManager;
    private MediatorCrewEquipment mediatorCrewEquipment;
    private Navigator navigator;
    final private double EPS = 0.001;
    final private double SPEED = 165;

    public Captain(Boat boat, CheckpointManager checkpointManager, EquipmentManager equipmentManager, Crew crew,
            Navigator navigator) {
        this.boat = boat;
        this.checkpointManager = checkpointManager;
        this.navigator = navigator;
        this.mediatorCrewEquipment=new MediatorCrewEquipment(crew, equipmentManager);

    }

    /**
     * Principal point d'entrée de la fonction
     */
    public List<SailorAction> nextRoundActions() {
        this.checkpointManager.updateCheckpoint(boat.getPosition());
        // TODO condition pour le cas où on a fini ie plus de nextCheckpoint
        Checkpoint chpoint = this.checkpointManager.nextCheckpoint();

        double orientation = this.navigator.additionalOrientationNeeded(boat.getPosition(), chpoint.getPosition());
        double distance = boat.getPosition().distanceTo(chpoint.getPosition());

        List<SailorAction> actionsOrientation = this.actionsToOrientate(orientation);
        List<SailorAction> actionsToAdjustSpeed = this.actionsToOrientate(distance);

        List<SailorAction> actions = new ArrayList<>();
        actions.addAll(actionsOrientation);
        actions.addAll(actionsToAdjustSpeed);

        return actions;

    }

    /**
     * Renvoie les actions à effectuer pour que le bateau prenne l'orientation
     * passée en parametre
     * 
     * @param orientation
     * @return
     */
    List<SailorAction> actionsToOrientate(double orientation) {
        if (Math.abs(orientation) < this.EPS) {
            return List.of();
        } 
        //le gouvernail existe, est accessible et suffit
        else if (mediatorCrewEquipment.rudderIsPresent() && mediatorCrewEquipment.rudderIsAccesible() && (Math.abs(orientation) <= Math.PI /4) ) {
            
                List<SailorAction> actions=new ArrayList<>();
                Marine rudderMarine=mediatorCrewEquipment.marineForRudder();
                Move tmpMove=rudderMarine.getPosition().howToMoveTo( mediatorCrewEquipment.rudderPosition() );
                actions.add( new MoveAction(rudderMarine.getId(), tmpMove.getXdistance(),tmpMove.getYdistance() ));
                actions.add( new Turn(rudderMarine.getId(),orientation) );

                return actions;
            // TODO renvoyer la liste dans le cas où il y a un gouvernail
            /*
             * Reste cas où le gouvernail seul ne suffit pas
             * cas où le gouvernail tout seul ne suffit pas
             */
        } else if (true) {
            // TODO dans le cas où il n'y a pas de gouvernail
        }
        return null;
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
    List<SailorAction> adjustSpeed(int distance, int currentSpeed, List<Marine> busy) {
        double minAdditionalSpeed = (SPEED * 2) / this.mediatorCrewEquipment.nbOars();
        if (distance < currentSpeed) {
            return List.of();// RIEN A FAIRE
        } else if (currentSpeed + minAdditionalSpeed <= distance) {
            // TODO calculer l'accélération qu'on peut appliquer sans dépasser distance
            //répartir autant de marins des deux cotés pour atteindre l'objectif
            //procéder par paire (1gauche,1droite) et recommencer si nécessaire
            //pourrait etre récursif

        }
        return List.of();

    }
}