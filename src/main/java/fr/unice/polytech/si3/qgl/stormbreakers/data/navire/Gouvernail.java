package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Gouvernail extends Equipment {
	@JsonCreator
	public
    Gouvernail(@JsonProperty("x") int x, 
    		@JsonProperty("y") int y) {
        super(EquipmentType.RUDDER.code,x,y);
    }

    @Override
    public String toLogs() {
        return EquipmentType.RUDDER.shortCode+ getPosLog();
    }
}
