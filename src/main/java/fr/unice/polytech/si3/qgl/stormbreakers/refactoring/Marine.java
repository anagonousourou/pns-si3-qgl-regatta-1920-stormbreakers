package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;

/**
 * classe qui représente un marin
 */
class Marine {
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

    public void executeMove(MoveAction mvt) {
        // TODO completer
        this.position.makeMove(new Move(mvt.getXdistance(), mvt.getYdistance()));
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

    public MoveAction howToGoTo(IntPosition pos) {
        return howToGoTo(pos.getX(),pos.getY());
    }

	public MoveAction howToGoTo(int x, int y) {
		return new MoveAction(this.id, x-this.getPosition().getX(),y-this.getPosition().getY() );
	}

	public int getDistanceTo(IntPosition pos) {
        return Math.abs(pos.getX()-this.getPosition().getX()) + Math.abs(pos.getY()-this.getPosition().getY());
    }

    public boolean canReach(IntPosition pos) {
        return getDistanceTo(pos) <= MAX_MOVEMENT_DISTANCE;
    }
}