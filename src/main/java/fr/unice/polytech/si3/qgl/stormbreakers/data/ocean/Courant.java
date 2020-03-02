package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.RectanglePositioned;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

public class Courant extends OceanEntity{
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

    public Point2D closestPointTo(Point2D point2d) {
        var tmp=new RectanglePositioned((Rectangle) this.shape, this.position).closestPointTo(point2d);
        if(tmp.isPresent()){
            return tmp.get();
        }
        //should never happen
        return null;
    }

    

    
	
	public boolean bringCloserCp(Checkpoint cp, Boat boat) {
		Rectangle r=(Rectangle)this.shape;
		Point2D nearestPoint=r.findPointNearestToPosition(cp.getPosition(),this.position);
		Point2D cpPoint2D = cp.getPosition().getPoint2D();
		Point2D boatPoint2D = boat.getPosition().getPoint2D();
		if(nearestPoint.getDistanceTo(cpPoint2D)<boatPoint2D.getDistanceTo(cpPoint2D)) {
			return r.haveGoodOrientation( cp, boatPoint2D,this.getPosition().getPoint2D());
		}
		return false;
	}
    /**
     * Dis si le courant est compatible avec le traject défini par les parametres
     * @param depart
     * @param destination
     * @return
     */
	public boolean isCompatibleWith(IPoint depart,IPoint destination) {
        Vector courantVector=Vector.createUnitVector( this.getPosition().getOrientation() );
        
        Vector trajectoirVector= new Vector(depart, destination);

        double helpness=courantVector.scal(trajectoirVector);
        return helpness >= Utils.EPSILON;
	}

    @Override
    public double getOrientation() {
        return this.position.getOrientation();
    }



}
