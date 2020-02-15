package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;

public class WeatherAnalyst{

    private Wind wind;
    private Boat boat;
    private EquipmentManager equipmentManager;
    //Later streams

    public WeatherAnalyst(Wind wind, Boat boat, EquipmentManager equipmentManager) {
        this.boat = boat;
        this.wind = wind;
        this.equipmentManager = equipmentManager;
    }



    /**
     * Calculate the norm of the speed procured by the wind and later
     * the streams given the current and actual conditions
     *  Ex:current number of lifted sails
     * @return
     */
    public double currentExternalSpeed() {
        if(wind!=null && equipmentManager.nbSails()!=0){
            return ((double) equipmentManager.nbOpennedSails()/equipmentManager.nbSails())*wind.getStrength()*Math.cos(
                this.boat.getOrientation()-this.wind.getOrientation()
            );
        }
        return 0.0;
    }

    public double externalSpeedGivenNbOpennedSails(int nbOpennedSails){
        if(wind!=null && equipmentManager.nbSails()!=0){
            return ((double) nbOpennedSails/equipmentManager.nbSails())*wind.getStrength()*Math.cos(
                this.boat.getOrientation()-this.wind.getOrientation()
            );
        }
        return 0.0;
    }

    /**
     * Dis si il existe un courant ou un stream susceptible de "pousser ou ralentir"
     * le bateau permet donc de savoir si il y a d'autres parametres que les rames dans le calcul de vitesse
     * @return
     */
    public boolean additionalSpeedExists() {
        return wind != null;
    }
    
    /**
     * Méthode chargée de retourner la vitesse maximale que pourrait apporter par les 
     * éléments ex: dans le cas du vent le rapport nbOpennedSails/nbSails est remplacé par
     * 1
     * @return
     */
    public double potentialSpeedAcquirable(){
        if(wind!=null && equipmentManager.nbSails()!=0){
            return wind.getStrength()*Math.cos(
                this.boat.getOrientation()-this.wind.getOrientation()
            );
        }
        return 0.0;
    }


}