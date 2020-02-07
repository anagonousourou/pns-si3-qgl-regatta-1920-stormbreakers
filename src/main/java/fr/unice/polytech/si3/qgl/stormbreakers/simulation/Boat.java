package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Boat implements PropertyChangeListener {
    Crew crew;
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("moving")){
            if( !((Move)evt.getNewValue()).longerThan(5) ){
                ((Marine) evt.getSource()).executeMove((Move) evt.getNewValue() );
            }
           
        }
        // TODO Auto-generated method stub

    }

    void setCrew(Crew c){
        this.crew=c;
        this.crew.toList().forEach(marin->
            marin.addPropertyChangeListener(this)
        );
    }

    
    
}