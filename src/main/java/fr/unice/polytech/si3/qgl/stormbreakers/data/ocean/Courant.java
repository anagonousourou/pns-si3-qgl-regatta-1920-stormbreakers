package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.RectanglePositioned;

public class Courant extends OceanEntity {
    private double strength;

    public Courant(@JsonProperty("position") Position position, @JsonProperty("shape") Shape shape, @JsonProperty("strength") double strength) {
        super("stream",position,shape);
        this.strength = strength;
    }

	public double getStrength() {
		return strength;
    }
    
    @Override
    public String toString() {
        return String.format("%s(strength= %f, position= %s, shape= %s)", this.getClass().getSimpleName(),strength,position.toString(),shape.toString());
    }

    @Override
    public boolean intersectsWith(LineSegment2D lineSegment2D) {
        if(this.shape.getType().equals("rectangle")){
            return new RectanglePositioned((Rectangle)shape,this.position).intersectsWith(lineSegment2D);
        }
        else{
            return false;
        }
    }

}
