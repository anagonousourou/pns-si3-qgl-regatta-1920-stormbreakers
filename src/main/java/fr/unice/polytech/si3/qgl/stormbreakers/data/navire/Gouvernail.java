package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

public class Gouvernail extends Equipment {

    Gouvernail(int x, int y) {
        super(EquipmentType.RUDDER.code,x,y);
    }

    @Override
    public String toLogs() {
        return EquipmentType.RUDDER.shortCode+ getX() + getY();
    }
}
