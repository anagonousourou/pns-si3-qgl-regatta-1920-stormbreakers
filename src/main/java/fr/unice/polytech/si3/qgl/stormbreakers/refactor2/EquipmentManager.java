package fr.unice.polytech.si3.qgl.stormbreakers.refactor2;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;

import java.util.List;
import java.util.stream.Collectors;

public class EquipmentManager {

    // pour le moment seulement les rames
    private List<Oar> leftOars;
    private List<Oar> rightOars;
    private Gouvernail rudder=null;
    private List<Equipment> equipments;

    public EquipmentManager(List<Equipment> equipments,int widthship){
        this.equipments=equipments;
        List<Oar> oars=this.equipments.stream().filter(e->e.getType().equals("oar")).map(e->(Oar)e).collect(Collectors.toList());
        if (widthship % 2 == 1) {// impair
            this.leftOars = oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
            this.rightOars = oars.stream().filter(oar -> oar.getY() > widthship / 2).collect(Collectors.toList());
        } else {
            this.leftOars = oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
            this.rightOars = oars.stream().filter(oar -> oar.getY() >= widthship / 2).collect(Collectors.toList());
        }
        var optRuddder=this.equipments.stream().filter(e->e.getType().equals("rudder")).map(e->(Gouvernail)e).findFirst();
        if(optRuddder.isPresent()){
            this.rudder=optRuddder.get();
        }

    }

    /**
     * Méthode qui renvoie si oui ou non il y a un gouvernail
     * @return
     */
    public boolean rudderIsPresent(){
        return this.rudder!=null;
    }

    List<Oar> allLeftOars() {
        return leftOars;
    }

    List<Oar> allRightOars() {
        return rightOars;
    }

    public int getOarsCount() {
        return leftOars.size() + rightOars.size();
    }

}
