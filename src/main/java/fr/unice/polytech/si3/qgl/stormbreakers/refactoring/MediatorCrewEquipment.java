package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;

public class MediatorCrewEquipment {
    private Crew crew;
    private EquipmentManager equipmentManager;

    public MediatorCrewEquipment(Crew crew, EquipmentManager equipmentManager) {
        this.crew = crew;
        this.equipmentManager=equipmentManager;
    }


    public boolean rudderIsAccesible(){

        return this.crew.marinAround(this.equipmentManager.rudderPosition());
    }
    /**
     * Renvoie le marin sur le gouvernail ou le plus proche du gouvernail
     * @return
     */
    public Marine marineForRudder(){
        var optMarine =this.crew.marineAtPosition(this.equipmentManager.rudderPosition()).or(()->
            this.crew.marineClosestTo(this.equipmentManager.rudderPosition())
        );

        if(optMarine.isPresent()){
            return optMarine.get();
        }
        //
        return null;
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

    public int nbMarinsOnLeftOars(){
        return (int) this.equipmentManager.allLeftOars().stream().
        filter(oar-> this.crew.marineAtPosition(oar.getPosition()).isPresent() ).count();
    }

    public int nbMarinsOnRightOars(){
        return (int) this.equipmentManager.allRightOars().stream().
        filter(oar-> this.crew.marineAtPosition(oar.getPosition()).isPresent() ).count();
    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à gauche
     * 
     * @param equimentManager
     * @return
     */
    public List<Marine> leftMarinsOnOars() {
        return this.crew.leftMarinsOnOars(this.equipmentManager);

    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à droite
     * 
     * @return
     */
    public List<Marine> rightMarinsOnOars() {
        
        return this.crew.rightMarinsOnOars(this.equipmentManager);

    }
    /**
     * Return sailoractions to activate exactly nb oars on left
     * if it is not possible return empty list
     * Do not make compromise the size of the list must be equals to nb or 0
     * sailors actions include OarAction and MoveAction
     * @param nb
     * @param idsOfBusySailors
     * @return
     */
    public List<SailorAction> activateOarsOnLeft(int nb){
        //Use the isDoneTurn method to know if a sailor is available or not
        //but do not set value doneTurn only methods in Captain will do that
        return List.of();
    }

    /**
     * Similar to activateOarsOnLeft(int nb) but on right
     * @param nb
     * @return
     */
    public List<SailorAction> activateOarsOnRight(int nb){
        //Use the isDoneTurn method to know if a sailor has been used or not
        return List.of();
    }


	public void resetAvailability() {
        this.crew.resetAvailability();;
    }
    
    public Marine getMarinById(int id){
        return this.crew.getMarinById(id).get();
    }

    
}