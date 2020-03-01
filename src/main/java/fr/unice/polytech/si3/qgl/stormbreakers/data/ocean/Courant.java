package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.RectanglePositioned;

public class Courant extends OceanEntity {
    private double strength;

    public Courant(@JsonProperty("position") Position position, @JsonProperty("shape") Shape shape,
            @JsonProperty("strength") double strength) {
        super("stream", position, shape);
        this.strength = strength;
    }

    public double getStrength() {
        return strength;
    }

    @Override
    public String toString() {
        return String.format("%s(strength= %f, position= %s, shape= %s)", this.getClass().getSimpleName(), strength,
                position.toString(), shape.toString());
    }

    @Override
    public boolean intersectsWith(LineSegment2D lineSegment2D) {
        if (this.shape.getType().equals("rectangle")) {
            return new RectanglePositioned((Rectangle) shape, this.position).intersectsWith(lineSegment2D);
        } else {
            return false;
        }
    }

    public Point2D closestPointTo(Point2D point2d) {
        return new RectanglePositioned((Rectangle) this.shape, this.position).closestPointTo(point2d).get();
    }

    

    // LATER turn this into a interface method to implement TODO
    public boolean isPtInside(IPoint pt) {
        return this.isPtInside(pt.x(), pt.y());
    }

    private boolean isPtInside(double x, double y) {
        // On se replace par rapport au centre de la forme
        Point2D pt = new Point2D(x - position.x(), y - position.y());
        double orientation = position.getOrientation();
        // On compense l'orientation du checkpoint
        if (orientation != 0)
            pt = pt.getRotatedBy(-orientation);
        return shape.isPtInside(pt);
    }
	
	public boolean bringCloserCp(Checkpoint cp, Boat boat) {
		
		Rectangle r=(Rectangle)this.shape;
		Point2D nearestPoint=r.findPointNearestToPosition(cp.getPosition(),this.position);
		Point2D cpPoint2D = new Point2D(cp.getPosition().x(),cp.getPosition().y());
		Point2D boatPoint2D = new Point2D(boat.getPosition().x(),boat.getPosition().y());
		if(nearestPoint.getDistanceTo(cpPoint2D)<boatPoint2D.getDistanceTo(cpPoint2D)) {
			return r.haveGoodOrientation( cp, this.getPosition(), boat);
		}
		return false;
	}



}
