package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Marin
 */
class Marine {
    private final int id;
    private IntPosition position;
    private boolean onEquipment=false;


    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    Marine(int id,int x,int y){
        this.id=id;
        this.position=new IntPosition(x, y);
    }

     public void addPropertyChangeListener(PropertyChangeListener listener) {
         this.pcs.addPropertyChangeListener(listener);
     }

     public void removePropertyChangeListener(PropertyChangeListener listener) {
         this.pcs.removePropertyChangeListener(listener);
     }


    public void requestMove(MoveAction mvt){
        this.pcs.firePropertyChange("moving", null, mvt);

    }

    public void executeMove(MoveAction mvt){
        // TODO completer
        this.position.makeMove(new Move(mvt.getXdistance(), mvt.getYdistance()));
    }

    public boolean onEquipment(){
        // TODO completer
        return this.onEquipment;
    }

    public void setOnEquipment(boolean b){
        this.onEquipment=b;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Marine(id: "+id+", \n"+"position:"+position+" ) ";
    }

    public IntPosition getPosition() {
        return position;
    }

    public void setPosition(IntPosition position) {
        this.position = position;
    }

	public void requestOarAction(OarAction oa) {
        this.pcs.firePropertyChange("OarAction", null, oa);
	}

    
}