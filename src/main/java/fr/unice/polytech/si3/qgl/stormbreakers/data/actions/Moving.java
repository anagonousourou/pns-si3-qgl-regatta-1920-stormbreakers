package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Moving extends SailorAction {
    private int xdistance;
    private int ydistance;

    @JsonCreator
    Moving(
            @JsonProperty("sailorId") int sailorId,
            @JsonProperty("xdistance") int xdistance,
            @JsonProperty("ydistance") int ydistance) {
        super(sailorId,"MOVING");
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
}
