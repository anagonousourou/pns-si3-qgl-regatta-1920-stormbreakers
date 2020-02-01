package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LiftSail extends SailorAction {

    @JsonCreator
    LiftSail( @JsonProperty("sailorId") int sailorId) {
        super(sailorId,ActionType.LIFTSAIL.actionCode);
    }

}
