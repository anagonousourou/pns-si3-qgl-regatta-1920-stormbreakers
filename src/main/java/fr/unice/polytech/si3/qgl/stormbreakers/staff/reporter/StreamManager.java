package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

/**
 * Classe pour gérér les streams
 */
public class StreamManager implements PropertyChangeListener {

    private List<Courant> courants;
    private List<Recif> recifs;
    private List<OceanEntity> obstacles;
    private InputParser parser;
    private Boat boat;

    public StreamManager(InputParser parser, Boat boat) {
        this.parser = parser;
        this.boat = boat;

    }

    /**
     * Method to say if the boat is currently within a stream
     * 
     * @return
     */
    public boolean insideStream() {
        return this.courants.stream().anyMatch(courant -> courant.isPtInside(boat));
    }

    public boolean pointIsInsideStream(IPoint point) {
        return this.courants.stream().anyMatch(courant -> courant.isPtInside(point));
    }

    /**
     * 
     * @return
     */
    public Courant streamAroundBoat() {
        var optCourant = this.courants.stream().filter(courant -> courant.isPtInside(boat)).findAny();
        if (optCourant.isPresent()) {
            return optCourant.get();
        } else {
            return null;
        }
    }

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

    public boolean thereIsObstacleBetween(IPoint position) {
        LineSegment2D segment2d = new LineSegment2D(position, boat);
        return this.obstacles.stream().anyMatch(obstacle -> obstacle.intersectsWith(segment2d));
    }

    /**
     * Lets define a trajectory as a list of points such that 1- the last point is
     * the destination 2- the first is the boat position 3- if we remove any point
     * in the middle the path will collide with an obstacle
     * 
     * in this method we assume that both the boat and the destination are not
     * inside a stream
     * 
     * @param position
     * @return
     */
    public List<IPoint> trajectoryToAvoidObstacles(IPoint depart, IPoint destination) {
        return List.of(depart,destination);// TODO
    }

    /**
     * We assume that the destination is in a stream
     * 
     * @param destination
     * @return immutable list
     */
    public List<IPoint> trajectoryToReachAPointInsideStream(IPoint depart, IPoint destination) {
        var optCourant = this.streamAroundPoint(destination);
        if (optCourant.isPresent()) {
            var courant = optCourant.get();
            if (courant.isCompatibleWith(depart, destination) || courant.getStrength() <= 50.0) {
                // TODO split this condition in completelyCompatible and partiallyCompatible
                // Or simply implement a weighted graph
                return List.of(depart, destination);
            }

        }
        return List.of(depart, destination);
    }

    /**
     * We assume boatPoint and checkPoint are in the same stream
     * 
     * @param courant
     * @param boatPoint
     * @param checkPoint
     * @return potentially immutable list
     */
    public List<IPoint> trajectoryBoatAndCheckpointInsideStream(Courant courant, IPoint boatPoint, IPoint checkPoint) {
        if (courant.isCompatibleWith(boatPoint, checkPoint) || courant.getStrength() <= 50.0) {
            // TODO est-ce qu'on peut avoir un récif à l'intérieur d'un courant ?
            return List.of(boatPoint, checkPoint);
        } else {
            return this.trajectoryLeaveStreamAndReachPoint(boatPoint, checkPoint);
        }
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
                
                return List.of(departPoint,destination);
            }
        }
        return List.of(departPoint,destination);// TODO
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

    public Courant streamBringCloserCp(Checkpoint cp) {
        for (Courant courant : courants) {
            if (courant.bringCloserCp(cp, boat)) {
                return courant;
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
            this.courants = entities.stream().filter(e -> e.getType().equals("stream")).map(e -> (Courant) e)
                    .collect(Collectors.toList());
            this.recifs = entities.stream().filter(e -> e.getType().equals("reef")).map(e -> (Recif) e)
                    .collect(Collectors.toList());

        } catch (JsonProcessingException e) {
            Logger.getInstance().log(e.getMessage());
        }
    }

    /**
     * @param courants the streams to set
     */
    void setCourants(List<Courant> courants) {
        this.courants = courants;
    }

    IPoint calculateEscapePoint(Courant courant, IPoint position) {
        // LATER add strength consideration etc ...
        return courant.closestPointTo(position);
    }

}