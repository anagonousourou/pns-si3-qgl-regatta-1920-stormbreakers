package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Marin
 */
class Marine {
    private final int id;
    private IntPosition position;

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


    public void requestMove(Move mvt){
        this.pcs.firePropertyChange("moving", null, mvt);

    }

    public void executeMove(Move mvt){
        // TODO completer
        this.position.makeMove(mvt);
    }

    public boolean onEquipment(){
        // TODO completer
        return false;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Marine(id: "+id+", \n"+"position:"+position+" ) ";
    }

    
}