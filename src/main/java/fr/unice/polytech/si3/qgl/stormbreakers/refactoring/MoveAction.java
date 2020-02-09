package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

/**
 * Classe pour représenter l'action de se déplacer pour un  sur le pont du bateau
 * 
 */
@Deprecated
public class MoveAction {

    private final int xdistance,ydistance,id;
    
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

    public boolean longerThan(int distance){
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
