package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegattaGoal extends Goal {
    private List<Checkpoint> checkpoints;

    @JsonCreator
    public RegattaGoal(@JsonProperty("checkpoints") List<Checkpoint> checkpoints) {
        super("REGATTA");
        this.checkpoints = checkpoints;
    }

    @JsonProperty("checkpoints")
    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
}
