package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rame extends Equipment {

    @JsonCreator
    public Rame(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y
    ) {
        super(EquipmentType.OAR.code,x,y);
    }

    @Override
    public String toLogs() {
        return "o"+getX()+getY();
    }
}
