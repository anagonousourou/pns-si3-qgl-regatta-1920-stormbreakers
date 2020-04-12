package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;


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
