package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Turn extends SailorAction {
    private double rotation;

    @JsonCreator
    Turn(
            @JsonProperty("sailorId") int sailorId,
            @JsonProperty("rotation") double rotation) {
        super(sailorId,"TURN");
        this.rotation = rotation;
    }

    @JsonProperty("rotation")
    public double getRotation() {
        return rotation;
    }
}