package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.RectanglePositioned;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;

public class Rectangle extends Shape {
    private double width;
    private double height;
    private double orientation;
    Polygon rectanglePolygon;

    public Rectangle(double width, double height,
                     double orientation, Position anchor) {
        super("rectangle", anchor);
        this.width = width;
        this.height = height;
        this.orientation = orientation;
        this.rectanglePolygon = buildAsPolygon();
    }

    private Polygon buildAsPolygon() {
        // TODO: 16/03/2020 Quick test
        double halfLength = height/2;
        double halfWidth = width/2;

        List<Point2D> rectangleVertices = new ArrayList<>();
        rectangleVertices.add(new Point2D(halfLength,halfWidth));
        rectangleVertices.add(new Point2D(-halfLength,halfWidth));
        rectangleVertices.add(new Point2D(-halfLength,-halfWidth));
        rectangleVertices.add(new Point2D(halfLength,-halfWidth));
        return new Polygon(this.orientation,rectangleVertices, this.anchor);
    }

    @JsonCreator
    public Rectangle(@JsonProperty("width") double width, @JsonProperty("height") double height,
            @JsonProperty("orientation") double orientation) {
        super("rectangle");
        this.width = width;
        this.height = height;
        this.orientation = orientation;
        this.rectanglePolygon = buildAsPolygon();
    }

    @JsonProperty("width")
    public double getWidth() {
        return width;
    }

    @JsonProperty("height")
    public double getHeight() {
        return height;
    }

    @JsonProperty("orientation")
    public double getOrientation() {
        return orientation;
    }

    @Override
    public ShapeType getTypeEnum() {
        return ShapeType.RECTANGLE;
    }

    @Override
    public boolean isPtInside(Point2D pt) {

        Point2D point2D = new Point2D(pt);
        // On ramene le plan pour que les cotes du rectangle soient sur les axes du
        // repere
        double totalOrientation = orientation + getAnchorOrientation();

        // Point relatif au centre de Rectangle 0
        point2D = new Point2D(pt.x()-anchor.x(),pt.y()-anchor.y());

        if (totalOrientation != 0) {
            // Orientation du rectangle compensée
            point2D = point2D.getRotatedAround(origin,-totalOrientation);
        }
        return isPtInRectangle0(point2D);


        //return rectanglePolygon.isPtInside(pt);
    }

    // =========
    // Methods for all shapes
    // TODO: 12/03/2020 Make Rectangle a Polygon -> FIXES StreamManagerTests

    @Override
    public boolean collidesWith(Shape shape) {
        return rectanglePolygon.collidesWith(shape);
    }

    @Override
    public boolean collidesWith(Polygon polygon) {
        return rectanglePolygon.collidesWith(polygon);
    }

    @Override
    public boolean collidesWith(Circle circle) {
        return rectanglePolygon.collidesWith(circle);
    }

    @Override
    public boolean collidesWith(LineSegment2D lineSegment2D) {
        //return rectanglePolygon.collidesWith(lineSegment2D);

        // TODO: 12/03/2020 Figure out why polygon collision doesn't work in tests
        return new RectanglePositioned(this, this.getAnchor()).intersectsWith(lineSegment2D);

    }

    @Override
    public Circle getBoundingCircle() {
        return rectanglePolygon.getBoundingCircle();
    }

    // =========

    /**
     * Vérifie si le point est dans le rectangle d'orientation 0
     * 
     * @param pt point à tester
     * @return true s'il est dedans, false sinon
     */
    private boolean isPtInRectangle0(IPoint pt) {
        double eps = Math.pow(10, -10);

        boolean xOk = between(pt.x(), -height / 2, height / 2, eps);
        boolean yOk = between(pt.y(), -width / 2, width / 2, eps);
        return xOk && yOk;
    }

    /**
     * true if min <= value <= max, false if not
     * 
     * @param value   value to test
     * @param min     minimum value
     * @param max     maximum value
     * @param epsilon error correction
     * @return true if min <= value <= max, false if not
     */
    private boolean between(double value, double min, double max, double epsilon) {
        return simpleBetween(value, min, max) || simpleBetween(value - epsilon, min, max)
                || simpleBetween(value + epsilon, min, max);
    }

    private boolean simpleBetween(double value, double min, double max) {
        return (min <= value) && (value <= max);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Rectangle))
            return false;
        Rectangle other = (Rectangle) obj;

        return Utils.within(other.width-this.width, Utils.EPSILON)
                && Utils.within(other.height-this.height, Utils.EPSILON)
                && Utils.within(other.orientation-this.orientation, Utils.EPS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, orientation);
    }

    @Override
    public String toString() {
        return getType() + ": (" + width + " " + height + " " + orientation + ")";
    }

    @Override
    public String toLogs() {
        return "R" + "(" + width + "|" + height + "|" + orientation + ")";
    }

    // TODO: 15/03/2020 KEEP ?
    public Point2D findPointNearestToPosition(Position other, Position rectangle) {
        RectanglePositioned rect = new RectanglePositioned(this, rectangle);
        Point2D p = new Point2D(other.x(), other.y());
        var tmp = rect.closestPointTo(p);
        return tmp.isPresent() ? tmp.get() : null;
    }

    // TODO: 15/03/2020 KEEP
    public boolean haveGoodOrientation(Checkpoint cp, Point2D boatposition, Point2D courantPos) {
        // tourner la plan pour que le courant est un angle 0
        Point2D ptCp = cp.getPosition().getPoint2D().getTranslatedBy(cp.getPosition().x() - courantPos.x(),
                cp.getPosition().y() - courantPos.y());
        ptCp = ptCp.getRotatedBy(-orientation);
        Point2D ptBoat = boatposition.getTranslatedBy(boatposition.x() - courantPos.x(),
                boatposition.y() - courantPos.y());
        ptBoat = ptBoat.getRotatedBy(-orientation);
        return ptCp.x() > 0 && ptCp.x() > ptBoat.x();
    }

}
