package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.EquipmentType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;

class Boat implements PropertyChangeListener {
    private Crew crew;
    private static final int MAX_DISTANCE = 5;
    private Position position = null;
    private EquipmentManager equipmentManager;

    Boat(Crew crew, EquipmentManager equipmentManager) {
        this.equipmentManager = equipmentManager;
        this.setCrew(crew);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("moving")) {
            if (!((MoveAction) evt.getNewValue()).longerThan(MAX_DISTANCE)) {
                Marine marine = (Marine) evt.getSource();
                marine.executeMove((MoveAction) evt.getNewValue());
                String typeEquipment=equipmentManager.typeOfEquipmentPresentAt(marine.getPosition() );
                if(typeEquipment.length()!=0){
                    marine.setOnEquipment(true);
                    marine.setTypeOfEquipment( equipmentManager.typeOfEquipmentPresentAt(marine.getPosition() )  );
                }
                else{
                    marine.setOnEquipment(false);

                }
            }

        }

        else if (evt.getPropertyName().equals("Action")) {
            Marine marine = (Marine) evt.getSource();
            if (marine.onEquipment()) {
                var optEq = this.equipmentManager.equipmentAt(marine.getPosition());
                SailorAction action=(SailorAction) evt.getNewValue();
                if (optEq.isPresent() && optEq.get().getType().equals(marine.getTypeOfEquipment()) &&  action.compatibleEquipmentType().equals(optEq.get().getType()) ) {
                    optEq.get().setUsed(true);
                    if (optEq.get().getType().equals(EquipmentType.RUDDER.code)) {

                        ((Gouvernail) optEq.get()).setOrientation(((Turn) evt.getNewValue()).getRotation());
                    }
                    else if(optEq.get().getType().equals(EquipmentType.SAIL.code)){
                        Sail sail=(Sail) optEq.get();
                        if(action.getType().equals("LIFT_SAIL")){
                            sail.setOpenned(true);
                        }
                        else if(action.getType().equals("LOWER_SAIL")){
                            sail.setOpenned(false);
                        }
                    }
                }
            }

        }
    }
    @Deprecated
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