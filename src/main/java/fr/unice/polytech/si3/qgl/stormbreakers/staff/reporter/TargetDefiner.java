package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;



import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Navigator;

public class TargetDefiner  {

    private CheckpointsManager checkpointsManager;
    private InputParser parser;
    private StreamManager streamManager;
    private Boat boat;
    private Navigator navigator;
    private static final double EPS = 0.001;

    public TargetDefiner(CheckpointsManager checkpointsManager, InputParser parser,StreamManager streamManager,Boat boat,Navigator navigator) {
        this.parser = parser;
        this.checkpointsManager = checkpointsManager;
        this.streamManager=streamManager;
        this.boat=boat;
        this.navigator=navigator;
    }

    boolean thereIsStreamOnTrajectory(){
        return false;
    }


    TupleDistanceOrientation caseInsideAStream(){
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

            Point2D pointToLeave=this.maximalPointToStay(boat.getPosition().getPoint2D(), cpPoint, courantVector, streamAround.getShape());
            double orientation=streamAround.getPosition().getOrientation()-boat.getOrientation();

            
            double distance;
            //PAS FINI
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

    
   

}