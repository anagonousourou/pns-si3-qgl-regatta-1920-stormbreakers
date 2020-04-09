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
        super(polygon.getAnchor(),getPolygonRadius(polygon));
        this.vertices = polygon.getActualVertices(polygon.getAnchor());
    }

    // TODO: 31/03/2020 Use AABB for updating display boundaries

    private static double getPolygonRadius(Polygon polygon) {
        List<Point2D> vertices = polygon.getActualVertices(polygon.getAnchor());

        double maxDiameter = 0;
        for (Point2D sommet1 : vertices) {
            for (Point2D sommet2 : vertices) {
                if (sommet1!=sommet2) {
                    double dist = sommet1.distanceTo(sommet2);
                    if (dist>maxDiameter) maxDiameter=dist;
                }
            }
        }

        return maxDiameter/2.0;
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

        new Arrow(getPosition(),getPosition().getOrientation()).draw(g,mapPoint);
    }

}
