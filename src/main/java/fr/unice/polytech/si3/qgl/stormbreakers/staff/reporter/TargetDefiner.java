package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Navigator;

public class TargetDefiner {

    private CheckpointsManager checkpointsManager;

    private StreamManager streamManager;
    private Boat boat;
    private Navigator navigator;
    private static final double EPS = 0.001;

    public TargetDefiner(CheckpointsManager checkpointsManager, StreamManager streamManager, Boat boat,
            Navigator navigator) {

        this.checkpointsManager = checkpointsManager;
        this.streamManager = streamManager;
        this.boat = boat;
        this.navigator = navigator;
    }

    boolean thereIsStreamOnTrajectory() {
        return this.streamManager.thereIsStreamBetween(checkpointsManager.nextCheckpoint().getPosition());
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
            if (streamManager.insideStream()
                    && !streamManager.streamAroundBoat().isPtInside(checkpoint.getPosition())) {
                return this.caseInsideAStream();
            } else if (streamManager.insideStream()
                    && streamManager.streamAroundBoat().isPtInside(checkpoint.getPosition())) {
                // LATER affiner la distance et l'orientation et déplacer dans caseInsideAStream
                double distance = boat.distanceTo(checkpoint.getPosition());
                double orientation = navigator.additionalOrientationNeeded(boat.getPosition(),
                        checkpoint.getPosition().getPoint2D());

                return new TupleDistanceOrientation(distance, orientation);
            }

            else if (thereIsStreamOnTrajectory()) {
                Courant courant = nextStreamOnTrajectory();

                if (courant.isCompatibleWith(boat, checkpoint.getPosition())) {
                    return new TupleDistanceOrientation(boat.distanceTo(checkpoint.getPosition()),
                            navigator.additionalOrientationNeeded(boat.getPosition(),
                                    checkpoint.getPosition().getPoint2D()));
                } else {
                    // LATER eviter le courant pour le moment on fonce droit dedans en augmentant
                    // juste la distance/vitesse à prendre
                    return new TupleDistanceOrientation(
                            boat.distanceTo(checkpoint.getPosition()) + courant.getStrength(),
                            navigator.additionalOrientationNeeded(boat.getPosition(),
                                    checkpoint.getPosition().getPoint2D()));
                }

            } else {// pas de stream du tout ou pas de stream sur la trajectoire
                double distance = checkpoint.getPosition().distanceTo(boat);
                double orientation = navigator.additionalOrientationNeeded(boat.getPosition(),
                        checkpoint.getPosition().getPoint2D());
                return new TupleDistanceOrientation(distance, orientation);
            }

        }

        return null;
    }

    public TupleDistanceOrientation caseInsideAStream() {
        Courant streamAround = this.streamManager.streamAroundBoat();
        Vector courantVector = Vector.createUnitVector(streamAround.getPosition().getOrientation());
        // check somewhere if nextcheckpoint is null
        Point2D cpPoint = this.checkpointsManager.nextCheckpoint().getPosition().getPoint2D();
        Vector trajectoireVector = new Vector(boat, cpPoint);

        double helpness = courantVector.scal(trajectoireVector);

        if (Math.abs(helpness) <= TargetDefiner.EPS) {
            // calculer l'orientation en fonction du déplacement engendre par le courant
            double orientation = navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint);
            double distance = boat.distanceTo(cpPoint) + streamAround.getStrength();

            return new TupleDistanceOrientation(distance, orientation);
        } else if (helpness > 0) {
            // LATER distinguer si le courant nous aide temporairement seulement
            Point2D pointToLeave = this.maximalPointToStay(boat.getPosition().getPoint2D(), cpPoint, courantVector,
                    streamAround);

            if (pointToLeave.distanceTo(boat) <= streamAround.getStrength()) {
                double orientation = navigator.additionalOrientationNeeded(boat.getPosition(), cpPoint);
                double distance = boat.distanceTo(cpPoint);
                return new TupleDistanceOrientation(distance, orientation);
            } else {
                double orientation = streamAround.getPosition().getOrientation() - boat.getOrientation();
                double distance = pointToLeave.distanceTo(boat.getPosition().getPoint2D()) - streamAround.getStrength();
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
            return new TupleDistanceOrientation(distance, orientation);

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

    double helpness(Vector streamVector, Point2D depart, Point2D destination) {
        Vector trajectVector = new Vector(depart, destination);
        return streamVector.scal(trajectVector);
    }

    Point2D calculateEscapePoint(Courant courant, Point2D position) {
        // LATER add strength consideration etc ...
        return courant.closestPointTo(position);
    }

}
