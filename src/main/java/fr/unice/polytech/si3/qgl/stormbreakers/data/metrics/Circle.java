package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.AbstractMap;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.math.*;

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
        Line2D line2D = lineSegment2D.getSupportingLine();
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
            Point2D anIntersection = this.findFirstIntersectingPoint(line2D);
            return Optional.of(anIntersection);
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


    class Point2DPair {
        AbstractMap.SimpleImmutableEntry<Point2D,Point2D> immutableEntry;

        Point2DPair(Point2D first, Point2D second) {
            immutableEntry = new AbstractMap.SimpleImmutableEntry<>(first,second);
        }

        Point2D getFirst() {
            return immutableEntry.getKey();
        }

        Point2D getSecond() {
            return immutableEntry.getValue();
        }
    }

    /**
     * Given a line intersecting this circle
     * computes the coordinates of one intersection point
     * @param line2D given intersecting line
     * @return the first intersection point found
     *
     * @throws UnsupportedOperationException if no intersection point found
     *   so if the segment wasn't intersecting in the first place
     *
     * @author David Lebrisse - Stormbreakers
     */
    Point2D findFirstIntersectingPoint(Line2D line2D) {
        // TODO: 05/03/2020 Tests
        Point2D originProjection = line2D.projectOnto(origin);
        Vector normalizedLineDirection = line2D.getNormalizedDirection();
        Point2D bounding1 = originProjection.getTranslatedBy(normalizedLineDirection.scaleVector(radius));
        LineSegment2D firstLineSegment = new LineSegment2D(originProjection,bounding1);

        return findOneIntersection(firstLineSegment);
    }

    /**
     * Given a line intersecting this circle
     * computes the coordinates of both intersection points
     * @param line2D given intersecting line
     * @return the intersection points
     *
     * @throws UnsupportedOperationException if no intersection points found
     *   so if the segment wasn't intersecting in the first place
     *
     * @author David Lebrisse - Stormbreakers
     */
    Point2DPair findBothIntersectingPoints(Line2D line2D) {
        // TODO: 05/03/2020 Tests
        Point2D originProjection = line2D.projectOnto(origin);
        Vector normalizedLineDirection = line2D.getDirection().normalize();

        Point2D bounding1 = originProjection.getTranslatedBy(normalizedLineDirection.scaleVector(radius));
        Point2D bounding2 = originProjection.getTranslatedBy(normalizedLineDirection.scaleVector(-radius));

        LineSegment2D firstLineSegment = new LineSegment2D(originProjection,bounding1);
        LineSegment2D secondLineSegment = new LineSegment2D(originProjection,bounding2);

        Point2D firstIntersection = findOneIntersection(firstLineSegment);
        Point2D secondIntersection= findOneIntersection(secondLineSegment);
        return new Point2DPair(firstIntersection,secondIntersection);
    }

    /**
     * Given a segment intersecting this circle
     * computes the coordinates of the intersection point
     * @param lineSegment2D given intersecting line segment
     * @return the intersection point
     *
     * @throws UnsupportedOperationException if no intersection point found
     *   so if the segment wasn't intersecting in the first place
     *
     * @author David Lebrisse - Stormbreakers
     */
    private Point2D findOneIntersection(LineSegment2D lineSegment2D) {
        // TODO: 05/03/2020 Tests
        // Calcul de l'intersection par dichotomie
        Line2D support = lineSegment2D.getSupportingLine();
        Point2D start = lineSegment2D.firstPoint();
        Point2D end = lineSegment2D.lastPoint();

        if (Utils.almostEquals(radius,this.distFromCenter(start)))
            return start;
        double minParam = support.lineParametorOf(start);

        if (Utils.almostEquals(radius,this.distFromCenter(end)))
            return end;
        double maxParam = support.lineParametorOf(end);

        double midParam = (maxParam+minParam)*0.5;

        Point2D possibleIntersection = support.point(midParam);
        while (! Utils.almostEquals(radius,this.distFromCenter(possibleIntersection)) && (maxParam-minParam)>Utils.EPS) {
            midParam = (maxParam+minParam)*0.5;
            double delta = distFromCenter(possibleIntersection)-radius;
            if (delta<0) {
                // We're outside the circle -> try closer to radius
                minParam = midParam;
            } else if (delta>0) {
                // We're inside the circle -> try further from radius
                maxParam = midParam;
            } else {
                // It's a perfect match
                // The last computed point is the one
                return possibleIntersection;
            }

            // Recompute the guessing point
            possibleIntersection = support.point(midParam);
        }

        // We stopped looping
        if ((maxParam-minParam)>Utils.EPS) {
            // because we found a close enough point
            return possibleIntersection;
        } else {
            // because we didn't find any Intersection
            throw new UnsupportedOperationException("Tried to find line-circle intersection, but there wasn't any !");
        }

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
		List<IPoint> list=new ArrayList<IPoint>();
		return list;
}
