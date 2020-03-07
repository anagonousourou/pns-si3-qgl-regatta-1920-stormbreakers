package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Navigator;

public class TargetDefiner {

    private CheckpointsManager checkpointsManager;

    private StreamManager streamManager;
    private Boat boat;
    private Navigator navigator;

    public TargetDefiner(CheckpointsManager checkpointsManager, StreamManager streamManager, Boat boat,
            Navigator navigator) {

        this.checkpointsManager = checkpointsManager;
        this.streamManager = streamManager;
        this.boat = boat;
        this.navigator = navigator;
    }

    boolean thereIsStreamOnTrajectory() {
        return this.streamManager.thereIsStreamBetween(checkpointsManager.nextCheckpoint());
    }
    boolean thereIsObstaclesOnTrajectory() {
        return this.streamManager.thereIsObstacleBetween(checkpointsManager.nextCheckpoint());
    }

    Courant nextStreamOnTrajectory() {
        if (thereIsStreamOnTrajectory()) {
            return this.streamManager.firstStreamBetween(checkpointsManager.nextCheckpoint().getPosition());
        }
        return null;
    }

    public TupleDistanceOrientation defineNextTarget() {
        Checkpoint checkpoint = checkpointsManager.nextCheckpoint();
        if (checkpoint != null) {
            boolean insideStream = streamManager.insideStream();
            
            if (insideStream && !streamManager.streamAroundBoat().isPtInside(checkpoint)) {
                //the boat is inside a stream but the checkpoint is not inside the same stream
                return this.caseInsideAStream();
            } else if (insideStream && streamManager.streamAroundBoat().isPtInside(checkpoint)) {
                //the boat and the stream are inside the same stream
                List<IPoint> trajectory = streamManager
                        .trajectoryBoatAndCheckpointInsideStream(streamManager.streamAroundBoat(), boat, checkpoint);
                if (trajectory.size() > 1) {
                    return new TupleDistanceOrientation(trajectory.get(1).distanceTo(boat),
                            this.navigator.additionalOrientationNeeded(boat.getPosition(), trajectory.get(1)));
                }
                else{
                    //TODO affiner en fonction de la force et la direction du courant
                    return new TupleDistanceOrientation(boat.distanceTo(checkpoint.getPosition()),
                            navigator.additionalOrientationNeeded(boat.getPosition(), checkpoint));
                }
            }

            else if (this.thereIsObstaclesOnTrajectory()) {
                List<IPoint> trajectory= this.streamManager.trajectoryToAvoidObstacles(boat,checkpoint);
                if (trajectory.size() > 1) {
                    return new TupleDistanceOrientation(trajectory.get(1).distanceTo(boat),
                            this.navigator.additionalOrientationNeeded(boat.getPosition(), trajectory.get(1)));
                }
                else{
                    //TODO on devrait éviter d'en venir ici puisque si l'obstacle est un récif ou un 
                    //courant trop fort on n'arrivera jamais à destination
                    return new TupleDistanceOrientation(boat.distanceTo(checkpoint.getPosition()),
                            navigator.additionalOrientationNeeded(boat.getPosition(), checkpoint));
                }

            } else {// pas de stream du tout ou pas de stream sur la trajectoire
                double distance = checkpoint.distanceTo(boat);
                double orientation = navigator.additionalOrientationNeeded(boat.getPosition(),
                        checkpoint);
                return new TupleDistanceOrientation(distance, orientation);
            }

        }

        return null;
    }

    public TupleDistanceOrientation caseStreamOnTrajectory() {
        IPoint cpPoint = this.checkpointsManager.nextCheckpoint();
        List<IPoint> trajectoire;
        if (this.streamManager.pointIsInsideStream(cpPoint)) {
            trajectoire = this.streamManager.trajectoryToReachAPointInsideStream(boat,cpPoint);
        } else {
            trajectoire = this.streamManager.trajectoryToAvoidObstacles(boat,cpPoint);
        }

        if (trajectoire.size() > 1) {
            return new TupleDistanceOrientation(trajectoire.get(1).distanceTo(boat),
                    this.navigator.additionalOrientationNeeded(boat.getPosition(), trajectoire.get(1)));
        } else {
            var optCourant = this.streamManager.streamAroundPoint(cpPoint);
            if (optCourant.isPresent()) {
                var courant = optCourant.get();

                if (courant.isCompatibleWith(boat, cpPoint)) {
                    return new TupleDistanceOrientation(boat.distanceTo(cpPoint),
                            this.navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint));
                } else {
                    return new TupleDistanceOrientation(boat.distanceTo(cpPoint) + courant.getStrength(),
                            this.navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint));
                }

            }

            else {
                return new TupleDistanceOrientation(boat.distanceTo(cpPoint),
                        this.navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint));
            }
        }

    }

    public TupleDistanceOrientation caseInsideAStream() {
        Courant streamAround = this.streamManager.streamAroundBoat();
        Vector courantVector = Vector.createUnitVector(streamAround.getPosition().getOrientation());
        
        Point2D cpPoint = this.checkpointsManager.nextCheckpoint().getPosition().getPoint2D();
        

        double helpness = this.helpness(courantVector, boat, cpPoint);

        if (Utils.within(helpness, Utils.EPS)) {
            // calculer l'orientation en fonction du déplacement engendre par le courant
            double orientation = navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint);
            double distance = boat.distanceTo(cpPoint) + streamAround.getStrength();

            return new TupleDistanceOrientation(distance, orientation);
        } else if (helpness > 0) {
            
            Point2D pointToLeave = this.maximalPointToStay(boat.getPosition().getPoint2D(), cpPoint, courantVector,
                    streamAround);

            if (pointToLeave.distanceTo(boat) <= streamAround.getStrength()) {
                double orientation = navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint);
                double distance = boat.distanceTo(cpPoint);
                return new TupleDistanceOrientation(distance, orientation);
            } else {
                double orientation = streamAround.getPosition().getOrientation() - boat.getOrientation();
                double distance = pointToLeave.distanceTo(boat) - streamAround.getStrength();
                return new TupleDistanceOrientation(distance, orientation);
            }
        }

        else if (helpness < 0) {
            // LATER Afin de rester cohérent avec la "stratégie" qui fait foncer le bateau
            // tout droit
            // dans le checkpoint meme si il y a un courant défavorable on continue notre
            // chemin
            // quand la "stratégie" aura changé on changera cette partie
            double orientation = navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint);
            double distance = boat.distanceTo(cpPoint);
            return new TupleDistanceOrientation(distance+streamAround.getStrength(), orientation);

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
    Point2D maximalPointToStay(Point2D depart, Point2D destination, Vector courant, Courant surface) {
        Point2D current = depart;
        Point2D prev = current;
        Vector biggerStreamVector = courant.scaleVector(2);
        while (helpness(courant, current, destination) > 0 && surface.isPtInside(current)) {

            prev = current;

            current = current.getTranslatedBy(biggerStreamVector);

        }
        return prev;

    }

    double helpness(Vector streamVector, IPoint depart, IPoint destination) {
        Vector trajectVector = new Vector(depart, destination);
        return streamVector.scal(trajectVector);
    }

    

}
