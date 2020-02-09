package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;

public class EquipmentManager {
    // pour le moment seulement les rames
    private List<Oar> leftOars;
    private List<Oar> rightOars;
    private List<Oar> oars;

    public EquipmentManager(List<Oar> rames, int widthship) {
        this.oars = rames;
        if (widthship % 2 == 1) {// impair
            this.leftOars = this.oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
            this.rightOars = this.oars.stream().filter(oar -> oar.getY() > widthship / 2).collect(Collectors.toList());
        } else {
            this.leftOars = this.oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
            this.rightOars = this.oars.stream().filter(oar -> oar.getY() >= widthship / 2).collect(Collectors.toList());
        }
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

}