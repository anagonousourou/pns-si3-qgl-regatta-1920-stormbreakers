package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import java.util.List;
import java.util.Optional;

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

    public Point2D closestPointTo(IPoint point2d) {
        //LATER check for the type of shape before cast
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
		if(nearestPoint.distanceTo(cpPoint2D)<boatPoint2D.distanceTo(cpPoint2D)) {
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
        return helpness > Utils.EPSILON;
    }
    /**
     * 
     * @param depart
     * @param destination
     * @return
     */
    public boolean isCompletelyCompatibleWith(IPoint depart,IPoint destination){
        Vector courantVector=Vector.createUnitVector( this.getPosition().getOrientation() );
        Vector courantComposantx= new Vector (courantVector.getDeltaX(), 0);
        Vector courantComposanty=new Vector(0, courantVector.getDeltaY());
        

        Vector trajectoirVector= new Vector(depart, destination);
        double helpx=courantComposantx.scal(trajectoirVector);
        double helpy=courantComposanty.scal(trajectoirVector);
        if( Utils.within(helpx, Utils.EPSILON)){
            return helpy> Utils.EPSILON;
        }
        else if(Utils.within(helpy, Utils.EPSILON)){
            return helpx> Utils.EPSILON;
        }
        return  helpx> Utils.EPSILON &&  helpy> Utils.EPSILON;


    }

    /**
     * 
     * @param depart
     * @param destination
     * @return
     */
    public boolean isPartiallyCompatibleWith(IPoint depart,IPoint destination){
        Vector courantVector=Vector.createUnitVector( this.getPosition().getOrientation() );
        Vector courantComposantx= new Vector (courantVector.getDeltaX(), 0);
        Vector courantComposanty=new Vector(0, courantVector.getDeltaY());

        Vector trajectoirVector= new Vector(depart, destination);
        
        return courantComposantx.scal(trajectoirVector) > Utils.EPSILON || courantComposanty.scal(trajectoirVector) > Utils.EPSILON;


    }
    /**
     * LATER test
     * @param point
     * @return
     */
    public Optional<IPoint> getAwayPoint(IPoint point){
        var rectanglePositioned=new RectanglePositioned((Rectangle)this.shape, this.position);

        List<IPoint> points= rectanglePositioned.pointsOfRectangle(0.01);
        return points.stream().filter(p->this.isCompatibleWith(point,p) ).min(
            (p1,p2)->Double.compare(p1.distanceTo(point), p2.distanceTo(point))
        );
    }

    @Override
    public double getOrientation() {
        return this.position.getOrientation();
    }



}
