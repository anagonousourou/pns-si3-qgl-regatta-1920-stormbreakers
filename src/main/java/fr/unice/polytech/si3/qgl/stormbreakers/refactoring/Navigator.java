package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.util.*;

/**
 * Utilitaire de calcul destinés à la navigation
 */
public class Navigator {

    /**
     * Renvoie l'orientation nécessaire pour se diriger vers une cible ie pour
     * orienter le boat dans la direction du checkpoint
     * 
     * @param boatPosition position du boat
     * @param target       coordonnées de la cible
     * @return double angle entre [-3Pi/4,3Pi/4]
     */
    public double additionalOrientationNeeded(Position boatPosition, Point2D target) {

        if (boatPosition.getPoint2D().getDistanceTo(target) < Math.pow(10, -10)) {
            // On est sur la target, on n'a aucunement besoin de tourner
            return 0;
        }

        // On effectue un translation pour ramener virtuellement
        // Les coordonnées du bateau en (0,0)
        Point2D target2 = target.getTranslatedBy(-boatPosition.getX(), -boatPosition.getY());
        // On effectue une rotation pour ramener virtuellement
        // la direction du bateau sur l'axe X;
        Point2D target3 = target2.getRotatedBy(-boatPosition.getOrientation());
        // On recupère l'angle entre le vecteur position de la cible et l'axe X
        double actualAngle = target3.getAngleFromXAxis();

        // Si la cible est completement derrière,
        // on dit par default qu'elle est derrière à gauche
        if (actualAngle == Math.PI)
            return 0.75 * Math.PI;

        if (actualAngle > 0)
            return Math.min(actualAngle, (0.75 * Math.PI));
        else if (actualAngle < 0)
            return Math.max(actualAngle, (-0.75 * Math.PI));
        else
            return actualAngle;
    }

    /**
     * Renvoie les configurations de rames possibles
     * 
     * @param nbLeftOars  nombre total de rames à gauches
     * @param nbRightOars nombre total de rames à droite
     * @return Set<OarConfig> les configurations
     */
    Set<OarConfig> possibleOarConfigs(int nbLeftOars, int nbRightOars) {
        int nbOars = nbLeftOars + nbRightOars;
        Set<OarConfig> possibleConfigs = new HashSet<>();

        for (int i = 0; i <= nbLeftOars; i++) {
            for (int j = 0; j <= nbRightOars; j++) {
                Fraction currentFraction = new Fraction(j - i, nbOars);
                // On limite à la rotation maximale induite par les rames : plus/moins Pi/2
                if (Math.abs(currentFraction.eval()) <= 0.5) {
                    possibleConfigs.add(new OarConfig(currentFraction, j - i));
                }
            }
        }
        return possibleConfigs;
    }

    /**
     * Renvoie les configurations de rames possibles
     * 
     * @param nbLeftOars  nombre total de rames à gauches
     * @param nbRightOars nombre total de rames à droite
     * @return Set<OarConfig> les configurations
     */
    Map<Fraction, Integer> pairAngleDifference(int nbLeftOars, int nbRightOars) {
        int nbOars = nbLeftOars + nbRightOars;
        Map<Fraction, Integer> possibleConfigs = new HashMap<>();

        for (int i = 0; i <= nbLeftOars; i++) {
            for (int j = 0; j <= nbRightOars; j++) {
                Fraction currentFraction = new Fraction(j - i, nbOars);
                // On limite à la rotation maximale induite par les rames : plus/moins Pi/2
                if (Math.abs(currentFraction.eval()) <= 0.5) {
                    possibleConfigs.put(currentFraction, j - i);
                }
            }
        }
        return possibleConfigs;
    }

    int fromAngleToDiff(double orientation, int nbLeftOars, int nbRightOars) {
        Map<Fraction, Integer> correspondances = this.pairAngleDifference(nbLeftOars, nbRightOars);
        Set<Fraction> eventailAngles = correspondances.keySet();
        Optional<Fraction> optAngle = eventailAngles.stream().filter(a -> a.eval() * orientation > 0.0)

                .min((a, b) -> Double.compare(Math.abs(a.eval() * Math.PI - orientation),
                        Math.abs(b.eval() * Math.PI - orientation)));
        if (optAngle.isPresent()) {
            Fraction approachingAngle = optAngle.get();
            return correspondances.get(approachingAngle);
        }
        return 0;
    }

}