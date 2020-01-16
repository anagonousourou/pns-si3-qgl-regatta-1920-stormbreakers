package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

public class Moving extends ActionType {
    int xdistance;
    int ydistance;

    Moving(int sailorId) {
        super(sailorId,"MOVING");
    }

}
