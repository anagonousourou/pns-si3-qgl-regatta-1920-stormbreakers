package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;

/**
 * classe qui représente un marin
 */
public class Marine {
    private final int id;
    private IntPosition position;
    private boolean onEquipment = false;
    private static final int MAX_MOVEMENT_DISTANCE = 5;

    /**
     * To Know wether a Marine is used or not
     * alternative to List of busy Marines
     */
    private boolean doneTurn = false;

    //Potentiellement à enlever
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    Marine(int id, int x, int y) {
        this.id = id;
        this.position = new IntPosition(x, y);
    }

    Marine(int id, IntPosition pos) {
        this(id,pos.getX(),pos.getY());
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * Deplace le marin selon les distances passees en parametre
     * @param xdistance distance selon l'axe X
     * @param ydistance distance selon l'axe Y
     */
    public void move(int xdistance, int ydistance) {
        if (Math.abs(xdistance) + Math.abs(ydistance) <= MAX_MOVEMENT_DISTANCE) {
            // Le deplacement respecte la contraite de distance
            position.add(xdistance,ydistance);
        }
    }

    public void move(MoveAction mvt){
        move(mvt.getXdistance(),mvt.getYdistance());
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

    @Override
    public String toString() {
        return "Marine(id: " + id + ", \n" + "position:" + position + " ) ";
    }

    public IntPosition getPosition() {
        return position;
    }

    public void setPosition(IntPosition position) {
        this.position = position;
    }
    /**
     * To Know wether a Marine is used or not
     * alternative to List of busy Marines
     */
    public boolean isDoneTurn() {
        return doneTurn;
    }
    /**
     * To set wether a Marine has been used or not
     * alternative to List of busy Marines
     */
    public void setDoneTurn(boolean doneTurn) {
        this.doneTurn = doneTurn;
    }

    public MoveAction howToMoveTo(IntPosition pos) {
        return new MoveAction(id, position.getPathTo(pos));
    }

	public int getDistanceTo(IntPosition pos) {
        return Math.abs(pos.getX()-this.getPosition().getX()) + Math.abs(pos.getY()-this.getPosition().getY());
    }

    public boolean canReach(IntPosition pos) {
        return getDistanceTo(pos) <= MAX_MOVEMENT_DISTANCE;
    }

}