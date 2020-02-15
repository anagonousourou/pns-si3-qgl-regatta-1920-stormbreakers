package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.Logger;

public class EquipmentManager implements PropertyChangeListener {
    // pour le moment seulement les rames et le gouvernail
    private List<Oar> leftOars;
    private List<Oar> rightOars;
    private List<Oar> oars;
    private List<Sail> sails;
    private Gouvernail rudder = null;
    private List<Equipment> equipments;
    private InputParser parser;

    public EquipmentManager(List<Equipment> equipments, int widthship, InputParser parser) {
        this.parser = parser;
        setUp(equipments, widthship);
    }
    public EquipmentManager(List<Equipment> equipments, int widthship) {
        setUp(equipments, widthship);
    }



    void setUp(List<Equipment> equipments, int widthship) {
        this.equipments = equipments;
        this.sails = this.equipments.stream().filter(e -> e.getType().equals("sail")).map(e -> (Sail) e)
                .collect(Collectors.toList());
        this.oars = this.equipments.stream().filter(e -> e.getType().equals("oar")).map(e -> (Oar) e)
                .collect(Collectors.toList());
        if (widthship % 2 == 1) {// impair
            this.leftOars = this.oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
            this.rightOars = this.oars.stream().filter(oar -> oar.getY() > widthship / 2).collect(Collectors.toList());
        } else {
            this.leftOars = this.oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
            this.rightOars = this.oars.stream().filter(oar -> oar.getY() >= widthship / 2).collect(Collectors.toList());
        }
        var optRuddder = this.equipments.stream().filter(e -> e.getType().equals("rudder")).map(e -> (Gouvernail) e)
                .findFirst();
        if (optRuddder.isPresent()) {
            this.rudder = optRuddder.get();
        }
    }

    /**
     * Méthode qui renvoie si oui ou non il y a un gouvernail
     * 
     * @return
     */
    public boolean rudderIsPresent() {
        return this.rudder != null;
    }

    List<Oar> allLeftOars() {
        return leftOars;
    }

    List<Oar> allRightOars() {
        return rightOars;
    }

    List<Oar> oars() {
        return oars;
    }

    int nbOars() {
        return this.oars.size();
    }

    List<Oar> usedRightOars() {
        return rightOars.stream().filter(Oar::isUsed).collect(Collectors.toList());
    }

    List<Oar> usedLeftOars() {
        return leftOars.stream().filter(Oar::isUsed).collect(Collectors.toList());
    }

    public List<Oar> unusedLeftOars() {
        return leftOars.stream().filter(Predicate.not(Oar::isUsed )).collect(Collectors.toList());
    }

    public List<Oar> unusedRightOars() {
        return rightOars.stream().filter(Predicate.not(Oar::isUsed )).collect(Collectors.toList());
    }

    List<Oar> toList() {
        return oars;
    }

    boolean oarPresentAt(IntPosition pos) {
        return this.oars.stream().anyMatch(oar -> oar.getX() == pos.getX() && oar.getY() == pos.getY());
    }

    Optional<Oar> oarAt(IntPosition pos) {
        return this.oars.stream().filter(oar -> oar.getX() == pos.getX() && oar.getY() == pos.getY()).findFirst();
    }

    IntPosition rudderPosition() {
        return new IntPosition(rudder.getX(), rudder.getY());
    }

    public int nbSails() {
        return this.sails.size();
    }

    public int nbOpennedSails() {
        return (int) this.sails.stream().filter(Sail::isOpenned).count();
    }
    
    /**
     * Permet de récupérer les voiles soit ouverte soit baisse
     * @param isOpened
     * @return List<Sail> - liste de voile ouverte si true sinon retourne les voiles baisse 
     */
    public List<Sail> sails(boolean isOpened) {
    	if(isOpened) {
    		return  this.sails.stream().filter(Sail::isOpenned).collect(Collectors.toList());
    	}
        return  this.sails.stream().filter(Predicate.not(Sail::isOpenned)).collect(Collectors.toList());
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String jString = (String) evt.getNewValue();
        try {
            this.setUp(this.parser.fetchEquipments(jString), this.parser.fetchBoatWidth(jString) );
        } catch (JsonProcessingException e) {
           Logger.getInstance().log(e.getMessage());
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
	public List<Sail> opennedSails() {
		return this.sails.stream().filter(Sail::isOpenned).collect(Collectors.toList());
	}
	public List<Sail> closedSails() {
		return this.sails.stream().filter(s-> !s.isOpenned() ).collect(Collectors.toList());
    }
    
    void resetUsedStatus(){
        this.equipments.forEach(Equipment::resetUsed);
    }

    Optional<Equipment> equipmentAt(IntPosition pos){
        return this.equipments.stream().filter(e-> e.getX()==pos.getX() && e.getY()==pos.getY()).findFirst();
    }

}