package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.ParsingException;
import fr.unice.polytech.si3.qgl.stormbreakers.io.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Surface;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;

/**
 * Le bateau implémente Propertychange listener pour permettre la mise à jour
 * des infos du bateau notamment la vie et la position-orientation
 */
public class Boat implements PropertyChangeListener, Surface {
    private Shape boatShape;
    private Position position;
    private final int deckwidth;
    private final int decklength;
    private int life;
    private InputParser parser;

    public Boat(Position position, int decklength, int deckwidth, int life, InputParser parser, Shape boatShape) {
        this.position = position;
        this.decklength = decklength;
        this.deckwidth = deckwidth;
        this.life = life;
        this.parser = parser;
        this.boatShape = boatShape;
        boatShape.setAnchor(position);
    }

    /**
     * Constructor for test compatibility (No shape)
     */
    public Boat(Position position, int decklength, int deckwidth, int life, InputParser parser) {
        this.position = position;
        this.decklength = decklength;
        this.deckwidth = deckwidth;
        this.life = life;
        this.parser = parser;
    }

    public Shape getShape() {
        return boatShape;
    }

    public void setPosition(Position position) {
        this.position = position;
        if (this.boatShape != null) {
            this.boatShape.setAnchor(position);
        }

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
            setPosition(this.parser.fetchBoatPosition(data));
        } catch (ParsingException e) {
            Logger.getInstance().logErrorMsg(e);
        }

    }

    public Position getPosition() {
        return position;
    }

    @Override
    public double x() {
        return this.position.x();
    }

    @Override
    public double y() {
        return this.position.y();
    }

    public double securityMargin(){
        return (Math.max(decklength,deckwidth) / (double)2) + 1.0;
    }

}