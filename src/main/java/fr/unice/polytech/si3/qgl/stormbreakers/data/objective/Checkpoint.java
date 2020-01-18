package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public class Checkpoint {
    Position position;
    Shape shape;
    
    @JsonCreator
    public Checkpoint(@JsonProperty("position") Position pos, @JsonProperty("shape") Shape shape) {
    	this.position=position;
    	this.shape=shape;
    }
}
