package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.AbstractMap;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Line2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

public class Circle extends Shape {
    private double radius;

    public Circle(double radius, Position anchor) {
        super("circle", anchor);
        this.radius = radius;
    }

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
    public boolean isPtInside(IPoint pt) {
        return distFromCenter(pt) <= radius;
    }

    private double distFromCenter(IPoint pt) {
        return getAnchorPoint().distanceTo(pt);
    }

    // =========
    // Methods for all shapes

    public Circle getBoundingCircle() {
        return this;
    }

    @Override
    public boolean collidesWith(Shape shape) {
        return shape.collidesWith(this); // Collision with circle
    }

    @Override
    public boolean collidesWith(Polygon polygon) {
        return polygon.collidesWith(this); // Collision with circle
    }

    @Override
    public boolean collidesWith(Circle circle) {
        double distBetweenCenters = distFromCenter(circle.getAnchorPoint());
        double combinedRadiuses = this.radius + circle.radius;
        return distBetweenCenters <= combinedRadiuses;

    }

    /**
     * Checks for collision between this shape and a given line segment
     * 
     * @param lineSegment2D the given line
     * @return true if it collides, false if not Note: The given line should be
     *         given relative to this shape's coordinates
     */
    public boolean collidesWith(LineSegment2D lineSegment2D) {
        // LATER: 14/03/2020 Tests
        Line2D support = lineSegment2D.getSupportingLine();
        double distanceToCenter = support.distance(getAnchorPoint());
        double delta = distanceToCenter - radius;
        if (delta > 0) {
            // No collision
            return false;
        } else if (Utils.almostEqualsBoundsIncluded(0, delta)) {
            // Line has only one intersection point
            // We need to compute said point
            Optional<Point2D> collisionPointOpt = this.intersect(support);
            return (collisionPointOpt.isPresent() && lineSegment2D.isCollinearPointOnSegment(collisionPointOpt.get()));
        } else {
            // Line has 2 intersection points
            if (this.isPtInside(lineSegment2D.firstPoint()) || this.isPtInside(lineSegment2D.lastPoint())) {
                // Si au moins une extremité est dans le cercle, il y a collision
                return true;
            }

            Point2DPair bothPoints = findBothIntersectingPoints(support);
            Point2D firstPoint = bothPoints.getFirst();
            Point2D secondPoint = bothPoints.getSecond();
            return (lineSegment2D.isCollinearPointOnSegment(firstPoint)
                    || lineSegment2D.isCollinearPointOnSegment(secondPoint));
        }
    }

    // =========

    /**
     * Checks for collision between this shape and a given line
     * 
     * @param line2D the given line
     * @return true if it collides, false if not Note: The given line should be
     *         given relative to this shape's coordinates
     */
    public boolean intersects(Line2D line2D) {
        // LATER: 05/03/2020 Tests
        double distanceToCenter = line2D.distance(getAnchorPoint());
        double delta = distanceToCenter - radius;
        // delta > 0 : No collision
        // else : Collision (even if on edge)
        return delta <= 0;
    }

    /**
     * Computes an intersection point between this shape and a given line segment
     * 
     * @param lineSegment2D the given line segment
     * @return an intersection point if it exists
     */
    public Optional<Point2D> intersect(LineSegment2D lineSegment2D) {
        // LATER Missing Test when supporting lines don't intersect and when the
        // intersection points are not on the line segment
        Line2D support = lineSegment2D.getSupportingLine();
        double distanceToCenter = support.distance(getAnchorPoint());
        double delta = distanceToCenter - radius;
        if (delta > 0 || Utils.almostEqualsBoundsIncluded(0, delta)) {
            // Max 1 collision point
            Optional<Point2D> intersectionOpt = this.intersect(support);
            if (intersectionOpt.isPresent()) {
                // There is an intersection point with the supporting line
                Point2D intersectionWithLine = intersectionOpt.get();
                if (lineSegment2D.isCollinearPointOnSegment(intersectionWithLine)) {
                    // The intersection point is on the line
                    return Optional.of(intersectionWithLine);
                } else {
                    // The intersection point is NOT on the line
                    return Optional.empty();
                }
            } else {
                // Even the supporting line doesn't intersect
                return Optional.empty();
            }
        } else {
            // Two Intersections with the supporting line
            Point2DPair bothPoints = findBothIntersectingPoints(support);
            Point2D firstPoint = bothPoints.getFirst();
            Point2D secondPoint = bothPoints.getSecond();

            if (lineSegment2D.isCollinearPointOnSegment(firstPoint))
                return Optional.of(firstPoint);
            else if (lineSegment2D.isCollinearPointOnSegment(secondPoint))
                return Optional.of(secondPoint);
            else
                return Optional.empty();
        }
    }

    /**
     * Computes the intersection point between this shape and a given line
     * 
     * @param line2D the given line
     * @return if it exists, the intersection point Note: The given line should be
     *         given relative to this shape's coordinates
     */
    public Optional<Point2D> intersect(Line2D line2D) {
        // LATER: 05/03/2020 Tests
        double distanceToCenter = line2D.distance(getAnchorPoint());
        double delta = distanceToCenter - radius;
        if (delta > 0) {
            // No collision
            return Optional.empty();
        } else if (Utils.almostEqualsBoundsIncluded(0, delta)) {
            // Only One Intersection
            Point2D linePoint = line2D.projectOnto(getAnchorPoint());
            return Optional.of(this.projectOntoEdge(linePoint));
        } else {
            // Two Intersections
            Point2D anIntersection = this.findFirstIntersectingPoint(line2D);
            return Optional.of(anIntersection);
        }
    }

