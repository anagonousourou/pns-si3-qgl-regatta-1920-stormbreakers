package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Rudder;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Watch;
import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.ParsingException;
import fr.unice.polytech.si3.qgl.stormbreakers.io.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;

public class EquipmentsManager implements PropertyChangeListener {
    // pour le moment seulement les rames et le gouvernail
    private List<Oar> leftOars;
    private List<Oar> rightOars;
    private List<Oar> oars;
    private List<Sail> sails;
    private Rudder rudder = null;
    private Watch watch;
    
    private List<Equipment> equipments;
    private InputParser parser;

    public EquipmentsManager(List<Equipment> equipments, int widthship, InputParser parser) {
        this.parser = parser;
        setUp(equipments, widthship);
    }

    public EquipmentsManager(List<Equipment> equipments, int widthship) {
        setUp(equipments, widthship);
    }

    void setUp(List<Equipment> equipments, int widthship) {
        this.equipments = equipments;
        this.sails = this.equipments.stream().filter(e -> e.getType().equals("sail")).map(e -> (Sail) e)
                .collect(Collectors.toList());
        this.oars = this.equipments.stream().filter(e -> e.getType().equals("oar")).map(e -> (Oar) e)
                .collect(Collectors.toList());
        if (widthship % 2 == 1) {// impair
            this.leftOars = this.oars.stream().filter(oar -> oar.y() < widthship / 2).collect(Collectors.toList());
            this.rightOars = this.oars.stream().filter(oar -> oar.y() > widthship / 2).collect(Collectors.toList());
        } else {
            this.leftOars = this.oars.stream().filter(oar -> oar.y() < widthship / 2).collect(Collectors.toList());
            this.rightOars = this.oars.stream().filter(oar -> oar.y() >= widthship / 2).collect(Collectors.toList());
        }
        var optRuddder = this.equipments.stream().filter(e -> e.getType().equals("rudder")).map(e -> (Rudder) e)
                .findFirst();
        optRuddder.ifPresent(gouvernail -> this.rudder = gouvernail);
        
        Optional<Watch> optVigie = this.equipments.stream().filter(e -> e.getType().equals("watch")).map(e -> (Watch) e)
                .findFirst();
        optVigie.ifPresent(vigie -> this.watch = vigie);
    }

    /**
     * Méthode qui renvoie si oui ou non il y a un gouvernail
     * 
     * @return
     */
    public boolean rudderIsPresent() {
        return this.rudder != null;
    }

    public boolean isRudderUsed() {
        return rudder.isUsed();
    }
    
    /**
     * Méthode qui renvoie si oui ou non il y a la vigie
     * 
     * @return
     */
    public boolean watchIsPresent() {
        return this.watch != null;
    }

    public boolean isWatchUsed() {
        return watch.isUsed();
    }

    public List<Oar> allLeftOars() {
        return leftOars;
    }

    public List<Oar> allRightOars() {
        return rightOars;
    }

    public List<Oar> oars() {
        return oars;
    }

    public int nbOars() {
        return this.oars.size();
    }

    public List<Oar> unusedOars() {
        return oars.stream().filter(Predicate.not(Oar::isUsed)).collect(Collectors.toList());
    }

    public List<Oar> unusedLeftOars() {
        return leftOars.stream().filter(Predicate.not(Oar::isUsed)).collect(Collectors.toList());
    }

    public List<Oar> unusedRightOars() {
        return rightOars.stream().filter(Predicate.not(Oar::isUsed)).collect(Collectors.toList());
    }

    boolean oarPresentAt(IntPosition pos) {
        return this.oars.stream().anyMatch(oar -> oar.x() == pos.x() && oar.y() == pos.y());
    }

    public IntPosition rudderPosition() {
        return new IntPosition(rudder.x(), rudder.y());
    }
    
    public IntPosition watchPosition() {
        return new IntPosition(watch.x(), watch.y());
    }

    public int nbSails() {
        return this.sails.size();
    }

    public int nbOpennedSails() {
        return (int) this.sails.stream().filter(Sail::isOpenned).count();
    }

    /**
     * Permet de récupérer les voiles soit ouverte soit baisse
     * 
     * @param isOpened
     * @return List<Sail> - liste de voile ouverte si true sinon retourne les voiles
     *         baisse
     */
    public List<Sail> sails(boolean isOpened) {
        if (isOpened) {
            return this.sails.stream().filter(Sail::isOpenned).collect(Collectors.toList());
        }
        return this.sails.stream().filter(Predicate.not(Sail::isOpenned)).collect(Collectors.toList());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String jString = (String) evt.getNewValue();
        try {
            this.setUp(this.parser.fetchEquipments(jString), this.parser.fetchBoatWidth(jString));
        } catch (ParsingException e) {
            Logger.getInstance().logErrorMsg(e);
        }

    }

    public int nbLeftOars() {
        return this.leftOars.size();
    }

    public int nbRightOars() {
        return this.rightOars.size();
    }

    public List<Sail> sails() {
        return sails;
    }

    public List<Sail> closedSails() {
        return this.sails.stream().filter(s -> !s.isOpenned()).collect(Collectors.toList());
    }

    public void resetUsedStatus() {
        this.equipments.forEach(Equipment::resetUsed);
    }

    public Optional<Equipment> equipmentAt(IntPosition pos) {
        return this.equipments.stream().filter(e -> e.x() == pos.x() && e.y() == pos.y()).findFirst();
    }

}