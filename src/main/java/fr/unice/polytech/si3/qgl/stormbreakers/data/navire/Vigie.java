package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

public class Vigie extends Equipment {

    Vigie(int x, int y) {
        super(EquipmentType.WATCH.code,x,y);
    }

    @Override
    public String toLogs() {
        return "w"+getX()+getY();
    }
}
