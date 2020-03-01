package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
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

    public Point2D closestPointTo(Point2D point2d){
        return new RectanglePositioned((Rectangle)this.shape, this.position).closestPointTo(point2d).get();
    }

	
	public boolean bringCloserCp(Checkpoint cp, Boat boat) {
		
		Rectangle r=(Rectangle)this.shape;
		Point2D nearestPoint=r.findPointNearestToPosition(cp.getPosition(),this.position);
		Point2D cpPoint2D = new Point2D(cp.getPosition().getX(),cp.getPosition().getY());
		Point2D boatPoint2D = new Point2D(boat.getPosition().getX(),boat.getPosition().getY());
		if(nearestPoint.getDistanceTo(cpPoint2D)<boatPoint2D.getDistanceTo(cpPoint2D)) {
			
		}
		return false;
	}

}
