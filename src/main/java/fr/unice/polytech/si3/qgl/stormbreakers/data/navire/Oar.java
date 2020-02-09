package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Oar extends Equipment {
    private boolean used = false;
    @JsonCreator
    public Oar(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y
    ) {
        super(EquipmentType.OAR.code,x,y);
    }

    @Override
    public String toLogs() {
        return EquipmentType.OAR.shortCode+ getPosLog();
    }

	public boolean isUsed() {
		return used;
	}
}
