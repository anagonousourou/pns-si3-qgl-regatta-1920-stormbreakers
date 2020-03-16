package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

public interface CanCollide {

    boolean collidesWith(Shape shape); // TODO: 16/03/2020 Test for timeout (should not be recursive)
    boolean collidesWith(Polygon polygon);
    boolean collidesWith(Circle circle);
    boolean collidesWith(LineSegment2D lineSegment2D);


    //public abstract boolean intersect(Circle circle);

    /**
     * Renvoie le cercle dans lequel la forme est inscrite
     */
    Circle getBoundingCircle();

}
