package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

public class Watch extends Equipment {

    public Watch(int x, int y) {
        super(EquipmentType.WATCH.code, x, y);
    }

    @Override
    public String toLogs() {
        return EquipmentType.WATCH.shortCode + getPosLog();
    }
}
