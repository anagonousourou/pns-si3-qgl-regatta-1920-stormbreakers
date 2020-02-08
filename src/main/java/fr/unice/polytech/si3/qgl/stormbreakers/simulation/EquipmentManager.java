package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class EquimentManager {
    // pour le moment seulement les rames
    List<Oar> leftOars;
    List<Oar> rightOars;
    List<Oar> oars;

    EquimentManager(List<Oar> rames, int widthship) {
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

    List<Oar> usedRightOars() {
        return new ArrayList<>();
    }

    List<Oar> usedLeftOars() {
        return new ArrayList<>();
    }

    List<Oar> toList() {
        return oars;
    }

    boolean oarPresentAt(IntPosition pos){
        return this.oars.stream().filter(oar-> oar.x==pos.getX() && oar.y==pos.getY()).findFirst().isPresent();
    }

    Optional<Oar> oarAt(IntPosition pos){
        return this.oars.stream().filter(oar-> oar.x==pos.getX() && oar.y==pos.getY()).findFirst();
    }

}