package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Navigator;

public class TargetDefiner  {

    private CheckpointsManager checkpointsManager;
    
    private StreamManager streamManager;
    private Boat boat;
    private Navigator navigator;
    private static final double EPS = 0.001;
    private static final double ARBITRARY_DISTANCE=300;

    public TargetDefiner(CheckpointsManager checkpointsManager, StreamManager streamManager,Boat boat,Navigator navigator) {
        
        this.checkpointsManager = checkpointsManager;
        this.streamManager=streamManager;
        this.boat=boat;
        this.navigator=navigator;
    }

    boolean thereIsStreamOnTrajectory(){
        return this.streamManager.thereIsStreamBetween(checkpointsManager.nextCheckpoint().getPosition() );
    }

    Courant nextStreamOnTrajectory(){
        if(thereIsStreamOnTrajectory()){
            return this.streamManager.firstStreamBetween(boat.getPosition());
        }
        return null;
    }

    public TupleDistanceOrientation defineNextTarget(){
        Checkpoint checkpoint=checkpointsManager.nextCheckpoint();
        if(checkpoint!=null){
            if(streamManager.insideStream() &&  !streamManager.streamAroundBoat().isPtInside(checkpoint.getPosition())){
                return this.caseInsideAStream();
            }
            else if(streamManager.insideStream() && streamManager.streamAroundBoat().isPtInside(checkpoint.getPosition())){
                //LATER affiner la distance et l'orientation et déplacer dans caseInsideAStream
                double distance=boat.getPosition().distanceTo(checkpoint.getPosition());
                double orientation=navigator.additionalOrientationNeeded(boat.getPosition(), checkpoint.getPosition().getPoint2D());

                return new TupleDistanceOrientation(distance, orientation);
            }

            else if(thereIsStreamOnTrajectory()){
                Courant courant=nextStreamOnTrajectory();

                if(courant.isCompatibleWith(boat.getPosition(),checkpoint.getPosition())){
                    return new TupleDistanceOrientation(boat.getPosition().distanceTo(checkpoint.getPosition())
                        , navigator.additionalOrientationNeeded(boat.getPosition(), checkpoint.getPosition().getPoint2D()));
                }
                else{
                    //TODO eviter le courant pour le moment on fonce droit dedans en augmentant juste la distance/vitesse à prendre
                    return new TupleDistanceOrientation(boat.getPosition().distanceTo(checkpoint.getPosition())+courant.getStrength()
                        , navigator.additionalOrientationNeeded(boat.getPosition(), checkpoint.getPosition().getPoint2D()));
                }
                
            }


        }

        return null;
    }


    public TupleDistanceOrientation caseInsideAStream(){
        Courant streamAround=this.streamManager.streamAroundBoat();

        Vector courantVector=Vector.createUnitVector( streamAround.getPosition().getOrientation() );
        //check somewhere if nextcheckpoint is null
        Point2D cpPoint=this.checkpointsManager.nextCheckpoint().getPosition().getPoint2D();
        Vector trajectoireVector=new Vector(boat.getPosition().getPoint2D(),cpPoint );

        double helpness=courantVector.scal(trajectoireVector);

        if(Math.abs(helpness)<=TargetDefiner.EPS){
            //calculer l'orientation en fonction du déplacement engendre par le courant
            double orientation = navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint);
            double distance = boat.getPosition().getPoint2D().distanceTo(cpPoint)+streamAround.getStrength();

            return new TupleDistanceOrientation(distance, orientation);
        }
        else if(helpness > 0){
            //TODO distinguer si le courant nous aide temporairement seulement
            Point2D pointToLeave=this.maximalPointToStay(boat.getPosition().getPoint2D(), cpPoint, courantVector, streamAround.getShape());
            if(pointToLeave.distanceTo(boat.getPosition().getPoint2D()) <= streamAround.getStrength()){
                double orientation=navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint);
                double distance=boat.getPosition().getPoint2D().distanceTo(cpPoint);
                return new TupleDistanceOrientation(distance, orientation);
            }
            else {
                double orientation=streamAround.getPosition().getOrientation()-boat.getOrientation();
                double distance= pointToLeave.distanceTo(boat.getPosition().getPoint2D())-streamAround.getStrength();
                return new TupleDistanceOrientation(distance, orientation);
            }
        }

        else if(helpness < 0){
            Point2D escapePoint=this.calculateEscapePoint(streamAround, boat.getPosition().getPoint2D());
            double orientation= navigator.additionalOrientationNeeded(boat.getPosition(), escapePoint);
            return new TupleDistanceOrientation(TargetDefiner.ARBITRARY_DISTANCE, orientation);
        }

        return null;
        
    }

    /**
     * 
     * @param depart
     * @param destination
     * @param courant
     * @param surface
     * @return
     */
    Point2D maximalPointToStay(Point2D depart, Point2D destination, Vector courant,Shape surface){
        Point2D current=depart;
        Point2D prev=current;
        Vector biggerStreamVector=courant.scaleVector(2);
        while(helpness(courant, current, destination)>0 && surface.isPtInside(current)){
            prev=current;
            current=current.getTranslatedBy(biggerStreamVector);
            
        }
        return prev;

    }

    double helpness(Vector streamVector,Point2D depart, Point2D destination){
        Vector trajectVector=new Vector(depart, destination);
        return streamVector.scal(trajectVector);
    }

    Point2D calculateEscapePoint(Courant courant,Point2D position){
        //TODO LATER add strenght consideration etc ...
        return courant.closestPointTo(position);
    }

    
   

}