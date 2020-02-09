package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

class Captain {
    
    private Boat boat;
    private CheckpointManager checkpointManager;
    private Compas compas;
    final private double EPS=0.001;

    Captain(Boat boat, CheckpointManager checkpointManager,Compas compas) {
        this.boat=boat;
        this.checkpointManager=checkpointManager;
        this.compas=compas;

    }
    /**
     * 
     */
    List<SailorAction> nextRound(){
        this.checkpointManager.updateCheckpoint(boat.getPosition());
        //TODO condition pour le cas où on a fini ie plus de nextCheckpoint
        Checkpoint chpoint=this.checkpointManager.nextCheckpoint();

        

        double orientation=this.compas.additionalOrientationNeeded(boat.getPosition(), chpoint.getPosition());
        double distance=boat.getPosition().distanceTo(chpoint.getPosition());

        List<SailorAction> actionsOrientation=this.actionsToOrientate(orientation);
        List<SailorAction> actionsToAdjustSpeed=this.actionsToOrientate(distance);

        List<SailorAction> actions=new ArrayList<>();
        actions.addAll(actionsOrientation);
        actions.addAll(actionsToAdjustSpeed);



        return actions;

    }
    /**
     * Renvoie les actions à effectuer pour que le bateau prenne l'orientation 
     * passée en parametre 
     * @param orientation
     * @return
     */
    List<SailorAction> actionsToOrientate(double orientation){
        if(Math.abs(orientation) < this.EPS ){
            return List.of();
        }
        else if(true){
            //TODO renvyoyer la liste dans le cas où il y a un gouvernail
            /*ne pas oublier qu'on peut subdiviser ie cas où seul le gouvernail 
            suffit et cas où le gouvernail tout seul ne suffit pas */
        }
        else if(true){
            //TODO dans le cas où il n'y a pas de gouvernail
        }
        return null;
    }
    /**
     * Fonction qui envoie des actions pour ajouter des marins
     * pour faire avancer le bateau sans changer l'orientation
     * on doit donc absolument veiller à garder l'équilibre
     * et aussi à ne pas utiliser les marins dispatchés par la fonction
     * actionsToOrientate
     *  pour régler l'angle
     * @param distance
     * @param busy
     * @return
     */
    List<SailorAction> adjustSpeed(int distance,List<Marine>  busy){
        return null;
    }
}