package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;

public class RectanglePositioned {

    private LineSegment2D largeur1;
    private LineSegment2D largeur2;
    private LineSegment2D longueur1;
    private LineSegment2D longueur2;

    private Point2D a;
    private Point2D b;
    private Point2D c;
    private Point2D d;

    public RectanglePositioned(Rectangle rectangle, Position position) {
        a = pointA(rectangle, position);
        b = pointB(rectangle, position);
        c = pointC(rectangle, position);
        d = pointD(rectangle, position);
        this.largeur1 = new LineSegment2D(a, b);
        this.largeur2 = new LineSegment2D(c, d);
        this.longueur1 = new LineSegment2D(d, a);
        this.longueur2 = new LineSegment2D(b, c);

    }

    Point2D pointA(Rectangle rectangle, Position position) {
        double radius = Math.hypot(rectangle.getHeight() / 2, rectangle.getWidth() / 2);
        double beta = Math.atan(rectangle.getWidth() / rectangle.getHeight());

        double alpha = rectangle.getOrientation() + position.getOrientation();

        double theta = alpha + beta;

        double xpoint = position.x() + radius * Math.cos(theta);
        double ypoint = position.y() + radius * Math.sin(theta);

        return new Point2D(xpoint, ypoint);
    }

    Point2D pointB(Rectangle rectangle, Position position) {
        double radius = Math.hypot(rectangle.getHeight() / 2, rectangle.getWidth() / 2);
        double beta = Math.atan(rectangle.getWidth() / rectangle.getHeight());

        double alpha = rectangle.getOrientation() + position.getOrientation();

        double theta = alpha - beta;

        double xpoint = position.x() + radius * Math.cos(theta);
        double ypoint = position.y() + radius * Math.sin(theta);

        return new Point2D(xpoint, ypoint);
    }

    Point2D pointC(Rectangle rectangle, Position position) {
        double radius = Math.hypot(rectangle.getHeight() / 2, rectangle.getWidth() / 2);
        double beta = Math.atan(rectangle.getWidth() / rectangle.getHeight());

        double alpha = rectangle.getOrientation() + position.getOrientation();

        double theta = Math.PI + alpha + beta;

        double xpoint = position.x() + radius * Math.cos(theta);
        double ypoint = position.y() + radius * Math.sin(theta);

        return new Point2D(xpoint, ypoint);
    }

    Point2D pointD(Rectangle rectangle, Position position) {
        double radius = Math.hypot(rectangle.getHeight() / 2, rectangle.getWidth() / 2);
        double beta = Math.atan(rectangle.getWidth() / rectangle.getHeight());

        double alpha = rectangle.getOrientation() + position.getOrientation();

        double theta = Math.PI + alpha - beta;

        double xpoint = position.x() + radius * Math.cos(theta);
        double ypoint = position.y() + radius * Math.sin(theta);

        return new Point2D(xpoint, ypoint);
    }

    public boolean intersectsWith(LineSegment2D lineSegment2D) {

        return largeur1.intersects(lineSegment2D) || longueur1.intersects(lineSegment2D)
                || largeur2.intersects(lineSegment2D) || longueur2.intersects(lineSegment2D);
    }

    /**
     * Retourne un des cotes que le segment passé en parametre touche
     * 
     * @param lineSegment2D
     * @return
     */
    public Optional<LineSegment2D> edgeOfIntersection(LineSegment2D lineSegment2D) {
        return List.of(largeur1, longueur1, largeur2, longueur2).stream()
                .filter(s -> lineSegment2D.intersects(lineSegment2D)).findFirst();
    }

    /**
     * Retourne le point du rectangle le plus proche de point2d
     * 
     * @param point2d
     * @return
     */
    // LATER: 15/03/2020 KEEP
    public Optional<Point2D> closestPointTo(IPoint point2d) {
        return List.of(largeur1, longueur1, largeur2, longueur2).stream().map(l -> l.closestPointTo(point2d))
                .min((p, pother) -> Double.compare(p.distanceTo(point2d), pother.distanceTo(point2d)));
    }

    /**
     * Return a list of points of the rectangle there must me enough points
     */
    public List<IPoint> pointsOfRectangle(double step) {

        return Utils.concatenate(largeur1.pointsOfSegment(step), largeur2.pointsOfSegment(step),
                longueur1.pointsOfSegment(step), longueur2.pointsOfSegment(step));
    }

    public Point2D getA() {
        return a;
    }

    public Point2D getB() {
        return b;
    }

    public Point2D getC() {
        return c;
    }

    public Point2D getD() {
        return d;
    }

    public List<Point2D> corners() {
        return List.of(a, b, c, d);
    }
}