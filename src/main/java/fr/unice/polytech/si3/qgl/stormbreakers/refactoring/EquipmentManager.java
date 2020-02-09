package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;

public class EquipmentManager {
    // pour le moment seulement les rames et le gouvernail
    private List<Oar> leftOars;
    private List<Oar> rightOars;
    private List<Oar> oars;
    private Gouvernail rudder=null;
    private List<Equipment> equipments;

    public EquipmentManager(List<Equipment> equipments,int widthship){
        this.equipments=equipments;
        this.oars=this.equipments.stream().filter(e->e.getType().equals("oar")).map(e->(Oar)e).collect(Collectors.toList());
        if (widthship % 2 == 1) {// impair
            this.leftOars = this.oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
            this.rightOars = this.oars.stream().filter(oar -> oar.getY() > widthship / 2).collect(Collectors.toList());
        } else {
            this.leftOars = this.oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
            this.rightOars = this.oars.stream().filter(oar -> oar.getY() >= widthship / 2).collect(Collectors.toList());
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

    List<Oar> oars(){
        return oars;
    }

    int nbOars(){
        return this.oars.size();
    }

    List<Oar> usedRightOars() {
        return rightOars.stream().filter(oar->oar.isUsed()).collect(Collectors.toList());
    }

    List<Oar> usedLeftOars() {
        return new ArrayList<>();
    }

    List<Oar> toList() {
        return oars;
    }

    boolean oarPresentAt(IntPosition pos){
        return this.oars.stream().filter(oar-> oar.getX()==pos.getX() && oar.getY()==pos.getY()).findFirst().isPresent();
    }

    Optional<Oar> oarAt(IntPosition pos){
        return this.oars.stream().filter(oar-> oar.getX()==pos.getX() && oar.getY()==pos.getY()).findFirst();
    }

    IntPosition rudderPosition(){
        return new IntPosition(rudder.getX(),rudder.getY());
    }

}