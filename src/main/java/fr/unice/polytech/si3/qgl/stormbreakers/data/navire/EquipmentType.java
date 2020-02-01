package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

public enum EquipmentType {
    OAR("oar"),
    SAIL("sail"),
    RUDDER("rudder"),
    WATCH("watch");

    public final String code;
    EquipmentType(String code) {
        this.code = code;
    }
}
