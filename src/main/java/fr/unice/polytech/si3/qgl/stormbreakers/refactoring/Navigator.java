package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

/**
 *  Utilitaire de calcul destinés à la navigation
 */
public class Navigator {

    /**
     * Renvoie l'orientation nécessaire pour se diriger vers le checkpoint
     * ie pour orienter le boat dans la direction du checkpoint
     * @param boatPosition
     * @param checkpointPosition
     * @return
     */
    public double additionalOrientationNeeded(Position boatPosition,Point2D target){

        // On effectue un translation pour ramener virtuellement
        // Les coordonnées du bateau en (0,0)
        target.getTranslatedBy(-boatPosition.getX(),-boatPosition.getY());





        return 0;
    }

    /**
     * Return the difference nbleftoars-nbrightoars needed to get the 
     * angle
     * TODO add parameters if needed
     * @param angle
     * @return
     */
    public int fromAngleToDiff(double angle){
        //TODO 
        return 0;
    }

    
    
}