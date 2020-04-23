package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntityType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.ParsingException;
import fr.unice.polytech.si3.qgl.stormbreakers.io.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.IPoint;

/**
 * Classe pour gérér les streams
 */
public class StreamManager implements PropertyChangeListener {

    private List<Courant> courants;

    private List<OceanEntity> obstacles;
    private List<OceanEntity> boatsAndReefs;
    private List<Recif> recifs;
    private final InputParser parser;
    private final Boat boat;

    /**
     * 
     * @param parser
     * @param boat
     */
    public StreamManager(InputParser parser, Boat boat) {
        this.parser = parser;
        this.boat = boat;
        this.courants = List.of();
        this.obstacles = List.of();
        this.recifs = List.of();
        this.boatsAndReefs = List.of();

    }

    /**
     * Method to say if the boat is currently within a stream
     * 
     * @return if the boat is really inside a stream
     */
    public boolean insideOpenStream() {
        return this.courants.stream().anyMatch(courant -> courant.isInsideOpenSurface(boat));
    }

    /**
     * 
     * @param point
     * @return if point is inside a stream included limits
     */
    public boolean pointIsInsideStream(IPoint point) {
        return this.courants.stream().anyMatch(courant -> courant.isPtInside(point));
    }

    /**
     * 
     * @param point
     * @return if point is inside a stream excluding limits
     */
    public boolean pointIsInsideOpenStream(IPoint point) {
        return this.courants.stream().anyMatch(courant -> courant.isInsideOpenSurface(point));
    }

    public boolean pointIsInsideOrAroundReefOrBoat(IPoint point) {
        return this.boatsAndReefs.stream().anyMatch(recif -> recif.isInsideWrappingSurface(boat.securityMargin(), point));
    }

    /**
     * 
     * @return the stream the boat is inside null if the boat is not inside any
     *         stream
     */
    public Courant streamAroundBoat() {
        var optCourant = this.courants.stream().filter(courant -> courant.isPtInside(boat)).findAny();
        if (optCourant.isPresent()) {
            return optCourant.get();
        } else {
            return null;
        }
    }

    /**
     * 
     * @param point
     * @return an optional of the stream surrounding point
     */
    public Optional<Courant> streamAroundPoint(IPoint point) {
        return this.courants.stream().filter(courant -> courant.isPtInside(point)).findAny();
    }

    /**
     * Méthode to say if there is any streams from the boat to the given position
     * 
     * @param position
     * @return
     */
    public boolean thereIsStreamBetween(IPoint position) {
        LineSegment2D segment2d = new LineSegment2D(position, boat);
        return this.courants.stream().anyMatch(courant -> courant.intersectsWith(segment2d));

    }

    /**
     * 
     * @param position
     * @return if there exists a stream / reef between boat and position
     */
    public boolean thereIsObstacleBetween(IPoint position) {
        return this.thereIsObstacleBetween(this.boat, position);
    }

    /**
     * 
     * @param depart
     * @param destination
     * @return if there exists a stream between depart and destination
     */
    public boolean thereIsObstacleBetween(IPoint depart, IPoint destination) {
        LineSegment2D segment2d = new LineSegment2D(depart, destination);

        return this.obstacles.stream().anyMatch(obstacle -> obstacle.intersectsWith(segment2d));
    }

    /**
     * 
     * @param depart
     * @param destination
     * @return
     */
    public boolean thereIsRecifsBetweenOrAround(IPoint depart, IPoint destination) {
        LineSegment2D segment2d = new LineSegment2D(depart, destination);
        return this.recifs.stream().anyMatch(obstacle -> obstacle.intersectsWithWrappingSurface(boat.securityMargin(), segment2d));
    }

    public boolean thereIsObstacleBetweenOrAround(IPoint cp) {
        LineSegment2D segment2d = new LineSegment2D(boat, cp);
        return this.obstacles.stream().anyMatch(obstacle -> obstacle.intersectsWithWrappingSurface(boat.securityMargin(), segment2d));
    }

