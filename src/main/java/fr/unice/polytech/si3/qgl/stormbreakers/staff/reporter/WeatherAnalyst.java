package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;

public class WeatherAnalyst{

    private Wind wind;
    private Boat boat;
    private EquipmentsManager equipmentsManager;
    //Later streams

    public WeatherAnalyst(Wind wind, Boat boat, EquipmentsManager equipmentsManager) {
        this.boat = boat;
        this.wind = wind;
        this.equipmentsManager = equipmentsManager;
    }



    /**
     * Calculate the norm of the speed procured by the wind and later
     * the streams given the current and actual conditions
     *  Ex:current number of lifted sails
     * @return
     */
    public double currentExternalSpeed() {
        if(wind!=null && equipmentsManager.nbSails()!=0){
            return ((double) equipmentsManager.nbOpennedSails()/equipmentsManager.nbSails())*wind.getStrength()*Math.cos(
                this.boat.getOrientation()-this.wind.getOrientation()
            );
        }
        return 0.0;
    }

    public double externalSpeedGivenNbOpennedSails(int nbOpennedSails){
        if(wind!=null && equipmentsManager.nbSails()!=0){
            return ((double) nbOpennedSails/equipmentsManager.nbSails())*wind.getStrength()*Math.cos(
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
        if(wind!=null && equipmentsManager.nbSails()!=0){
            return wind.getStrength()*Math.cos(
                this.boat.getOrientation()-this.wind.getOrientation()
            );
        }
        return 0.0;
    }


}