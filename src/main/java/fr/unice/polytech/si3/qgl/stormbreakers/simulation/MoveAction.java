package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

/**
 * Classe pour représenter l'action de se déplacer pour un  sur le pont du bateau
 * 
 */
public class MoveAction {

    private final int xdistance;
    private final int ydistance;
    private final int id;
    
    MoveAction(int id,int xd,int yd){
        this.id=id;
        this.xdistance=xd;
        this.ydistance=yd;
    }

    public int getXdistance() {
        return xdistance;
    }

    public int getYdistance() {
        return ydistance;
    }

    boolean longerThan(int distance){
        return Math.abs(this.xdistance)+Math.abs(this.ydistance) > distance;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MoveAction(id: "+id+", "+"xd: "+xdistance+", "+"yd: "+ydistance+")";
    }


}
