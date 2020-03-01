package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Navigator;

/**
 * Classe pour gérér les streams
 */
public class StreamManager implements PropertyChangeListener {

    List<Courant> courants;
    InputParser parser;
    Boat boat;
    Navigator navigator;
    private static final double EPS = 0.001;

    public StreamManager(InputParser parser,Boat boat,Navigator navigator) {
        this.parser = parser;
        this.boat=boat;
        this.navigator=navigator;

    }
    /**
     * Method to say if the boat is currently within a stream
     * @return
     */
    public boolean insideStream(){
        return this.courants.stream().anyMatch(courant->courant.getShape().isPtInside(boat.getPosition().getPoint2D()));
    }

    public Courant streamAroundBoat(){
        return this.courants.stream().filter(courant->courant.getShape().isPtInside(boat.getPosition().getPoint2D())).findAny().get();
    }

    /**
     * Méthode to say if there is any streams from the boat 
     * to the given position
     * @param position
     * @return
     */

    public boolean thereIsStreamBetween(Position position){
        LineSegment2D segment2d=new LineSegment2D(position.getPoint2D(), boat.getPosition().getPoint2D());
        return this.courants.stream().anyMatch(courant->courant.intersectsWith( segment2d ));
        
        
    }
    
    
   public Courant streamBringCloserCp(Checkpoint cp) {
	   for(Courant courant:courants) {
		   if(courant.bringCloserCp(cp,boat)) {
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
    
}