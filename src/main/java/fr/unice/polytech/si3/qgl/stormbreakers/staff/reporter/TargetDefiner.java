package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Navigator;

public class TargetDefiner {

    private CheckpointsManager checkpointsManager;

    private StreamManager streamManager;
    private Boat boat;
    private Navigator navigator;

    private Cartographer cartographer;

    public TargetDefiner(CheckpointsManager checkpointsManager, StreamManager streamManager, Boat boat,
            Navigator navigator, Cartographer cartographer) {

        this.checkpointsManager = checkpointsManager;
        this.streamManager = streamManager;
        this.boat = boat;
        this.navigator = navigator;
        this.cartographer = cartographer;
    }

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
        IPoint cp = checkpointsManager.nextCheckpoint();

        if (!this.streamManager.thereIsObstacleBetweenOrAround(cp) && !this.streamManager.insideOpenStream()) {
            System.out.println("Not using Pathfinding:");
            return new TupleDistanceOrientation(cp.distanceTo(boat),
                    this.navigator.additionalOrientationNeeded(boat.getPosition(), cp));

        }
        System.out.println("Using pathfinding:");
        IPoint target = cartographer.nextPoint();
        Logger.getInstance().log(target.toString());
        boolean insideStream = streamManager.insideOpenStream();

        if (insideStream && streamManager.streamAroundBoat().isPtInside(target)) {
            var courant = streamManager.streamAroundBoat();
            Logger.getInstance().log("both inside stream " + target);
            // the boat and the stream are inside the same stream

            if (!courant.isCompatibleWith(boat, target)) {
                return new TupleDistanceOrientation(target.distanceTo(boat) + courant.getStrength(),
                        this.navigator.additionalOrientationNeeded(boat.getPosition(), target));
            }

            double speedDueToStream = courant.speedProvided(boat, target);

            if (target.distanceTo(boat) > courant.getStrength() + Utils.MAX_SPEED_OARS
                    && target.distanceTo(boat) < 2 * courant.getStrength() + Utils.MAX_SPEED_OARS) {
                return new TupleDistanceOrientation(0.0,
                        this.navigator.additionalOrientationNeeded(boat.getPosition(), target), true);
            }
            return new TupleDistanceOrientation(target.distanceTo(boat) - speedDueToStream,
                    this.navigator.additionalOrientationNeeded(boat.getPosition(), target));

        }

        return new TupleDistanceOrientation(target.distanceTo(boat),
                this.navigator.additionalOrientationNeeded(boat.getPosition(), target));
    }

    public boolean curveTrajectoryIsSafe(TupleDistanceOrientation objectif) {
        int nbStep = 50;
        if (Utils.within(objectif.getOrientation(), Utils.EPS)) {
            return true;
        }
        double x = this.boat.x();
        double y = this.boat.y();
        double orientation = this.boat.getOrientation();

        double vitesseLineaire = objectif.getDistance();
        double vitesseOrientation = objectif.getOrientation();
        
        for (int i = 0; i < nbStep; i++) {

            x = x + Math.cos(orientation) * vitesseLineaire / nbStep;
            y = y + Math.sin(orientation) * vitesseLineaire / nbStep;
            orientation = orientation + vitesseOrientation / nbStep;

            if (this.streamManager.pointIsInsideOrAroundReefOrBoat(new Point2D(x, y))) {
                return false;
            }

        }
        return true;
    }

}
