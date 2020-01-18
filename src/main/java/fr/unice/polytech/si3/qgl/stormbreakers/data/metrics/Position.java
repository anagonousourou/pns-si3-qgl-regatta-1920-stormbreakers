package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {
    double x;
    double y;
    double orientation;
    
    @JsonCreator
    public Position(@JsonProperty("x") double x,@JsonProperty("y") double y, @JsonProperty("orientation") double orientation) {
    	this.x=x;
    	this.y=y;
    	this.orientation=orientation;
	}
}
