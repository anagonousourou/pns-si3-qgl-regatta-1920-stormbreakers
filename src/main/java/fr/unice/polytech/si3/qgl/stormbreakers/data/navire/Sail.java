package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sail extends Equipment {

    private boolean openned;

    @JsonCreator
    public Sail(@JsonProperty("x") int x,@JsonProperty("y") int y) {
        super(EquipmentType.SAIL.code, x, y);
        openned = false;
    }

    public Sail(int x, int y, boolean openned) {
        super(EquipmentType.SAIL.code, x, y);
        this.openned = openned;
    }

    @Override
    public String toLogs() {
        return EquipmentType.SAIL.shortCode + ((openned) ? "O" : "C") + getPosLog();
    }

    public boolean isOpenned() {
        return openned;
    }

    public void setOpenned(boolean openned) {
        this.openned = openned;
    }

}
