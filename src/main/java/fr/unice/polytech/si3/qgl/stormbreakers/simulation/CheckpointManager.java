package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

class CheckpointManager {

    List<Checkpoint> checkpoints;
    public CheckpointManager(List<Checkpoint> checkpoints){
        this.checkpoints=checkpoints;

    }
    /**
     * TODO vérfie si la position en parametre est dans le nextCheckpoint
     * et  retire le nextCheckpoint si c'est le cas
     * @param position
     */
	public void updateCheckpoint(Position position) {
        // pas encore fini d'implémenter
	}
    /**
     * 
     * @return the nextCheckpoint in the List
     */
	public Checkpoint nextCheckpoint() {
		return null;
	}
    
}