    /**
     * Method to handle edge cases for speedProvided
     * 
     * @param depart
     * @param destination
     * @return
     */
    double speedProvidedLimits(IPoint depart, IPoint destination) {
        if (this.pointIsInsideOpenStream(depart) && this.pointIsInsideStream(destination)) {
            var optCourant = this.streamAroundPoint(depart);
            if (optCourant.isPresent()) {
                Courant courant = optCourant.get();
                return courant.speedProvided(depart, destination);
            }

        }

        if (this.pointIsInsideOpenStream(destination) && this.pointIsInsideStream(depart)) {
            var optCourant = this.streamAroundPoint(destination);
            if (optCourant.isPresent()) {
                Courant courant = optCourant.get();
                return courant.speedProvided(depart, destination);
            }

        }

        if (this.pointIsInsideStream(depart) && !this.pointIsInsideOpenStream(depart)
                && !this.pointIsInsideStream(destination)) {
            return 0.0;
        }

        if (this.pointIsInsideStream(destination) && !this.pointIsInsideOpenStream(destination)
                && !this.pointIsInsideStream(depart)) {
            return 0.0;
        }
        if (this.pointIsInsideStream(depart) && !this.pointIsInsideOpenStream(depart)
                && this.pointIsInsideStream(destination) && !this.pointIsInsideOpenStream(destination)) {
            return 0.0;
        }

        return Double.POSITIVE_INFINITY;
    }

    // LATER tests for NEW code
    /**
     * 
     * @param depart
     * @param destination
     * @return
     */
    public double speedProvided(IPoint depart, IPoint destination) {

        IPoint insideIPoint;
        IPoint outsideIPoint;

        double resultLimitCase = this.speedProvidedLimits(depart, destination);
        if (resultLimitCase != Double.POSITIVE_INFINITY) {

            return resultLimitCase;
        }

        if (this.pointIsInsideStream(depart)) {

            insideIPoint = depart;
            outsideIPoint = destination;
        } else if (this.pointIsInsideStream(destination)) {

            insideIPoint = destination;
            outsideIPoint = depart;
        } else {
            return 0.0;
        }

        var optCourant = this.streamAroundPoint(insideIPoint);
        if (optCourant.isPresent()) {
            Courant courant = optCourant.get();
            return courant.speedProvided(depart, courant.limitToSurface(insideIPoint, outsideIPoint));
        }

        return 0.0;

    }

    /**
     * Lets define a trajectory as a list of points such that 1- the last point is
     * the destination 2- the first is the boat position 3- if we remove any point
     * in the middle the path will collide with an obstacle
     * 
     * in this method we assume that both the boat and the destination are not
     * inside a stream
     * 
     * @return
     */
    public List<IPoint> trajectoryToAvoidObstacles(IPoint depart, IPoint destination) {
        if (this.thereIsObstacleBetween(depart, destination)) {
            OceanEntity obstacleEntity = this.firstObstacleBetween(depart, destination);
            if (obstacleEntity.getEnumType().equals(OceanEntityType.COURANT)) {
                Courant courant = (Courant) obstacleEntity;
                if (!courant.isCompatibleWith(depart, destination)) {
                    return courant.avoidHit(depart, destination);
                }
            } else {// reef
                return obstacleEntity.avoidHit(depart, destination);
            }
        }

        return List.of(depart, destination);
    }

    /**
     * We assume boatPoint and checkPoint are in the same stream
     * 
     * @param boatPoint
     * @param checkPoint
     * @return potentially immutable list
     */
    public List<IPoint> trajectoryBoatAndCheckpointInsideStream(IPoint boatPoint, IPoint checkPoint) {

        // LATER est-ce qu'on peut avoir un récif à l'intérieur d'un courant ?
        return List.of(boatPoint, checkPoint);

    }

