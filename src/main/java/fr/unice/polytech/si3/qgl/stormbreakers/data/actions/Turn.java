package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.EquipmentType;

public class Turn extends SailorAction {
    private double rotation;

    @JsonCreator
    public Turn(@JsonProperty("sailorId") int sailorId, @JsonProperty("rotation") double rotation) {
        super(sailorId, ActionType.TURN.actionCode);
        this.rotation = rotation;
    }

    @JsonProperty("rotation")
    public double getRotation() {
        return rotation;
    }

    @Override
    public String toLogs() {
        return ActionType.TURN.shortCode + this.getSailorId();
    }

    @Override
    public String compatibleEquipmentType() {
        return EquipmentType.RUDDER.code;
    }

    @Override
    public String toString() {
        return String.format("%s( id:%d, orientation:%f )", this.getClass().getSimpleName(), this.sailorId,
                this.rotation);
    }
}
