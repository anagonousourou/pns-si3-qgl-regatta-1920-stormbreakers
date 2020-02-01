package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public class Checkpoint {
    private Position position;
    private Shape shape;
    
    @JsonCreator
    public Checkpoint(@JsonProperty("position") Position pos, @JsonProperty("shape") Shape shape) {
    	this.position=pos;
    	this.shape=shape;
    }

    @JsonProperty("position")
    public Position getPosition() {
        return position;
    }

    @JsonProperty("shape")
    public Shape getShape() {
        return shape;
    }

    public boolean isPosInside(Position pos) {
        return isPosInside(pos.getX(),pos.getY());
    }

    private boolean isPosInside(double x, double y) {
        // On se replace par rapport au centre de la forme
        return shape.isPosInside(x-position.getX(),y-position.getY());
    }
}
