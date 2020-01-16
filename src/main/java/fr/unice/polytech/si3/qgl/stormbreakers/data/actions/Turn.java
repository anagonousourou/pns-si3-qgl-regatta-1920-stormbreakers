package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

public class Turn extends ActionType {
    double rotation;

    Turn(int sailorId) {
        super(sailorId,"TURN");
    }

}
