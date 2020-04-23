package fr.unice.polytech.si3.qgl.stormbreakers.math.metrics;

public interface IPoint {

    double x();

    double y();

    default double distanceTo(IPoint other) {
        return Math.hypot(other.x() - this.x(), other.y() - this.y());
    }

    static double distance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    /**
     * Computes the orientation of the 3 points: returns +1 is the path P0->P1->P2
     * turns Counter-Clockwise, -1 if the path turns Clockwise, and 0 if the point
     * P2 is located on the line segment [P0 P1]. Algorithm taken from Sedgewick.
     * 
     * @param p0 the initial point
     * @param p1 the middle point
     * @param p2 the last point
     * @return +1, 0 or -1, depending on the relative position of the points
     */
    static int ccw(IPoint p0, IPoint p1, IPoint p2) {
        double x0 = p0.x();
        double y0 = p0.y();
        double dx1 = p1.x() - x0;
        double dy1 = p1.y() - y0;
        double dx2 = p2.x() - x0;
        double dy2 = p2.y() - y0;

        if (dx1 * dy2 > dy1 * dx2)
            return +1;
        if (dx1 * dy2 < dy1 * dx2)
            return -1;
        if ((dx1 * dx2 < 0) || (dy1 * dy2 < 0))
            return -1;
        if (Math.hypot(dx1, dy1) < Math.hypot(dx2, dy2))
            return +1;
        return 0;
    }

    static IPoint centerPoints(IPoint aPoint, IPoint bPoint) {
        return new IPoint() {

            @Override
            public double y() {
                return (aPoint.y() + bPoint.y()) / 2;
            }

            @Override
            public double x() {

                return (aPoint.x() + bPoint.x()) / 2;
            }
        };
    }
}