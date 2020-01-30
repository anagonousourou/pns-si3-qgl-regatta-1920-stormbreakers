package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;


public enum ActionType {
    MOVING("MOVING"),
    LIFTSAIL("LIFT_SAIL"),
    LOWERSAIL("LOWER_SAIL"),
    TURN("TURN"),
    OAR("OAR"),
    USEWATCH("USE_WATCH");

    public final String actionCode;
    ActionType(String actionCode) {
        this.actionCode = actionCode;
    }
}
