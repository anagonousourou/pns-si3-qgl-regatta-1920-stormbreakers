package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

public class CheckpointManager {

    private final List<Checkpoint> checkpoints;

    public CheckpointManager(List<Checkpoint> checkpoints){
        this.checkpoints=new ArrayList<>(checkpoints);

    }

    /**
     * Verifie si la position en parametre est dans le nextCheckpoint
     * et retire le nextCheckpoint si c'est le cas
     * @param position
     */
	public void updateCheckpoint(Position position) {
        if (hasNextCheckpoint() && isPosInCheckpoint(position)) {
                checkpoints.remove(0);
        }
	}

	private boolean hasNextCheckpoint() {
        return (this.checkpoints!=null && !checkpoints.isEmpty());
    }

    private boolean isPosInCheckpoint(Position position) {
        return checkpoints.get(0).isPtInside(position.getPoint2D());
    }


    /**
     * 
     * @return the nextCheckpoint in the List
     */
	public Checkpoint nextCheckpoint() {
        if(hasNextCheckpoint()) {
            return checkpoints.get(0);
        }
        else{
            return null;
        }
		
	}
    
}