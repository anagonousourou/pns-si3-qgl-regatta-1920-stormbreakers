package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Shape {
    private String type;
    @JsonCreator
    Shape(@JsonProperty("type") String type) {
        this.type = type;
    }
}
