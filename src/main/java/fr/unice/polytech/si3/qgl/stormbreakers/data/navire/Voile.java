package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

public class Voile extends Equipment {

    private boolean openned;

    public Voile(int x, int y) {
        super(EquipmentType.SAIL.code, x, y);
        openned = false;
    }

    public Voile(int x, int y, boolean openned) {
        super(EquipmentType.SAIL.code, x, y);
        this.openned = openned;
    }

    @Override
    public String toLogs() {
        return EquipmentType.SAIL.shortCode + ((openned) ? "O" : "C") + getPosLog();
    }
}
