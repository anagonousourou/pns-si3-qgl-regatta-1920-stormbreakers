package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.EquipmentType;

public class LiftSail extends SailorAction {

    @JsonCreator
    public LiftSail(@JsonProperty("sailorId") int sailorId) {
        super(sailorId, ActionType.LIFTSAIL.actionCode);
    }

    @Override
    public String toLogs() {
        return ActionType.LIFTSAIL.shortCode + this.getSailorId();
    }

    @Override
    public String toString() {
        return String.format("LiftSail(id :%d)", this.getSailorId());
    }

    @Override
    public String compatibleEquipmentType() {
       return EquipmentType.SAIL.code;
    }

}
