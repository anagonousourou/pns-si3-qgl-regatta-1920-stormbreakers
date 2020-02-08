package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

public class Voile extends Equipment {

    private boolean openned;

    Voile(int x, int y) {
        super(EquipmentType.SAIL.code,x,y);
        openned = false;
    }


    @Override
    public String toLogs() {
        return EquipmentType.SAIL.shortCode + ((openned)?"O":"C") + getPosLog();
    }
}
