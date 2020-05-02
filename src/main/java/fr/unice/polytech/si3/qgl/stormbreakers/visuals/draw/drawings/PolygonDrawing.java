package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.awt.geom.Path2D;
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

        // Drawn
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(getColor());


        Path2D.Double path2D = new Path2D.Double();
        Point2D firstPoint = verticesCopy.remove(0);

        path2D.moveTo(firstPoint.x(),firstPoint.y());
        for (Point2D vertex : verticesCopy) {
            path2D.lineTo(vertex.x(),vertex.y());
        }
        path2D.closePath();

        g2d.draw(path2D);
        new Arrow(getPosition(),getPosition().getOrientation()).draw(g);
    }

}
