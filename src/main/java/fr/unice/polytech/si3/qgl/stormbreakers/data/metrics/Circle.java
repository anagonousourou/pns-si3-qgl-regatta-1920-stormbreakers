package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Line2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

public class Circle extends Shape {
    private double radius;

    @JsonCreator
    public Circle(@JsonProperty("radius") double radius) {
        super("circle");
        this.radius = radius;
    }

    @JsonProperty("radius")
    public double getRadius() {
        return radius;
    }

    @Override
    public boolean isPtInside(Point2D pt) {
        return distFromCenter(pt) <= radius;
    }

    private double distFromCenter(Point2D pt) {
        return new Point2D(0, 0).getDistanceTo(pt);
    }

    /**
     * Computes the intersection point between this shape and a given line segment
     * @param lineSegment2D the given line segment
     * @return if it exists, the intersection point
     */
    public Optional<Point2D> intersect(LineSegment2D lineSegment2D){
        // TODO: 05/03/2020 Tests
        Line2D line2D = lineSegment2D.getCorrespondingLine();
        return this.intersect(line2D);
    }

    /**
     * Computes the intersection point between this shape and a given line
     * @param line2D the given line
     * @return if it exists, the intersection point
     * Note: The given line should be given relative to this shape's coordinates
     */
    public Optional<Point2D> intersect(Line2D line2D){
        // TODO: 05/03/2020 Tests
        double distanceToCenter = line2D.distance(origin);
        double delta = distanceToCenter - radius;
        if (delta > 0) {
            // No collision
            return Optional.empty();
        } else if (delta==0) {
            // Only One Intersection
            Point2D linePoint = line2D.projectOnto(origin);
            return Optional.of(this.projectOntoEdge(linePoint));
        } else {
            // Two Intersections
            // TODO: 05/03/2020 Resolution par dichotomie
            return Optional.of(origin); // CHANGE THIS
        }
    }

    /**
     * Pulls any given point onto the circle's edge
     * @param point2D the give point
     * @return the new point, on the edge of the shape
     */
    private Point2D projectOntoEdge(Point2D point2D) {
        // TODO: 05/03/2020 Tests
        Vector fromCenterToPoint = new Vector(origin,point2D);
        double ratio = fromCenterToPoint.norm() / radius;
        Vector fromCenterToEdge = fromCenterToPoint.scaleVector(ratio);
        return origin.getTranslatedBy(fromCenterToEdge);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Circle))
            return false;
        Circle other = (Circle) obj;
        return other.radius == radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(radius);
    }

    @Override
    public String toString() {
        return getType() + ": (" + radius + ")";
    }

    @Override
    public String toLogs() {
        return "C" + radius;
    }
}
