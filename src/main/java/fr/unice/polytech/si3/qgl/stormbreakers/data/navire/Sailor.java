package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;
import fr.unice.polytech.si3.qgl.stormbreakers.math.MovementPath;

/**
 * classe qui représente un marin
 */
public class Sailor implements Logable {
    private final int id;
    private IntPosition position;
    private boolean onEquipment = false;
    private static final int MAX_MOVEMENT_DISTANCE = 5;
    private String name;

    /**
     * To Know wether a Marine is used or not alternative to List of busy Marines
     */
    private boolean doneTurn = false;

    // Potentiellement à enlever
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Sailor(int id, int x, int y) {
        this.id = id;
        this.position = new IntPosition(x, y);
    }

    public Sailor(int id, IntPosition pos) {
        this(id, pos.getX(), pos.getY());
    }

    public Sailor(int id, IntPosition pos, String name) {
        this(id, pos.getX(), pos.getY());
        this.name = name;

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * Deplace le marin selon les distances passees en parametre
     * 
     * @param xdistance distance selon l'axe X
     * @param ydistance distance selon l'axe Y
     */
    public void move(int xdistance, int ydistance) {
        if (Math.abs(xdistance) + Math.abs(ydistance) <= MAX_MOVEMENT_DISTANCE) {
            // Le deplacement respecte la contrainte de distance
            position.add(xdistance, ydistance);
        }
    }

    public void move(MoveAction mvt) {
        move(mvt.getXdistance(), mvt.getYdistance());
    }

    public boolean onEquipment() {
        return this.onEquipment;
    }

    public void setOnEquipment(boolean b) {
        this.onEquipment = b;
    }

    public int getId() {
        return id;
    }

    public IntPosition getPosition() {
        return position;
    }

    /**
     * To Know wether a Marine is used or not alternative to List of busy Marines
     */
    public boolean isDoneTurn() {
        return doneTurn;
    }

    /**
     * To set wether a Marine has been used or not alternative to List of busy
     * Marines
     */
    public void setDoneTurn(boolean doneTurn) {
        this.doneTurn = doneTurn;
    }

    public MoveAction howToMoveTo(IntPosition pos) {
        return new MoveAction(id, pos.getX() - this.position.getX(), pos.getY() - this.position.getY());
    }

    public int getDistanceTo(IntPosition pos) {
        return Math.abs(pos.getX() - this.position.getX()) + Math.abs(pos.getY() - this.position.getY());
    }

    public boolean canReach(IntPosition pos) {
        return getDistanceTo(pos) <= MAX_MOVEMENT_DISTANCE;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Sailor))
            return false;
        Sailor other = (Sailor) obj;
        return other.id == id && other.name.equals(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(id: " + id + ", \n" + "nom: " + name + ", \n" + "position:"
                + position + " ) ";
    }

    public String toLogs() {
        return "M(id:" + id + "position:" + position + ")";
    }

    /**
     * Retourne un MoveAction vers la position indiquée
     * priviliégie le déplacement selon X
     * @param position vers laquelle se diriger
     * @return MoveAction limité par le déplacement maximal authorisé
     */
    public MoveAction howToGetCloserTo(IntPosition position) {
        int distanceShortOf = getDistanceTo(position) - MAX_MOVEMENT_DISTANCE;
        if (distanceShortOf<=0) return howToMoveTo(position);
        else {
            MovementPath path = this.position.getPathTo(position);
            int toLowerXBy = 0;
            int toLowerYBy;
            // lower deltaY enough
            toLowerYBy = Math.min(distanceShortOf,path.getDeltaY());
            distanceShortOf -= toLowerYBy;
            // If still too far lower deltaX enough
            if (distanceShortOf>0) {
                toLowerXBy = Math.min(distanceShortOf,path.getDeltaX());
            }
            return new MoveAction(id, path.getDeltaX()-toLowerXBy, path.getDeltaY()-toLowerYBy);
        }
    }
}