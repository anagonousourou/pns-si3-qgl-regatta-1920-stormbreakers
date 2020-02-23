package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.MovementPath;

public class MoveAction extends SailorAction implements Logable {
    private int xdistance;
    private int ydistance;

    @JsonCreator
    public MoveAction(@JsonProperty("sailorId") int sailorId, @JsonProperty("xdistance") int xdistance,
            @JsonProperty("ydistance") int ydistance) {
        super(sailorId, ActionType.MOVING.actionCode);
        this.xdistance = xdistance;
        this.ydistance = ydistance;
    }

    public MoveAction(int id, MovementPath pathTo) {
        this(id,pathTo.getDeltaX(),pathTo.getDeltaY());
    }

    @JsonProperty("xdistance")
    public int getXdistance() {
        return xdistance;
    }

    @JsonProperty("ydistance")
    public int getYdistance() {
        return ydistance;
    }

    public boolean longerThan(int distance) {
        return Math.abs(this.xdistance) + Math.abs(this.ydistance) > distance;
    }

    @Override
    public String toString() {
        return "MoveAction( id: " + this.getSailorId() + ", xd: " + this.xdistance + ", yd: " + this.ydistance + " )";
    }

    @Override
    public String toLogs() {
        return ActionType.MOVING.shortCode + "(" + this.getSailorId() + "|" + this.xdistance + "," + this.ydistance
                + ")";
    }

    @Override
    public String compatibleEquipmentType() {
        return "";
    }
}