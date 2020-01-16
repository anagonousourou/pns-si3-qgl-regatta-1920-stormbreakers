package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

public abstract class ActionType {
    private int sailorId;
    private String type;

    ActionType(int sailorId, String type) {
        this.sailorId = sailorId;
        this.type = type;
    }

}
