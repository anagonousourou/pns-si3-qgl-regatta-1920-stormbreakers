package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

import java.awt.*;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


public class PolygonDrawing extends Drawing {

    private List<Point2D> vertices;

    public PolygonDrawing(Polygon polygon) {
        super(getPolygonReCenteredBoundingCircle(polygon).getAnchor(),getPolygonReCenteredBoundingCircle(polygon).getRadius());
        this.vertices = polygon.getActualVertices(polygon.getAnchor());
    }

    // TODO: 31/03/2020 Use AABB for updating display boundaries

    public static Circle getPolygonReCenteredBoundingCircle(Polygon polygon) {
        List<Point2D> vertices = polygon.getActualVertices(polygon.getAnchor());

        double xMin = vertices.stream().map(v -> v.x()).min(Double::compareTo).get();
        double yMin = vertices.stream().map(v -> v.y()).min(Double::compareTo).get();
        double xMax = vertices.stream().map(v -> v.x()).max(Double::compareTo).get();
        double yMax = vertices.stream().map(v -> v.y()).max(Double::compareTo).get();

        Point2D diagMiddle = new LineSegment2D(new Point2D(xMin,yMin),new Point2D(xMax,yMax)).getMiddle();
        Position center = new Position(diagMiddle.x(),diagMiddle.y(),0);

        double radius = new Vector(xMax-xMin,yMax-yMin).scaleVector(0.5).norm();
        return new Circle(radius,center);
    }

    @Override
    public void draw(Graphics g, UnaryOperator<Point2D> mapPoint) {
        super.draw(g,mapPoint);

        List<Point2D> resizedShape = vertices.stream()
                .map(mapPoint)
                .collect(Collectors.toList());

        // Close the shape
        Point2D last = resizedShape.remove(0);
        resizedShape.add(last);

        g.setColor(getColor());
        for (Point2D vertex : resizedShape) {
            g.drawLine((int) last.x(), (int) last.y(), (int) vertex.x(), (int) vertex.y());
            last = vertex;
        }
    }

}
