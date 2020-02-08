package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LowerSail extends SailorAction {

    @JsonCreator
    LowerSail( @JsonProperty("sailorId") int sailorId) {
        super(sailorId,ActionType.LOWERSAIL.actionCode);
    }

    @Override
    public String toLogs() {
        return ActionType.LOWERSAIL.shortCode + this.getSailorId();
    }
}
