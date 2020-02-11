package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.EquipmentType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;

class Boat implements PropertyChangeListener {
    private Crew crew;
    private static final int MAX_DISTANCE = 5;
    private Position position = null;
    private EquipmentManager equipmentManager;

    Boat(Crew crew, EquipmentManager equipmentManager) {
        this.equipmentManager = equipmentManager;
        this.crew = crew;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("moving")) {
            if (!((MoveAction) evt.getNewValue()).longerThan(MAX_DISTANCE)) {
                Marine marine = (Marine) evt.getSource();
                marine.executeMove((MoveAction) evt.getNewValue());
                if (equipmentManager.oarPresentAt(marine.getPosition())) {
                    marine.setOnEquipment(true);
                    marine.setTypeOfEquipment(EquipmentType.OAR.code);
                } else if (equipmentManager.rudderPresentAt(marine.getPosition())) {
                    marine.setOnEquipment(true);
                    marine.setTypeOfEquipment(EquipmentType.RUDDER.code);
                } else {
                    marine.setOnEquipment(false);
                }
            }

        }

        else if (evt.getPropertyName().equals("Action")) {
            Marine marine = (Marine) evt.getSource();
            if (marine.onEquipment()) {
                var optOar = this.equipmentManager.equipmentAt(marine.getPosition());
                if (optOar.isPresent() && optOar.get().getType().equals(marine.getTypeOfEquipment())) {
                    optOar.get().setUsed(true);
                    if (optOar.get().getType().equals(EquipmentType.RUDDER.code)) {

                        ((Gouvernail) optOar.get()).setOrientation(((Turn) evt.getNewValue()).getRotation());
                    }
                }
            }

        }
    }

    void setCrew(Crew c) {
        this.crew = c;
        this.crew.addListener(this);
    }

    void setEquipmentManager(EquipmentManager eManager) {
        this.equipmentManager = eManager;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}