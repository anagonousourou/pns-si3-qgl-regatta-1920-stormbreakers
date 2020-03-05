package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;

import java.util.Optional;

public class Line2D {

    static final Vector verticalDirection = new Vector(0,1);

    private Point2D anchor;
    private Vector direction;

    private EquationDroite equationDroite;

    /** Defines a new Straight line going through the two given points. */
    public Line2D(double x0, double y0, double dx, double dy) {
        this(new Point2D(x0,y0),new Vector(dx,dy));
    }

    /** Defines a new Straight line going through the two given points. */
    public Line2D(Point2D P1, Point2D P2) {
        this(P1,P1.getVectorTo(P2));
    }

    /** Defines a new Straight line going through anchor with a given direction. */
    public Line2D(Point2D anchor, Vector direction) {
        if (Utils.almostEquals(direction.getDeltaX(),0)) {
            // We have a vertical line
            this.direction = verticalDirection;
            equationDroite = null;
        } else {
            // Not a vertical line
            equationDroite = new EquationDroite(anchor,anchor.getTranslatedBy(direction));
            this.direction = direction;
        }
        this.anchor = anchor;
    }

    private boolean isVerticalLine() {
        return direction.equals(verticalDirection);
    }

    /**
     * Computes the <i>line parameter</i> of the point given by (x,y) for this line
     * The position is the number t such that if the point belong to the line,
     * its location is given by x=x0+t*dx and y=y0+t*dy.
     * Note: The point needs to be on the line.
     */
    public double lineParametorOf(double x, double y) {
        double dx = direction.getDeltaX();
        double dy = direction.getDeltaY();

        double denom = dx * dx + dy * dy;

        if (Math.abs(denom) < LineSegment2D.ACCURACY)
            throw new DegeneratedLine2DException("");

        double x0 = anchor.x();
        double y0 = anchor.y();
        return ((y - y0) * dy + (x - x0) * dx) / denom;
    }

    public double lineParametorOf(Point2D P) {
        // TODO: 05/03/2020 Tests if not any
        return lineParametorOf(P.x(),P.y());
    }

    /**
     * Computes the coordinates of the point given by (x,y) where
     * x=x0+lineParameter*dx
     * y=y0+lineParameter*dy
     * @return the projection
     */
    public Point2D point(double lineParameter) {
        double x0 = anchor.x();
        double y0 = anchor.y();

        double dx = direction.getDeltaX();
        double dy = direction.getDeltaY();

        lineParameter = Math.min(Math.max(lineParameter, 0), 1);
        return new Point2D(x0 + dx * lineParameter, y0 + dy * lineParameter);
    }

    /**
     * Computes the projection on the line of the point given by (x,y).
     * @return Point2D resulting from the projection
     */
    public Point2D projectOnto(Point2D pointToProject) {
        Point2D projectionPoint;
        if (isVerticalLine()) {
            projectionPoint = new Point2D(anchor.x(),pointToProject.y());
        } else {
            Vector AP = new Vector(anchor,pointToProject);
            // A + AB * ( AP.AB / |AB|² )
            projectionPoint = anchor.getTranslatedBy( direction.scaleVector( AP.scal(direction) / direction.squaredNorm() ) );
        }
        return projectionPoint;
    }

    /**
     * Computes the intersection point between this line and a given one
     * @param other second line
     * @return if it exists, the intersection point
     */
    public Optional<Point2D> intersect(Line2D other) {
        // TODO: 05/03/2020 Tests
        if (this.isVerticalLine() && other.isVerticalLine()) {
            // Both vertical
            if (this.anchor.x() == other.anchor.x()) {
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
            Point2D intersection = new Point2D(intersectionX,intersectionY);
            return Optional.of(intersection);
        }

        else if (other.isVerticalLine())
            // intersect relation is symmetric so why bother reimplementing it
            return other.intersect(this);

        else {
            // Both are non vertical lines
            EquationDroite eq1 = this.equationDroite;
            EquationDroite eq2 = other.equationDroite;

            // On cherche x t.q. : y1(x) = y2(x)
            //  soit : a1*x+b1 = a2*x+b2
            //  d'où : (a1-a2) * x = (b2-b1)
            // On obtiens : x = (b2-b1)/(a1-a2)
            double intersectionX = eq1.findCommonSolution(eq2);
            double intersectionY = eq2.evalY(intersectionX);

            Point2D intersection = new Point2D(intersectionX,intersectionY);
            return Optional.of(intersection);
        }
    }

    /**
     * Computes the distance for a given point to this Line
     * @param P the given point
     * @return the computed distance
     */
    public double distance(Point2D P) {
        // TODO: 05/03/2020 Tests
        return new Vector(P,this.projectOnto(P)).norm();
    }

    public Vector getDirection() {
        return new Vector(direction);
    }

    public Vector getNormalizedDirection() {
        // TODO: 05/03/2020 Tests
        return getDirection().normalize();
    }
}