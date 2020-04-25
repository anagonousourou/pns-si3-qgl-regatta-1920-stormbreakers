package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.ParsingException;
import fr.unice.polytech.si3.qgl.stormbreakers.io.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logger;

public class Wind implements PropertyChangeListener {
    private double orientation = 0.0;
    private double strength = 0.0;
    private InputParser parser;

    @JsonCreator
    public Wind(@JsonProperty("orientation") double orientation, @JsonProperty("strength") double strength) {
        this.orientation = orientation;
        this.strength = strength;
    }

    public Wind(InputParser parser) {
        this.parser = parser;
    }

    @JsonProperty("orientation")
    public double getOrientation() {
        return orientation;
    }

    @JsonProperty("strength")
    public double getStrength() {
        return strength;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String jString = (String) evt.getNewValue();
        try {
            this.orientation = this.parser.fetchWindOrientation(jString);
            this.strength = this.parser.fetchWindStrength(jString);
        } catch (ParsingException e) {
            Logger.getInstance().logErrorMsg(e);
        }

    }
}
