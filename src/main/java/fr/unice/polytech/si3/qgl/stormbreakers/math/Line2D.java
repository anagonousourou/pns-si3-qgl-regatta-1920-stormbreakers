package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.Objects;
import java.util.Optional;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;

public class Line2D {

    static final Vector verticalDirection = new Vector(0, 1);

    private Point2D anchor;
    private Vector direction;

    private EquationDroite equationDroite;

    /** Defines a new Straight line going through the two given points. */
    public Line2D(Point2D p1, Point2D p2) {
        this(p1, p1.getVectorTo(p2));
    }

    /** Defines a new Straight line going through anchor with a given direction. */
    public Line2D(Point2D anchor, Vector direction) {
        if (Utils.almostEquals(0.0, direction.norm()))
            throw new DegeneratedLine2DException("Cannot create line with direction set to null vector");

        if (Utils.almostEquals(direction.getDeltaX(), 0)) {
            // We have a vertical line
            this.direction = verticalDirection; // facing up
            if (direction.getDeltaY() < 0)
                this.direction = this.direction.scaleVector(-1); // facing down
            equationDroite = null;
        } else {
            // Not a vertical line
            equationDroite = new EquationDroite(anchor, anchor.getTranslatedBy(direction));
            this.direction = direction;
        }
        this.anchor = anchor;
    }

    private boolean isVerticalLine() {
        return Vector.areCollinear(this.direction, Line2D.verticalDirection);
    }

    /**
     * Computes the <i>line parameter</i> of the given point P for this line The
     * parameter is the number k such that if the point belong to the line, its
     * location is given by P(k)=P0+k*direction, where P0 is the anchor. Note: The
     * point needs to be on the line.
     */
    public double lineParameterOf(Point2D p) {
        Vector relativeTranslation = new Vector(anchor, p);
        return relativeTranslation.norm() / direction.norm();
    }

    /**
     * Computes the coordinates of the point given by (x,y) where
     * x=x0+lineParameter*dx y=y0+lineParameter*dy
     * 
     * @return the projection
     */
    public Point2D pointFromLineParameter(double lineParameter) {
        return anchor.getTranslatedBy(direction.scaleVector(lineParameter));
    }

    /**
     * Computes the projection on the line of the point given by (x,y).
     * 
     * @return Point2D resulting from the projection
     */
    public Point2D projectOnto(Point2D pointToProject) {
        Point2D projectionPoint;
        if (isVerticalLine()) {
            projectionPoint = new Point2D(anchor.x(), pointToProject.y());
        } else {
            Vector ap = new Vector(anchor, pointToProject);
            // A + AB * ( AP.AB / |AB|² )
            projectionPoint = anchor
                    .getTranslatedBy(direction.scaleVector(ap.scal(direction) / direction.squaredNorm()));
        }
        return projectionPoint;
    }

    /**
     * Computes the intersection point between this line and a given one
     * 
     * @param other second line
     * @return if it exists, the intersection point
     */
    public Optional<Point2D> intersect(Line2D other) {
        if (this.isVerticalLine() && other.isVerticalLine()) {
            // Both vertical
            if (Utils.almostEquals(this.anchor.x(), other.anchor.x())) {
                // Lines are collinear -> Infinite collision points
                return Optional.of(this.anchor);
            } else {
                // Lines are parallel -> No collision
                return Optional.empty();
            }
        }

        else if (this.isVerticalLine()) {
            double intersectionX = this.anchor.x();
            double intersectionY = other.equationDroite.evalY(intersectionX);
            Point2D intersection = new Point2D(intersectionX, intersectionY);
            return Optional.of(intersection);
        }

        else if (other.isVerticalLine())
            // intersect relation is symmetric so why bother reimplementing it
            return other.intersect(this);

        else {
            // Both are non vertical lines
            Vector thisDirection = this.direction;
            Vector otherDirection = other.direction;
            if (Vector.areCollinear(thisDirection, otherDirection)) {
                // Lines are parallel
                return Optional.empty();
            } else {
                // Lines are intersecting
                EquationDroite eq1 = this.equationDroite;
                EquationDroite eq2 = other.equationDroite;

                // On cherche x t.q. : y1(x) = y2(x)
                // soit : a1*x+b1 = a2*x+b2
                // d'où : (a1-a2) * x = (b2-b1)
                // On obtiens : x = (b2-b1)/(a1-a2)
                double intersectionX = eq1.findCommonSolution(eq2);
                double intersectionY = eq2.evalY(intersectionX);

                Point2D intersection = new Point2D(intersectionX, intersectionY);
                return Optional.of(intersection);
            }

        }
    }

    /**
     * Computes the distance for a given point to this Line
     * 
     * @param p the given point
     * @return the computed distance
     */
    public double distance(Point2D p) {
        return p.distanceTo(this.projectOnto(p));
    }

    public Vector getDirection() {
        return new Vector(direction);
    }

    public Vector getNormalizedDirection() {
        return getDirection().normalize();
    }

    public boolean contains(Point2D point2D) {
        double distance = distance(point2D);
        return Utils.almostEquals(0, distance,2.5e-3);
    }

    @Override
    public String toString() {
        return "Line2D: anchor:" + anchor.toString() + "dir:" + direction.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (! (obj instanceof Line2D)) return false;
        Line2D other = (Line2D) obj;

        boolean sameDirection = Vector.areCollinear(this.direction,other.direction);
        return sameDirection && this.distance(other.anchor)==0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.direction.getOrientation()%Math.PI, projectOnto(new Point2D(0,0)));
    }
}