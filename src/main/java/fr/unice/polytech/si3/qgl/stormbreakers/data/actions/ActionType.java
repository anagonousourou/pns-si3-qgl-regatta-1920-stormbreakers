package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;


public enum ActionType {
    MOVING("MOVING","M"),
    LIFTSAIL("LIFT_SAIL","CS"),
    LOWERSAIL("LOWER_SAIL","OS"),
    TURN("TURN","T"),
    OAR("OAR","O"),
    USEWATCH("USE_WATCH","W");

    public final String actionCode;
    public final String shortCode;
    ActionType(String actionCode, String shortCode) {
        this.actionCode = actionCode;
        this.shortCode = shortCode;
    }
}
