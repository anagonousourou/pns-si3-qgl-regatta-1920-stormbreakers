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

    public Move howToMoveTo(IntPosition otherPosition){
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
        return "Position (x: "+x+", "+"y: "+y+")";
    }

    public int distanceTo(IntPosition position){
        return Math.abs(this.x-position.x)+Math.abs(this.y-position.y);
    }


}