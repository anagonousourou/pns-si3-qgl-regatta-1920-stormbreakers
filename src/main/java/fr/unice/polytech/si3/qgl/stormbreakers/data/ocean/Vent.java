package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Vent {
    private double orientation;
    private double strength;

    @JsonCreator
    public Vent(
            @JsonProperty("orientation") double orientation,
            @JsonProperty("strength") double strength
    ) {
        this.orientation = orientation;
        this.strength = strength;
    }

    @JsonProperty("orientation")
    public double getOrientation() {
        return orientation;
    }

    @JsonProperty("strength")
    public double getStrength() {
        return strength;
    }
}
