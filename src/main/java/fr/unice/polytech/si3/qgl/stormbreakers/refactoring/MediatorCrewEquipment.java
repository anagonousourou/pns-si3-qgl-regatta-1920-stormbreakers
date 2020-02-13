package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;

public class MediatorCrewEquipment {
    private Crew crew;
    private EquipmentManager equipmentManager;
    private static final int MAXDISTANCE=5;

    public MediatorCrewEquipment(Crew crew, EquipmentManager equipmentManager) {
        this.crew = crew;
        this.equipmentManager=equipmentManager;
    }

    void validateActions(List<SailorAction> actions) {
        actions.forEach(action -> {
            this.getMarinById(action.getSailorId()).setDoneTurn(true);
        });

    }


    public boolean rudderIsAccesible(){

        return this.crew.marinAround(this.equipmentManager.rudderPosition());
    }
    /**
     * Renvoie le marin sur le gouvernail ou le plus proche du gouvernail
     * @return
     */
    public Optional<Marine> marineForRudder(){
        return this.crew.marineAtPosition(this.equipmentManager.rudderPosition()).or(()->
            this.crew.marineClosestTo(this.equipmentManager.rudderPosition())
        );
    }

    public List<SailorAction> activateRudder(double orientation){
        var optMarine=this.marineForRudder();
        if(optMarine.isPresent()){
            Marine rudderMarine =optMarine.get();
            List<SailorAction> actions = new ArrayList<>();
            Move tmpMove = rudderMarine.getPosition().howToMoveTo(this.rudderPosition());
            actions.add(new MoveAction(rudderMarine.getId(), tmpMove.getXdistance(), tmpMove.getYdistance()));
            actions.add(new Turn(rudderMarine.getId(), orientation));
            return actions;
        }
        return List.of();
        
    }

    public boolean rudderIsPresent(){
        return this.equipmentManager.rudderIsPresent();
    }

    public IntPosition rudderPosition(){
        return this.equipmentManager.rudderPosition();
    }

    public int nbOars(){
        return this.equipmentManager.nbOars();
    }

    public int nbSails(){
        return this.equipmentManager.nbSails();
    }

    public boolean allSailsAreOpenned(){
        return this.nbSails()==this.nbSailsOpenned();
    }

    private int nbSailsOpenned() {
        return this.equipmentManager.nbOpennedSails();
    }

    public int nbMarinsOnLeftOars() {
        return (int) this.equipmentManager.allLeftOars().stream().
        filter(oar-> this.crew.marineAtPosition(oar.getPosition()).isPresent() ).count();
    }

    public int nbMarinsOnRightOars(){
        return (int) this.equipmentManager.allRightOars().stream().
        filter(oar-> this.crew.marineAtPosition(oar.getPosition()).isPresent() ).count();
    }

    public Map<Equipment, List<Marine>> marinsDisponibles() {
        HashMap<Equipment, List<Marine>> results = new HashMap<>();
        this.equipmentManager.oars().forEach(oar -> results.put(oar,
                this.crew.marins().stream().filter(m -> m.getPosition().distanceTo(oar.getPosition()) <= MediatorCrewEquipment.MAXDISTANCE)
                        .collect(Collectors.toList())));
        return results;
    }

    
    /**
     * Return sailoractions to activate exactly nb oars on left
     * if it is not possible return empty list
     * Do not make compromise the size of the list must be equals to either nb or 0
     * sailors actions include OarAction and MoveAction
     * @param nb
     * @param idsOfBusySailors
     * @return
     */
    public List<SailorAction> activateOarsOnLeft(int nb){
        return this.activateNbOars(this.equipmentManager.allLeftOars(), nb);
    }

    /**
     * Similar to activateOarsOnLeft(int nb) but on right
     * @param nb
     * @return
     */
    public List<SailorAction> activateOarsOnRight(int nb){
        //Use the isDoneTurn method to know if a sailor has been used or not
        return this.activateNbOars(this.equipmentManager.allRightOars(), nb);
    }

    List<SailorAction> activateNbOars(List<Oar> oars,int nb){

        //Use the isDoneTurn method and a list to know if a sailor is available or not
        //but do not set value doneTurn only methods in Captain will do that
        List<SailorAction> result = new ArrayList<>();
        List<Marine> yetBusy = new ArrayList<>();
        int compteur = 0;
        Map<Equipment, List<Marine>> correspondances = this.marinsDisponibles();
        for (Equipment oar : oars ) {
            if (correspondances.get(oar) != null) {
                for (Marine m : correspondances.get(oar)) {
                    if (!yetBusy.contains(m) && !m.isDoneTurn()) {
                        yetBusy.add(m);
                        result.add(new OarAction(m.getId()));
                        result.add(m.howToGoTo(oar.getX(), oar.getY()));
                        compteur++;
                        break;
                    }
                }
            }
            if (compteur == nb) {
                break;
            }

        }
        return result;
    }

    

    /**
     * @param nb différence entre nbgauche-nbdroite if nb 
     * @return
     */
    public List<SailorAction> activateOars(int nb){

        if(nb > 0){
            return this.activateOarsOnRight(nb);
        }
        else{
            return this.activateOarsOnLeft(-nb);
        }

    }
    /**
     * 
     * TODO
     * @return
     */
    public List<SailorAction> activateOarsEachSide(){
        return List.of();

    }


	public void resetAvailability() {
        this.crew.resetAvailability();;
    }
    
    public Marine getMarinById(int id){
        return this.crew.getMarinById(id).get();
    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à gauche
     * 
     * @param equimentManager
     * @return
     */
    public List<Marine> leftMarinsOnOars() {
        List<Marine> marines = new ArrayList<>();
        for(Oar oar:equipmentManager.allLeftOars()){
            if(this.crew.marineAtPosition(oar.getPosition()).isPresent()){
                marines.add(this.crew.marineAtPosition(oar.getPosition()).get());
            }
        }

        return marines;

    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à droite
     * 
     * @param equimentManager
     * @return
     */
    public List<Marine> rightMarinsOnOars() {
        List<Marine> marines = new ArrayList<>();
        for(Oar oar:equipmentManager.allRightOars()){
            if(this.crew.marineAtPosition(oar.getPosition()).isPresent()){
                marines.add(this.crew.marineAtPosition(oar.getPosition()).get());
            }
        }

        return marines;
    }

    /**
     * TODO
     * Fonction qui renvoie si il est possible de fermer/descendre 
     * toutes les voiles actuellement ouvertes
     * @return un boleen
     */
    public boolean canLowerAllSails(){
        return false;
    }

    /**
     * TODO
     * @return
     */
    public List<SailorAction> actionsToLowerSails(){
        return List.of();
    }

    //TODO
	public boolean canLiftAllSails() {
		return false;
	}

    /**
     * TODO
     * @return
     */
	public List<SailorAction> actionsToLiftSails() {
		return List.of();
	}

    /**
     * TODO complete
     * @return
     */
	public boolean canAccelerate() {
		return false;
	}

    
}