package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

public enum EquipmentType {
    OAR("oar","o"),
    SAIL("sail","s"),
    RUDDER("rudder","r"),
    WATCH("watch","w");

    public final String code;
    public final String shortCode;
    EquipmentType(String code, String shortCode) {
        this.code = code;
        this.shortCode = shortCode;
    }
}
