package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.Logable;

public class OarAction extends SailorAction implements Logable {

    @JsonCreator
    public OarAction(@JsonProperty("sailorId") int sailorId) {
        super(sailorId, ActionType.OAR.actionCode);
    }

    @Override
    public String toString() {
        return "OarAction( id:" + this.getSailorId() + " )";
    }

    @Override
    public String toLogs() {
        return ActionType.OAR.shortCode + this.getSailorId();
    }
}
