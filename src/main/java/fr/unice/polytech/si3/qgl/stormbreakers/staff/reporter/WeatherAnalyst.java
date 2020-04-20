package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

public class WeatherAnalyst {

    private Wind wind;
    private Boat boat;
    private EquipmentsManager equipmentsManager;
    // Later streams

    public WeatherAnalyst(Wind wind, Boat boat, EquipmentsManager equipmentsManager) {
        this.boat = boat;
        this.wind = wind;
        this.equipmentsManager = equipmentsManager;
    }

    /**
     * Calculate the norm of the speed procured by the wind Ex:current number of
     * lifted sails
     * 
     * @return
     */
    public double currentExternalSpeed() {
        if (wind != null && equipmentsManager.nbSails() != 0) {
            return ((double) equipmentsManager.nbOpennedSails() / equipmentsManager.nbSails()) * wind.getStrength()
                    * Math.cos(this.boat.getOrientation() - this.wind.getOrientation());
        }
        return 0.0;
    }

    public double externalSpeedGivenNbOpennedSails(int nbOpennedSails) {
        if (wind != null && equipmentsManager.nbSails() != 0) {
            return ((double) nbOpennedSails / equipmentsManager.nbSails()) * wind.getStrength()
                    * Math.cos(this.boat.getOrientation() - this.wind.getOrientation());
        }
        return 0.0;
    }

    /**
     * Dis si il existe un courant ou un stream susceptible de "pousser ou ralentir"
     * le bateau permet donc de savoir si il y a d'autres parametres que les rames
     * dans le calcul de vitesse
     * 
     * @return
     */
    public boolean speedFromWindExists() {
        if (wind == null) {
            return false;
        } else {
            return !Utils.almostEquals(wind.getStrength(), 0.0, Utils.EPSILON);
        }
    }

    /**
     * Méthode chargée de retourner la vitesse maximale que pourrait apporter par
     * les éléments ex: dans le cas du vent le rapport nbOpennedSails/nbSails est
     * remplacé par 1
     * 
     * @return
     */
    public double potentialSpeedAcquirable() {
        if (wind != null && equipmentsManager.nbSails() != 0) {
            return wind.getStrength() * Math.cos(this.boat.getOrientation() - this.wind.getOrientation());
        }
        return 0.0;
    }

    public double speedProvided(IPoint depart, IPoint arrive) {
        if (wind != null && equipmentsManager.nbSails() != 0) {
            return wind.getStrength()
                    * Math.cos(new Vector(depart, arrive).getOrientation() - this.wind.getOrientation());
        }
        return 0.0;
    }

}