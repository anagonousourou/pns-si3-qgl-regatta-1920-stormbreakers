package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

public class CheckpointsManager {

    private final List<Checkpoint> checkpoints;

    public CheckpointsManager(List<Checkpoint> checkpoints){
        this.checkpoints=new ArrayList<>(checkpoints);

    }

    /**
     * Verifie si la position en parametre est dans le nextCheckpoint
     * et retire le nextCheckpoint si c'est le cas
     * @param position
     */
	public void updateCheckpoint(IPoint point) {
        if (hasNextCheckpoint() && isPosInCheckpoint(point)) {
                checkpoints.remove(0);
        }
	}

	private boolean hasNextCheckpoint() {
        return (this.checkpoints!=null && !checkpoints.isEmpty());
    }

    private boolean isPosInCheckpoint(IPoint position) {
        return checkpoints.get(0).isPtInside(position);
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