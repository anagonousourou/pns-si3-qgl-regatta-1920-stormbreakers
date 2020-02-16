package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
/**
 * Le bateau implémente Propertychange listener pour permettre 
 * la mise à jour des infos du bateau notamment la vie et la position-orientation
 */
public class Boat implements PropertyChangeListener {
    private Position position = null;
    private final int deckwidth;
    private final int decklength;
    private int life = 0;
    //add a Shape field
    private InputParser parser;

    public Boat(Position position, int decklength, int deckwidth, int life, InputParser parser) {
        this.position = position;
        this.decklength = decklength;
        this.deckwidth = deckwidth;
        this.life = life;
        this.parser = parser;
    }

    

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getDeckwidth() {
        return deckwidth;
    }

    
    public int getDecklength() {
        return decklength;
    }
    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public double getOrientation() {
        return this.position.getOrientation();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String data = (String) evt.getNewValue();
        try {
            this.life = this.parser.fetchBoatLife(data);
            this.position =this.parser.fetchBoatPosition(data);
        } catch (JsonProcessingException e) {
            Logger.getInstance().log(e.getMessage());
        }
        

    }

}