    /**
     * Pulls any given point onto the circle's edge
     * 
     * @param point2D the give point
     * @return the new point, on the edge of the shape
     */
    private Point2D projectOntoEdge(Point2D point2D) {
        Vector fromCenterToPoint = new Vector(getAnchorPoint(), point2D);
        double ratio = fromCenterToPoint.norm() / radius;
        Vector fromCenterToEdge = fromCenterToPoint.scaleVector(ratio);
        return getAnchorPoint().getTranslatedBy(fromCenterToEdge);
    }

    class Point2DPair {
        AbstractMap.SimpleImmutableEntry<Point2D, Point2D> immutableEntry;

        Point2DPair(Point2D first, Point2D second) {
            immutableEntry = new AbstractMap.SimpleImmutableEntry<>(first, second);
        }

        Point2D getFirst() {
            return immutableEntry.getKey();
        }

        Point2D getSecond() {
            return immutableEntry.getValue();
        }
    }

    /**
     * Given a line intersecting this circle computes the coordinates of one
     * intersection point
     * 
     * @param line2D given intersecting line
     * @return the first intersection point found
     *
     * @throws UnsupportedOperationException if no intersection point found so if
     *                                       the segment wasn't intersecting in the
     *                                       first place
     *
     * @author David Lebrisse - Stormbreakers
     */
    Point2D findFirstIntersectingPoint(Line2D line2D) {
        // LATER: 05/03/2020 Tests
        Point2D anchorProjection = line2D.projectOnto(getAnchorPoint());
        Vector normalizedLineDirection = line2D.getNormalizedDirection();
        Point2D bounding1 = anchorProjection.getTranslatedBy(normalizedLineDirection.scaleVector(radius));
        LineSegment2D firstLineSegment = new LineSegment2D(anchorProjection, bounding1);

        return findOneIntersection(firstLineSegment);
    }

    /**
     * Given a line intersecting this circle computes the coordinates of both
     * intersection points
     * 
     * @param line2D given intersecting line
     * @return the intersection points
     *
     * @throws UnsupportedOperationException if no intersection points found so if
     *                                       the segment wasn't intersecting in the
     *                                       first place
     *
     * @author David Lebrisse - Stormbreakers
     */
    Point2DPair findBothIntersectingPoints(Line2D line2D) {
        Point2D anchorProjection = line2D.projectOnto(getAnchorPoint());
        Vector normalizedLineDirection = line2D.getDirection().normalize();

        Point2D bounding1 = anchorProjection.getTranslatedBy(normalizedLineDirection.scaleVector(radius));
        Point2D bounding2 = anchorProjection.getTranslatedBy(normalizedLineDirection.scaleVector(-radius));

        LineSegment2D firstLineSegment = new LineSegment2D(anchorProjection, bounding1);
        LineSegment2D secondLineSegment = new LineSegment2D(anchorProjection, bounding2);

        Point2D firstIntersection = findOneIntersection(firstLineSegment);
        Point2D secondIntersection = findOneIntersection(secondLineSegment);
        return new Point2DPair(firstIntersection, secondIntersection);
    }

    /**
     * Given a segment intersecting this circle computes the coordinates of the
     * intersection point
     * 
     * @param lineSegment2D given intersecting line segment
     * @return the intersection point
     *
     * @throws UnsupportedOperationException if no intersection point found so if
     *                                       the segment wasn't intersecting in the
     *                                       first place
     *
     * @author David Lebrisse - Stormbreakers
     */
    private Point2D findOneIntersection(LineSegment2D lineSegment2D) {
        // Calcul de l'intersection par dichotomie
        Point2D start = lineSegment2D.firstPoint(); // Always inside the circle
        Point2D end = lineSegment2D.lastPoint(); // Always outside the circle

        if (Utils.almostEquals(radius, this.distFromCenter(start)))
            return start;

        if (Utils.almostEquals(radius, this.distFromCenter(end)))
            return end;

        Point2D testedPoint = new LineSegment2D(start, end).getMiddle();
        while (!Utils.almostEquals(radius, this.distFromCenter(testedPoint))
                && !Utils.almostEqualsBoundsIncluded(start.distanceTo(end), 0.0, 0.1)) {
            double delta = distFromCenter(testedPoint) - radius;
            if (delta > 0) {
                // We're outside the circle -> try closer to radius
                end = testedPoint;
            } else if (delta < 0) {
                // We're inside the circle -> try further from radius
                start = testedPoint;
            } else {
                // It's a perfect match
                // The last computed point is the one
                return testedPoint;
            }

            // Recompute the guessing points
            testedPoint = new LineSegment2D(start, end).getMiddle();
        }

        // We stopped looping
        if (Utils.almostEqualsBoundsIncluded(start.distanceTo(end), 0.0, 0.1)) {
            // because we found a close enough point
            return testedPoint;
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
        // orientation doesn't matter for a circle
        return getAnchorPoint().equals(other.getAnchorPoint()) && Utils.almostEquals(other.radius, radius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anchor, radius);
    }

    @Override
    public String toString() {
        return getType() + " at " + anchor.toString() + " : (" + radius + ")";
    }

    @Override
    public String toLogs() {
        return "C" + radius;
    }

    @Override
    public ShapeType getTypeEnum() {
        return ShapeType.CIRCLE;
    }

    @Override
    public boolean isInsideOpenShape(IPoint pt) {
        return distFromCenter(pt) < radius;
    }

    @Override
    public IPoint intersectionPoint(IPoint depart, IPoint arrive) {
        //NEW Code to test LATER
        LineSegment2D lineSegment2D = new LineSegment2D(depart, arrive);
        return this.findOneIntersection(lineSegment2D);
    }

    @Override
    public Shape wrappingShape(double margin) {
        return new Circle(radius+margin,anchor);
    }

}
