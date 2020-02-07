package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

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

    boolean longerThan(int distance){
        return Math.abs(this.xdistance)+Math.abs(this.ydistance) > distance;
    }


}
