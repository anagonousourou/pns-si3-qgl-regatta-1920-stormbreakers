package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;

/**
 * Classe pour gérér les streams
 */
public class StreamManager implements PropertyChangeListener {

    List<Courant> courants;
    InputParser parser;

    public StreamManager(InputParser parser) {
        this.parser = parser;
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