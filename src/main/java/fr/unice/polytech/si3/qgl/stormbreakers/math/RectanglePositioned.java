package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;

public class RectanglePositioned {

    private LineSegment2D largeur1;
    private LineSegment2D largeur2;
    private LineSegment2D longueur1;
    private LineSegment2D longueur2;

    public RectanglePositioned(Rectangle rectangle, Position position) {

        this.largeur1 = new LineSegment2D(pointA(rectangle, position), pointB(rectangle, position));
        this.largeur2 = new LineSegment2D(pointC(rectangle, position), pointD(rectangle, position));
        this.longueur1 = new LineSegment2D(pointD(rectangle, position), pointA(rectangle, position));
        this.longueur2 = new LineSegment2D(pointB(rectangle, position), pointC(rectangle, position));

    }

    Point2D pointA(Rectangle rectangle, Position position) {
        double radius = Math.hypot(rectangle.getHeight() / 2, rectangle.getWidth() / 2);
        double beta = Math.atan(rectangle.getWidth() / rectangle.getHeight());

        double alpha = rectangle.getOrientation();

        double theta = alpha + beta;

        double xpoint = position.getX() + radius * Math.cos(theta);
        double ypoint = position.getY() + radius * Math.sin(theta);

        return new Point2D(xpoint, ypoint);
    }

    Point2D pointB(Rectangle rectangle, Position position) {
        double radius = Math.hypot(rectangle.getHeight() / 2, rectangle.getWidth() / 2);
        double beta = Math.atan(rectangle.getWidth() / rectangle.getHeight());

        double alpha = rectangle.getOrientation();

        double theta = alpha - beta;

        double xpoint = position.getX() + radius * Math.cos(theta);
        double ypoint = position.getY() + radius * Math.sin(theta);

        return new Point2D(xpoint, ypoint);
    }

    Point2D pointC(Rectangle rectangle, Position position) {
        double radius = Math.hypot(rectangle.getHeight() / 2, rectangle.getWidth() / 2);
        double beta = Math.atan(rectangle.getWidth() / rectangle.getHeight());

        double alpha = rectangle.getOrientation();

        double theta = Math.PI + alpha + beta;

        double xpoint = position.getX() + radius * Math.cos(theta);
        double ypoint = position.getY() + radius * Math.sin(theta);

        return new Point2D(xpoint, ypoint);
    }

    Point2D pointD(Rectangle rectangle, Position position) {
        double radius = Math.hypot(rectangle.getHeight() / 2, rectangle.getWidth() / 2);
        double beta = Math.atan(rectangle.getWidth() / rectangle.getHeight());

        double alpha = rectangle.getOrientation();

        double theta = Math.PI + alpha - beta;

        double xpoint = position.getX() + radius * Math.cos(theta);
        double ypoint = position.getY() + radius * Math.sin(theta);

        return new Point2D(xpoint, ypoint);
    }

    public boolean intersectsWith(LineSegment2D lineSegment2D){
        return largeur1.intersects(lineSegment2D)||longueur1.intersects(lineSegment2D)||largeur2.intersects(lineSegment2D)||longueur2.intersects(lineSegment2D);
    }
}