    /**
     * We assume the checkpoint is not in the same stream as departPoint
     * 
     * @param departPoint
     * @param destination
     * @return
     */
    public List<IPoint> trajectoryLeaveStreamAndReachPoint(IPoint departPoint, IPoint destination) {
        var optCourant = this.streamAroundPoint(departPoint);
        if (optCourant.isPresent()) {
            var courant = optCourant.get();
            if (!courant.isCompatibleWith(departPoint, destination)) {
                List<IPoint> points = new ArrayList<>();
                points.add(departPoint);
                var optPoint = courant.getAwayPoint(departPoint);
                if (optPoint.isPresent()) {
                    points.add(optPoint.get());
                    points.add(destination);
                    return points;
                }

            } else {

                return List.of(departPoint, destination);
            }
        }
        return List.of(departPoint, destination);// LATER
    }

    public Courant firstStreamBetween(IPoint destination) {
        LineSegment2D segment2d = new LineSegment2D(destination, boat);
        List<Courant> streamsOnTrajectory = this.courants.stream().filter(courant -> courant.intersectsWith(segment2d))
                .collect(Collectors.toList());

        if (streamsOnTrajectory.size() == 1) {

            return streamsOnTrajectory.get(0);
        } else if (streamsOnTrajectory.size() > 1) {

            var tmp = streamsOnTrajectory.stream()
                    .min((a, b) -> Double.compare(boat.distanceTo(a.getPosition()), boat.distanceTo(b.getPosition())));

            if (tmp.isPresent()) {
                return tmp.get();
            } else {
                // should never happen
                return null;
            }
        }
        return null;
    }

    public OceanEntity firstObstacleBetween(IPoint depart, IPoint destination) {
        LineSegment2D segment2d = new LineSegment2D(destination, depart);
        List<OceanEntity> obstaclesOnTrajectory = this.obstacles.stream()
                .filter(obstacle -> obstacle.intersectsWith(segment2d)).collect(Collectors.toList());

        if (obstaclesOnTrajectory.size() == 1) {

            return obstaclesOnTrajectory.get(0);
        } else if (obstaclesOnTrajectory.size() > 1) {

            var tmp = obstaclesOnTrajectory.stream().min(
                    (a, b) -> Double.compare(depart.distanceTo(a.getPosition()), depart.distanceTo(b.getPosition())));

            if (tmp.isPresent()) {
                return tmp.get();
            } else {
                // should never happen
                return null;
            }
        }
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String s = (String) evt.getNewValue();
        try {
            var entities = parser.fetchOceanEntities(s);
            this.obstacles = entities;
            this.courants = entities.stream().filter(e -> e.getEnumType().equals(OceanEntityType.COURANT))
                    .map(e -> (Courant) e).collect(Collectors.toList());
            this.recifs = entities.stream().filter(e -> e.getEnumType().equals(OceanEntityType.RECIF))
                    .map(e -> (Recif) e).collect(Collectors.toList());

            this.boatsAndReefs = entities.stream().filter(
                    e -> e.getEnumType().equals(OceanEntityType.RECIF) || e.getEnumType().equals(OceanEntityType.BOAT))
                    .collect(Collectors.toList());

        } catch (ParsingException e) {

            Logger.getInstance().logErrorMsg(e);
        }
    }

    /**
     * @param courants the streams to set
     */
    public void setCourants(List<Courant> courants) {
        this.courants = courants;
    }

    public void setObstacles(List<OceanEntity> oceanEntities) {
        this.obstacles = oceanEntities;
    }

    /**
     * @param recifs the recifs to set
     */
    public void setRecifs(List<Recif> recifs) {
        this.recifs = recifs;

    }

    /**
     * @param boatsAndReefs the boatsAndReefs to set
     */
    public void setBoatsAndReefs(List<OceanEntity> boatsAndReefs) {
        this.boatsAndReefs = boatsAndReefs;
    }

    public List<Recif> getRecifs() {
        return this.recifs;
    }

    public List<Courant> getStreams() {
        return this.courants;
    }

    public List<OceanEntity> getObstacles() {
        return this.obstacles;
    }

    public List<OceanEntity> getBoatsAndReefs() {
        return new ArrayList<>(this.boatsAndReefs);
    }

    public double boatSecurityMargin(){
        return this.boat.securityMargin();
    }

}