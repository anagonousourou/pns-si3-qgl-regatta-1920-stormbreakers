package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;

public class Line2D {
    // class variables

    /**
     * Coordinates of starting point of the line
     */
    protected double x0;
    protected double y0;

    /**
     * Direction vector of the line. dx and dy should not be both zero.
     */
    protected double dx;
    protected double dy;

    // ===================================================================
    /** Defines a new Straight line going through the two given points. */

    public Line2D(Point2D point1, Point2D point2) {
        this.x0 = point1.x();
        this.y0 = point1.y();
        this.dx = point2.x() - point1.x();
        this.dy = point2.y() - point1.y();
    }

    public Line2D(double x0, double y0, double dx, double dy) {
        this.x0 = x0;
        this.y0 = y0;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Computes position on the line of the point given by (x,y). The position is
     * the number t such that if the point belong to the line, it location is given
     * by x=x0+t*dx and y=y0+t*dy.
     * <p>
     * If the point does not belong to the line, the method returns the position of
     * its projection on the line.
     */
    public double positionOnLine(double x, double y) {
        double denom = dx * dx + dy * dy;
        if (Math.abs(denom) < LineSegment2D.ACCURACY)
            throw new DegeneratedLine2DException("");
        return ((y - y0) * dy + (x - x0) * dx) / denom;
    }

    public Point2D point(double t) {
        t = Math.min(Math.max(t, 0), 1);
        return new Point2D(x0 + dx * t, y0 + dy * t);
    }
}