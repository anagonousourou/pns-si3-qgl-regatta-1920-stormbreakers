package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;

public class Moving extends SailorAction implements Logable {
    private int xdistance;
    private int ydistance;

    @JsonCreator
    public Moving(@JsonProperty("sailorId") int sailorId, @JsonProperty("xdistance") int xdistance,
            @JsonProperty("ydistance") int ydistance) {
        super(sailorId, ActionType.MOVING.actionCode);
        this.xdistance = xdistance;
        this.ydistance = ydistance;
    }

    @JsonProperty("xdistance")
    public int getXdistance() {
        return xdistance;
    }

    @JsonProperty("ydistance")
    public int getYdistance() {
        return ydistance;
    }

    public void applyTo(Marin sailor) {
        if (sailor != null)
            sailor.move(xdistance, ydistance);
    }

    @Override
    public String toString() {
        return "Moving( idsailor: " + this.getSailorId() + ", xdistance: " + this.xdistance + ", ydistance: "
                + this.ydistance + " )";
    }

    @Override
    public String toLogs() {
        return ActionType.MOVING.shortCode + "(" + this.getSailorId() + "|" + this.xdistance + "," + this.ydistance + ")";
    }
}
