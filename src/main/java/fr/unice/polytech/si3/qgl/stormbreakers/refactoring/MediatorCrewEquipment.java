package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

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
}