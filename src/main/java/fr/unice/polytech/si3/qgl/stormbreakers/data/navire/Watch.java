package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Watch extends Equipment {

    @JsonCreator
    public Watch(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        super(EquipmentType.WATCH.code, x, y);
    }

    @Override
    public String toLogs() {
        return EquipmentType.WATCH.shortCode + getPosLog();
    }
}
