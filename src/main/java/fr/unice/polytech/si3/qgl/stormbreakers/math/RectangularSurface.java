package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;

public class RectangularSurface implements Surface {
    private double x;
    private double y;
    private double orientation;
    private Shape shape;

    public RectangularSurface(double x, double y, double orientation, Shape shape) {
        this.shape = shape;
        this.shape.setAnchor(new Position(x, y, orientation));
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    @Override
    public double x() {

        return x;
    }

    @Override
    public double y() {

        return y;
    }

    @Override
    public double getOrientation() {

        return orientation;
    }

    @Override
    public Shape getShape() {

        return shape;
    }

    public double minX() {
        var rect = new RectanglePositioned((Rectangle) shape, new Position(x, y, orientation));
        var optResult = rect.corners().stream().map(Point2D::x).min(Double::compare);
        if (optResult.isPresent()) {
            return optResult.get();
        } else
        	
            return -1000.0;
    }

    public double minY() {
        var rect = new RectanglePositioned((Rectangle) shape, new Position(x, y, orientation));
        var optResult = rect.corners().stream().map(Point2D::y).min(Double::compare);
        if (optResult.isPresent()) {
            return optResult.get();
        } else
            return -1000.0;
    }

    public double maxX() {
        var rect = new RectanglePositioned((Rectangle) shape, new Position(x, y, orientation));
        var optResult = rect.corners().stream().map(Point2D::x).max(Double::compare);
        if (optResult.isPresent()) {
            return optResult.get();
        } else
            return 1000.0;
    }

    public double maxY() {
        var rect = new RectanglePositioned((Rectangle) shape, new Position(x, y, orientation));
        var optResult = rect.corners().stream().map(Point2D::y).max(Double::compare);
        if (optResult.isPresent()) {
            return optResult.get();
        } else
            return 1000.0;
    }

}