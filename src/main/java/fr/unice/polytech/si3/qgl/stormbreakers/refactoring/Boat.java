package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

public class Boat implements PropertyChangeListener {
    private final int MAX_DISTANCE = 5;
    private Position position = null;
    private int deckwidth = 0;
    private int decklength = 0;
    private int life = 0;
    // TODO add a Shape field
    private InputParser parser;

    public Boat(Position position, int decklength, int deckwidth, int life, InputParser parser) {
        this.position = position;
        this.decklength = decklength;
        this.deckwidth = deckwidth;
        this.life = life;
        this.parser = parser;
    }

    public Boat(InputParser parser){
        this.parser=parser;

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

    public void setDeckwidth(int deckwidth) {
        this.deckwidth = deckwidth;
    }

    public int getDecklength() {
        return decklength;
    }

    public void setDecklength(int decklength) {
        this.decklength = decklength;
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
            this.decklength =this.parser.fetchBoatLength(data);
            this.deckwidth = this.parser.fetchBoatWidth(data);
            this.position =this.parser.fetchBoatPosition(data);
        } catch (JsonProcessingException e) {
            //something
        }
        

    }

}