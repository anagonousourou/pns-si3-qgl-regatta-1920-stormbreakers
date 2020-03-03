package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

public interface CanCollide {

    boolean intersectsWith(LineSegment2D lineSegment2D);

}
