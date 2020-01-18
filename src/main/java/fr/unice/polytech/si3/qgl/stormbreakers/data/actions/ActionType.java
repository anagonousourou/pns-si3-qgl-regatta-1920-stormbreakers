package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;


public enum ActionType {
    moving("MOVING"),
    liftSail("LIFT_SAIL"),
    lowerSail("LOWER_SAIL"),
    turn("TURN"),
    oar("OAR"),
    useWatch("USE_WATCH");

    public final String actionCode;
    ActionType(String actionCode) {
        this.actionCode = actionCode;
    }
}
