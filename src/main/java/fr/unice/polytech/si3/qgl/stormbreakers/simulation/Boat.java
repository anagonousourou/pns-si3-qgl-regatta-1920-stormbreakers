package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Boat implements PropertyChangeListener {
    Crew crew;
    private final int MAX_DISTANCE = 5;
    private EquimentManager equipmentManager;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("moving")) {
            if (!((MoveAction) evt.getNewValue()).longerThan(MAX_DISTANCE)) {
                Marine marine=(Marine) evt.getSource();
                marine.executeMove((MoveAction) evt.getNewValue());
                if(equipmentManager.oarPresentAt(marine.getPosition())){
                    marine.setOnEquipment(true);
                    //later typeof equipment
                }
                else{
                    marine.setOnEquipment(false);
                }
            }

        }

        else if(evt.getPropertyName().equals("OarAction")){
            Marine marine=(Marine) evt.getSource();
            if(marine.onEquipment()){
                var optOar=this.equipmentManager.oarAt(marine.getPosition());
                if(optOar.isPresent()){
                    optOar.get().setUsed(true);
                }
            }

        }
    }

    void setCrew(Crew c) {
        this.crew = c;
        this.crew.toList().forEach(marin -> marin.addPropertyChangeListener(this));
    }

    void setEquipmentManager(EquimentManager eManager){
        this.equipmentManager=eManager;
    }

}