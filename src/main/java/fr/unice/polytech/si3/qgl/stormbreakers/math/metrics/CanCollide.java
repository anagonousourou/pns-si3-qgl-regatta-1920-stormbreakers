package fr.unice.polytech.si3.qgl.stormbreakers.math.metrics;

import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

public interface CanCollide {

    boolean collidesWith(Shape shape); 

    boolean collidesWith(Polygon polygon);

    boolean collidesWith(Circle circle);

    boolean collidesWith(LineSegment2D lineSegment2D);

    /**
     * Renvoie le cercle dans lequel la forme est inscrite
     */
    Circle getBoundingCircle();

}
