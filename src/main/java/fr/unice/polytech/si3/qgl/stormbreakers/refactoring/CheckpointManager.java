package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

public class CheckpointManager {

    private final List<Checkpoint> checkpoints;
    public CheckpointManager(List<Checkpoint> checkpoints){
        this.checkpoints=checkpoints;

    }
    /**
     * TODO vérfie si la position en parametre est dans le nextCheckpoint
     * et  retire le nextCheckpoint si c'est le cas
     * @param position
     */
	public void updateCheckpoint(Position position) {
        
	}
    /**
     * 
     * @return the nextCheckpoint in the List
     */
	public Checkpoint nextCheckpoint() {
        if(this.checkpoints!=null && !this.checkpoints.isEmpty()) {
            this.checkpoints.get(0);
        }
		return null;
	}
    
}