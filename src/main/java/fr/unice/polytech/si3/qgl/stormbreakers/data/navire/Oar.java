package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.IntPosition;

public class Oar extends Equipment {
    private final IntPosition position;
    @JsonCreator
    public Oar(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y
    ) {
        super(EquipmentType.OAR.code,x,y);
        position=new IntPosition(x,y);
    }

    @Override
    public String toLogs() {
        return EquipmentType.OAR.shortCode+ getPosLog();
    }

    public IntPosition getPosition() {
        return position;
    }

    

	
}
