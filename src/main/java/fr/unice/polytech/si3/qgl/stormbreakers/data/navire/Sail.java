package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

public class Sail extends Equipment {

    private boolean openned;

    public Sail(int x, int y) {
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
