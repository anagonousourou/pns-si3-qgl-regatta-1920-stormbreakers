package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.Logable;

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

    @Override
    public String toLogs() {
        return getMode()+" "+checkpointsToString();
    }

    private String checkpointsToString() {
        List<Logable> logables = checkpoints.stream().map((Checkpoint cp)->(Logable) cp).collect(Collectors.toList());
        String result = Logable.listToLogs(logables,"|", "[", "]");
        return "CP" + result;
    }
}
