package fr.unice.polytech.si3.qgl.stormbreakers.refactor2;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

/**
 * Angle pour se diriger vers un point cible
 */

public class Cap {

    private Point2D posActuelle;
    private double orientationActuelle;

    private Point2D objectif;

    /**
     * Renvoie l'orientation nécessaire pour se diriger vers le checkpoint
     * ie pour orienter le boat dans la direction du checkpoint
     * @param boatPosition
     * @param checkpointPosition
     * @return
     */
    Cap(Position boat2DPos, double actualAngle ,Position checkpoint2DPos){

    }

    /**
     * Renvoie l'angle nécessaire duquel tourner
     * @return
     */
    public double getAngle() {
        //TODO complete carefully
    }
}

