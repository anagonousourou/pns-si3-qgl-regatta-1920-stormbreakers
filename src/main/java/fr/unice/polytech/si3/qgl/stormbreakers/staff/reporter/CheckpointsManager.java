package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;

public class CheckpointsManager {

    private final List<Checkpoint> checkpoints;

    public CheckpointsManager(List<Checkpoint> checkpoints) {
        this.checkpoints = new ArrayList<>(checkpoints);

    }

    /**
     * Verifie si le bateau en parametre est dans le nextCheckpoint et retire le
     * nextCheckpoint si c'est le cas
     * 
     * @param boat le bateau en question
     */
    public void updateCheckpoint(Boat boat) {
        // LATER: 14/03/2020 tests
        if (hasNextCheckpoint() && (this.isPosInCheckpoint(boat) || boatCollidesWithNextCheckpoint(boat))) {
            Logger.getInstance().addSeparatorThenLog("Validated:" + checkpoints.remove(0).toLogs());
        }
    }

    boolean boatCollidesWithNextCheckpoint(Boat boat) {
        // LATER: 14/03/2020 Tests
        return boat.collidesWith(nextCheckpoint());

    }

    private boolean hasNextCheckpoint() {
        return !checkpoints.isEmpty();
    }

    private boolean isPosInCheckpoint(IPoint position) {
        
        return checkpoints.get(0).isPtInside(position);
    }

    /**
     * 
     * @return the nextCheckpoint in the List
     */
    public Checkpoint nextCheckpoint() {
        if (hasNextCheckpoint()) {
            return checkpoints.get(0);
        } else {
            return null;
        }

    }

}