package fr.unice.polytech.si3.qgl.stormbreakers.processing.navire;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

/**
 *  Utilitaire de calcul destinés à la navigation
 */
public class Navigator {

    /**
     * Renvoie l'orientation nécessaire pour se diriger vers une cible
     * ie pour orienter le boat dans la direction du checkpoint
     * @param boatPosition position du boat
     * @param target coordonnées de la cible
     * @return double angle entre [-3Pi/4,3Pi/4]
     */
    public double additionalOrientationNeeded(Position boatPosition, Point2D target){

        if (boatPosition.getPoint2D().getDistanceTo(target) < Math.pow(10,-10)) {
            // On est sur la target, on n'a aucunement besoin de tourner
            return 0;
        }

        // On effectue un translation pour ramener virtuellement
        // Les coordonnées du bateau en (0,0)
        Point2D target2 = target.getTranslatedBy(-boatPosition.getX(),-boatPosition.getY());
        // On effectue une rotation pour ramener virtuellement
        // la direction du bateau sur l'axe X;
        Point2D target3 = target2.getRotatedBy(-boatPosition.getOrientation());
        // On recupère l'angle entre le vecteur position de la cible et l'axe X
        double actualAngle = target3.getAngleFromXAxis();

        // Si la cible est completement derrière,
        // on dit par default qu'elle est derrière à gauche
        if (actualAngle == Math.PI) return 0.75 * Math.PI;

        if (actualAngle > 0) return Math.min(actualAngle,(0.75 * Math.PI));
        else if (actualAngle < 0) return Math.max(actualAngle,(-0.75 * Math.PI));
        else return actualAngle;
    }

}