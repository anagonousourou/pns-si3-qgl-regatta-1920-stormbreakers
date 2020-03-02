package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;

public class CrewManager {

    private final List<Sailor> marins;

    public CrewManager(List<Sailor> marins) {
        this.marins = marins;
    }

    public Optional<Sailor> getMarinById(int id) {
        return marins.stream().filter(m -> m.getId() == id).findFirst();
    }

    public void addListener(PropertyChangeListener propertyChangeListener) {
        this.marins.forEach(marin -> marin.addPropertyChangeListener(propertyChangeListener));
    }

    
    
    public void executeMoves(List<MoveAction> moves){
        for(MoveAction m:moves){
            var optMarin=this.getMarinById(m.getSailorId());
            if(optMarin.isPresent()){
                optMarin.get().move(m);
            }
        }
    }
    /**
     * Fait executer aux marin concernés les MoveAction contenus dans les actions
     * @param actions liste des actions contenant des MoveAction
     */
    public void executeMovingsInSailorAction(List<SailorAction> actions) {
        // On récupère les déplacements
        List<MoveAction> moves = actions.stream()
                .filter(act -> act.getType().equals(ActionType.MOVING.actionCode))
                .map(x -> (MoveAction) x)
                .collect(Collectors.toList());

        for (MoveAction move : moves) {
            Optional<Sailor> sailor = getMarinById(move.getSailorId());
            if(sailor.isPresent()){
                sailor.get().move(move );
            }
            
        }
        
    }
    

    

    @Override
    public String toString() {
        return marins.toString();
    }

    public boolean marinAround(IntPosition position){
        return this.marins.stream().anyMatch(m->m.canReach(position));
    }

    public Optional<Sailor> marineAtPosition(IntPosition position){
        return marins.stream().filter(m -> m.getPosition().distanceTo(position) == 0).findFirst();
    }


    /**
     *
     * @param position
     * @return a optional encapsulating the closest Marine to position
     */
    public Optional<Sailor> marineClosestTo(IntPosition position){
        return marineClosestTo(position, marins);
    }

    public Optional<Sailor> marineClosestTo(IntPosition position, List<Sailor> sailors){
        return sailors.stream().min(
                Comparator.comparingInt(a -> a.getDistanceTo(position)));
    }

    public void resetAvailability(){
        this.marins.forEach(m->m.setDoneTurn(false));
    }

	public List<Sailor> marins() {
		return marins;
	}

	public List<Sailor> getAvailableSailorsIn(List<Sailor> sailors) {
        return sailors.stream().filter(s -> !s.isDoneTurn()).collect(Collectors.toList());
    }

    public List<Sailor> getAvailableSailors() {
        return getAvailableSailorsIn(marins);
    }


    public List<Sailor> getSailorsWhoCanReach(List<Sailor> sailors, IntPosition position) {
        return sailors.stream().filter(s -> s.canReach(position)).collect(Collectors.toList());
    }
}