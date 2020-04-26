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

    private final List<Sailor> sailors;

    public CrewManager(List<Sailor> sailors) {
        this.sailors = sailors;
    }

    public Optional<Sailor> getSailorById(int id) {
        return sailors.stream().filter(m -> m.getId() == id).findFirst();
    }

    public void addListener(PropertyChangeListener propertyChangeListener) {
        this.sailors.forEach(marin -> marin.addPropertyChangeListener(propertyChangeListener));
    }

    public void executeMoves(List<MoveAction> moves) {
        for (MoveAction m : moves) {
            var optMarin = this.getSailorById(m.getSailorId());
            optMarin.ifPresent(sailor -> sailor.move(m));
        }
    }

    /**
     * Fait executer aux marin concernés les MoveAction contenus dans les actions
     * 
     * @param actions liste des actions contenant des MoveAction
     */
    public void executeMovingsInSailorAction(List<SailorAction> actions) {
        // On récupère les déplacements
        List<MoveAction> moves = actions.stream().filter(act -> act.getType().equals(ActionType.MOVING.actionCode))
                .map(x -> (MoveAction) x).collect(Collectors.toList());

        for (MoveAction move : moves) {
            Optional<Sailor> sailorOpt = getSailorById(move.getSailorId());
            sailorOpt.ifPresent(sailor -> sailor.move(move));

        }

    }

    @Override
    public String toString() {
        return sailors.toString();
    }

    public boolean sailorAround(IntPosition position) {
        return this.sailors.stream().anyMatch(m -> m.canReach(position));
    }

    public Optional<Sailor> sailorAtPosition(IntPosition position) {
        return sailors.stream().filter(m -> m.getPosition().distanceTo(position) == 0).findFirst();
    }

    public Optional<Sailor> availableSailorAtPosition(IntPosition position) {
        return getAvailableSailors().stream().filter(m -> m.getPosition().distanceTo(position) == 0).findFirst();
    }

    /**
     *
     * @param position
     * @return a optional encapsulating the closest Marine to position
     */
    public Optional<Sailor> sailorClosestTo(IntPosition position) {
        return sailorClosestTo(position, sailors);
    }

    public Optional<Sailor> availableSailorClosestTo(IntPosition position) {
        return sailorClosestTo(position, getAvailableSailors());
    }

    /**
     * Gets the sailor which is closest to the given position
     * 
     * @param position target
     * @param sailors  list of sailors
     * @return Optional which contains the closest sailors if there is any
     */
    public Optional<Sailor> sailorClosestTo(IntPosition position, List<Sailor> sailors) {
        return sailors.stream().min(Comparator.comparingInt(a -> a.getDistanceTo(position)));
    }

    public void resetAvailability() {
        this.sailors.forEach(m -> m.setDoneTurn(false));
    }

    public List<Sailor> sailors() {
        return sailors;
    }

    public List<Sailor> getAvailableSailorsIn(List<Sailor> sailors) {
        return sailors.stream().filter(s -> !s.isDoneTurn()).collect(Collectors.toList());
    }

    public List<Sailor> getAvailableSailors() {
        return getAvailableSailorsIn(sailors);
    }

    public List<Sailor> getSailorsWhoCanReach(List<Sailor> sailors, IntPosition position) {
        return sailors.stream().filter(s -> s.canReach(position)).collect(Collectors.toList());
    }

    /**
     * Rapproche le marin le plus proche d'une position donnée, de celle-ci NB:
     * modifie la liste de marins (retire celui déplacé s'il existe)
     * 
     * @param sailors  Liste de marins
     * @param position à approcher
     * @return déplacement généré, null si aucun
     */
    public MoveAction bringClosestSailorCloserTo(List<Sailor> sailors, IntPosition position) {
        // LATER: 27/02/2020 Verify Tests
        MoveAction move = null;
        Optional<Sailor> closestSailorOpt = sailorClosestTo(position, sailors);

        if (closestSailorOpt.isPresent()) {
            Sailor closestSailor = closestSailorOpt.get();
            move = closestSailor.howToGetCloserTo(position);
            sailors.remove(closestSailor); // Already assigned
        }

        return move;
    }
}