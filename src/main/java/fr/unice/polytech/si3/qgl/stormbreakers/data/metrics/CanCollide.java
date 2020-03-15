package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

public interface CanCollide {

    // TODO: 12/03/2020 Implement for all shapes
    boolean collidesWith(Shape shape);
    boolean collidesWith(Polygon polygon);
    boolean collidesWith(Circle circle);
    boolean collidesWith(LineSegment2D lineSegment2D);


    //public abstract boolean intersect(Circle circle);

    /**
     * Renvoie le cercle dans lequel la forme est inscrite
     */
    // TODO: 12/03/2020 Implement for all shapes
    Circle getBoundingCircle();

}
