package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;

public class RegattaGoal extends Goal {
    List<Checkpoint> checkpoints;
    
    public RegattaGoal(List<Checkpoint> checkpoints) {
        super("REGATTA");
        this.checkpoints =checkpoints;
    }
}
