package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableData {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private String value;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String newValue) {
        String oldValue = this.value;
        this.value = newValue;
        this.pcs.firePropertyChange("value", oldValue, newValue);
    }

}