package fr.unice.polytech.si3.qgl.stormbreakers.refactor2;

import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.Move;

public class IntPosition {
    private int x,y;


    IntPosition(int x, int y){
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


}