package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.EquipmentType;

public class UseWatch extends SailorAction {

    @JsonCreator
	public UseWatch(@JsonProperty("sailorId")  int sailorId) {
        super(sailorId,ActionType.USEWATCH.actionCode);
    }

    @Override
    public String toLogs() {
        return ActionType.USEWATCH.shortCode + this.getSailorId();
    }

    @Override
    public String compatibleEquipmentType() {
        return EquipmentType.WATCH.code;
    }

}
