package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

import java.awt.*;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


public class PolygonDrawing extends Drawing {

    private List<Point2D> vertices;

    public PolygonDrawing(Polygon polygon) {
        super(polygon.getBoundingCircle().getAnchor(),getPolygonBoundingRadius(polygon));
        this.vertices = polygon.getActualVertices(polygon.getAnchor());
    }

    private static double getPolygonBoundingRadius(Polygon polygon) {
        // TODO: 31/03/2020 Check if problem with bounding circles' radiuses
        List<Point2D> vertices = polygon.getActualVertices(polygon.getAnchor());
        if (vertices.size()<=1) return 0;

        double xMin = vertices.stream().map(v -> v.x()).min(Double::compareTo).get();
        double yMin = vertices.stream().map(v -> v.y()).min(Double::compareTo).get();
        double xMax = vertices.stream().map(v -> v.x()).max(Double::compareTo).get();
        double yMax = vertices.stream().map(v -> v.y()).max(Double::compareTo).get();
        return new Vector(xMax-xMin,yMax-yMin).scaleVector(0.5).norm();
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
