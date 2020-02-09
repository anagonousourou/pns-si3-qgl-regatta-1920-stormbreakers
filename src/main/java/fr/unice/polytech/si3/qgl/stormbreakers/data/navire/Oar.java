package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Oar extends Equipment {

    @JsonCreator
    public Oar(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        super(EquipmentType.OAR.code, x, y);

    }

    @Override
    public String toLogs() {
        return EquipmentType.OAR.shortCode + getPosLog();
    }

    @Override
    public String toString() {
        return "Oar(x: " + x + ", " + "y: " + y + " )";
    }

}
