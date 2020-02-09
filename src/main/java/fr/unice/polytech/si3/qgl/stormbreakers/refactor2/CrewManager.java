package fr.unice.polytech.si3.qgl.stormbreakers.refactor2;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.Move;
import fr.unice.polytech.si3.qgl.stormbreakers.simulation.IntPosition;

public class CrewManager {

    private List<Marin> marins;

    public CrewManager(List<Marin> marins) {
        this.marins = marins;
    }

    Optional<Marin> getMarinById(int id) {
        return marins.stream().filter(m -> m.getId() == id).findFirst();
    }


    /**
     * fait executer aux marin concernés les MoveAction contenus dans le param moves
     *
     * @param moves
     */
    public void executeMovingsInSailorAction(List<SailorAction> moves) {
        for (SailorAction m : actions) {
            // TODO complete
        }
    }

    /**
     * @param position position a comparer
     * @return le marin le plus proche de la position
     */
    public Marin getClosestToPoint(IntPosition position) {

    }

    public Move howToMoveTo(fr.unice.polytech.si3.qgl.stormbreakers.refactor2.IntPosition otherPosition){
        if(otherPosition!=null){
            return new Move(otherPosition.x-this.x, otherPosition.y-this.y);
        }
        return null;
    }

    public void makeMove(Move mvt){
        this.x+=mvt.getXdistance();
        this.y+=mvt.getYdistance();
    }

    @Override
    public String toString() {
        return marins.toString();
    }

}