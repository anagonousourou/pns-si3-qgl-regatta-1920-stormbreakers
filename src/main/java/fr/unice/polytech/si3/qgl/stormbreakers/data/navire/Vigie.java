package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Vigie extends Equipment {

    @JsonCreator
    public Vigie(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        super(EquipmentType.WATCH.code, x, y);
    }

    @Override
    public String toLogs() {
        return EquipmentType.WATCH.shortCode + getPosLog();
    }
}
