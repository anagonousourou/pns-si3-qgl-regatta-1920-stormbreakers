package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
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
            Logger.getInstance().log(checkpoint.toString());
            boolean insideStream = streamManager.insideStream();

            if (insideStream && !streamManager.streamAroundBoat().isPtInside(checkpoint)) {
                Logger.getInstance().log("inside stream " + checkpoint);
                // the boat is inside a stream but the checkpoint is not inside the same stream
                return this.caseInsideAStream();
            } else if (insideStream && streamManager.streamAroundBoat().isPtInside(checkpoint)) {
                var courant = streamManager.streamAroundBoat();
                Logger.getInstance().log("both inside stream " + checkpoint);
                // the boat and the stream are inside the same stream
                List<IPoint> trajectory = streamManager.trajectoryBoatAndCheckpointInsideStream(boat, checkpoint);

                if (!courant.isCompatibleWith(boat, checkpoint)) {
                    return new TupleDistanceOrientation(trajectory.get(1).distanceTo(boat) + courant.getStrength(),
                            this.navigator.additionalOrientationNeeded(boat.getPosition(), trajectory.get(1)));
                }

                double speedDueToStream = Math
                        .cos(courant.getPosition().getOrientation() - new Vector(boat, checkpoint).getOrientation())
                        * courant.getStrength();

                if (trajectory.get(1).distanceTo(boat) > courant.getStrength() + Utils.MAX_SPEED_OARS
                        && trajectory.get(1).distanceTo(boat) < 2 * courant.getStrength() + Utils.MAX_SPEED_OARS) {
                    return new TupleDistanceOrientation(0.0,
                            this.navigator.additionalOrientationNeeded(boat.getPosition(), trajectory.get(1)));
                }
                return new TupleDistanceOrientation(trajectory.get(1).distanceTo(boat) - speedDueToStream,
                        this.navigator.additionalOrientationNeeded(boat.getPosition(), trajectory.get(1)));

            }

            else if (this.thereIsObstaclesOnTrajectory()) {
                Logger.getInstance().log("obstacle on trajet " + checkpoint);
                List<IPoint> trajectory = this.streamManager.trajectoryToAvoidObstacles(boat, checkpoint);

                return new TupleDistanceOrientation(trajectory.get(1).distanceTo(boat),
                        this.navigator.additionalOrientationNeeded(boat.getPosition(), trajectory.get(1)));

            } else {// pas de stream du tout ou pas de stream sur la trajectoire
                Logger.getInstance().log("nothing " + checkpoint);
                double distance = checkpoint.distanceTo(boat);
                double orientation = navigator.additionalOrientationNeeded(boat.getPosition(), checkpoint);
                return new TupleDistanceOrientation(distance, orientation);
            }

        }

        return null;
    }

    public TupleDistanceOrientation caseInsideAStream() {
        Courant streamAround = this.streamManager.streamAroundBoat();

        IPoint cpPoint = this.checkpointsManager.nextCheckpoint();

        double helpness = streamAround.helpness(boat, cpPoint);
        // courantVector est perpendiculaire à la trajectoire
        if (Utils.within(helpness, Utils.EPS)) {
            Logger.getInstance().log("Helpness nulle:" + helpness);
            double orientation = navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint);
            double distance = boat.distanceTo(cpPoint) + streamAround.getStrength();

            var tmp = new TupleDistanceOrientation(distance, orientation);
            Logger.getInstance().log(tmp.toString());
            return tmp;
        } else if (helpness > 0) {
            Logger.getInstance().log("helpness positive:" + helpness);

            IPoint pointToLeave = streamAround.maximalPointToStay(boat.getPosition().getPoint2D(), cpPoint);

            double speedDueToStream = streamAround.speedProvided(boat, cpPoint);
            if (pointToLeave.distanceTo(boat) <= streamAround.getStrength()) {
                //
                double orientation = navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint);

                double distance = boat.distanceTo(pointToLeave) - speedDueToStream;
                var tmp = new TupleDistanceOrientation(distance, orientation);
                Logger.getInstance().log("PointToLeaveClose and Stream Fast:" + tmp.toString());
                return tmp;
            } else {
                double orientation = streamAround.getPosition().getOrientation() - boat.getOrientation();
                double distance = pointToLeave.distanceTo(boat) - speedDueToStream;
                var tmp = new TupleDistanceOrientation(distance, orientation);
                Logger.getInstance().log("PointToLeaveNotSoClose:" + tmp.toString());
                return tmp;
            }
        }

        else if (helpness < 0) {
            Logger.getInstance().log("helpness negative: " + helpness);
            List<IPoint> trajet = this.streamManager.trajectoryLeaveStreamAndReachPoint(boat, cpPoint);
            double orientation = navigator.additionalOrientationNeeded(boat.getPosition(), trajet.get(1));
            double distance = boat.distanceTo(trajet.get(1));
            Logger.getInstance().log("NextTarget:" + trajet.get(1).toString());
            return new TupleDistanceOrientation(distance + streamAround.getStrength(), orientation);

        }

        return null;

    }

}
