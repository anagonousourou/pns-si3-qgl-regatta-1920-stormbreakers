package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;


public class PolygonDrawing extends Drawing {

    private final List<Point2D> vertices;

    public PolygonDrawing(Polygon polygon) {
        super(polygon.getAnchor(),getPolygonRadius(polygon));
        this.vertices = polygon.getActualVertices(polygon.getAnchor());
    }

    private static double getPolygonRadius(Polygon polygon) {
        List<Point2D> vertices = polygon.getActualVertices(polygon.getAnchor());

        double maxDiameter = 0;
        for (Point2D vertex1 : vertices) {
            for (Point2D vertex2 : vertices) {
                if (vertex1!=vertex2) {
                    double dist = vertex1.distanceTo(vertex2);
                    if (dist>maxDiameter) maxDiameter=dist;
                }
            }
        }
        return maxDiameter/2.0;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        List<Point2D> verticesCopy = new ArrayList<>(vertices);

        // Close the shape
        Point2D last = verticesCopy.remove(0);
        verticesCopy.add(last);

        // Drawn
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(getColor());

        for (Point2D vertex : verticesCopy) {
            g2.draw(new Line2D.Double(last.x(),last.y(),vertex.x(),vertex.y()));
            last = vertex;
        }

        new Arrow(getPosition(),getPosition().getOrientation()).draw(g);
    }

}
