package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

/**
 * Classe pour gérér les streams
 */
public class StreamManager implements PropertyChangeListener {

    private List<Courant> courants;
    private InputParser parser;
    private Boat boat;

    private static final double EPS = 0.001;

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
        return this.courants.stream().anyMatch(courant -> courant.isPtInside(boat.getPosition()));
    }

    /**
     * 
     * @return
     */
    public Courant streamAroundBoat() {
        var optCourant = this.courants.stream().filter(courant -> courant.isPtInside(boat.getPosition()))
                .findAny();
        if (optCourant.isPresent()) {
            return optCourant.get();
        } else {
            return null;
        }
    }

    /**
     * Méthode to say if there is any streams from the boat to the given position
     * 
     * @param position
     * @return
     */
    public boolean thereIsStreamBetween(Position position) {
        LineSegment2D segment2d = new LineSegment2D(position, boat.getPosition());
        return this.courants.stream().anyMatch(courant -> courant.intersectsWith(segment2d));

    }

    public Courant firstStreamBetween(Position destination){
        LineSegment2D segment2d = new LineSegment2D(destination, boat.getPosition());
        List<Courant> streamsOnTrajectory=this.courants.stream().filter(courant->courant.intersectsWith(segment2d))
        .collect(Collectors.toList());

        if(streamsOnTrajectory.size()==1){
            return streamsOnTrajectory.get(0);
        }
        else if(streamsOnTrajectory.size()>1){
            //TODO 
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
            this.courants = parser.fetchStreams(s);
        } catch (JsonProcessingException e) {
            Logger.getInstance().log(e.getMessage());
        }
    }

    /**
     * @param courants the courants to set
     */
    void setCourants(List<Courant> courants) {
        this.courants = courants;
    }

}