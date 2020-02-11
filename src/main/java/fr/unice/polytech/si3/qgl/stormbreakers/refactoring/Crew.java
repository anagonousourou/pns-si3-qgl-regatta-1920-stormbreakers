package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;

public class Crew {

    private final List<Marine> marins;

    public Crew(List<Marine> marins) {
        this.marins = marins;
    }

    Optional<Marine> getMarinById(int id) {
        return marins.stream().filter(m -> m.getId() == id).findFirst();
    }

    public void addListener(PropertyChangeListener propertyChangeListener) {
        this.marins.forEach(marin -> marin.addPropertyChangeListener(propertyChangeListener));
    }

    /**
     * fait executer aux marin concernés les MoveAction contenus dans le param moves
     * 
     * @param moves
     */
    public void executeMovingsInSailorAction(List<SailorAction> actions) {
        
    }

    

    @Override
    public String toString() {
        return marins.toString();
    }

    public boolean marinAround(IntPosition position){
        return this.marins.stream().filter(m->m.getPosition().distanceTo(position)<=5 ).findAny().isPresent();
    }

    public Optional<Marine> marineAtPosition(IntPosition position){
        return marins.stream().filter(m -> m.getPosition().distanceTo(position) == 0).findFirst();
    }
    /**
     * 
     * @param position
     * @return a optional encapsulating the closest Marine to position
     */
    public Optional<Marine> marineClosestTo(IntPosition position){
        return marins.stream().min(
            (a,b)-> Integer.compare(position.distanceTo(a.getPosition()),position.distanceTo(b.getPosition()) ) );
    }

    void resetAvailability(){
        this.marins.forEach(m->m.setDoneTurn(false));
    }


}