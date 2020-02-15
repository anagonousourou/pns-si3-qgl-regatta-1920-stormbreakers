package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

public class IntPosition {
    private int x,y;


    public IntPosition(int x,int y){
        this.x=x;
        this.y=y;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void add(int deltaX, int deltaY){
        this.x+=deltaX;
        this.y+=deltaY;
    }

    public MovementPath getPathTo(IntPosition target){
        return new MovementPath(this,target);
    }

    @Override
    public String toString() {
        return "Position (x: "+x+", "+"y: "+y+")";
    }

    public int distanceTo(IntPosition position){
        return Math.abs(this.x-position.x)+Math.abs(this.y-position.y);
    }


}