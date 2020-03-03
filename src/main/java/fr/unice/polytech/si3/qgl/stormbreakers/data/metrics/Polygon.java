package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Orientable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Polygon extends Shape implements CanCollide, Orientable {

    private double orientation;
    private List<Point2D> vertices;
    private List<LineSegment2D> borders;

    @JsonCreator
    Polygon(@JsonProperty("orientation") double orientation,
            @JsonProperty("vertices") List<Point2D> vertices) {
        super("polygon");
        this.orientation = orientation;
        this.vertices = vertices;
        this.borders = generateBorders();
    }

    /**
     * Generates shape's borders from it's verticies
     * @return List of the shape borders as LineSegment2D
     */
    private List<LineSegment2D> generateBorders() {
        List<LineSegment2D> borders = new ArrayList<>();
        List<Point2D> vertices = new ArrayList<>(this.vertices);
        vertices.add(vertices.get(0)); // Close the hull

        Iterator<Point2D> it = vertices.iterator();
        Point2D lastPoint = null;
        if (it.hasNext()) lastPoint=it.next();
        while (it.hasNext()) {
            Point2D currentPoint = it.next();
            borders.add(new LineSegment2D(lastPoint,currentPoint));
            lastPoint = currentPoint;
        }

        return borders;
    }

    @Override
    public boolean isPtInside(Point2D pt) {
        // TODO: 03/03/2020 Implement with 360° check
        return false;
    }

    /**
     * Tells whether this lineSegment intersects or not with the shape borders
     * @param lineSegment2D line for which to test collision
     */
    public boolean intersectsWith(LineSegment2D lineSegment2D) {
        boolean doesIntersect = false;
        for (LineSegment2D border : borders) {
            if (border.intersects(lineSegment2D)) {
                doesIntersect = true;
                break;
            }
        }
        return doesIntersect;
    }

    List<LineSegment2D> getHull() {
        return borders;
    }

    @JsonProperty("vertices")
    public List<Point2D> getVertices() {
        return new ArrayList<>(vertices);
    }

    @JsonProperty("orientation")
    public double getOrientation() {
        return orientation;
    }


    @Override
    public String toLogs() {
        return "Polygon " + Logable.listToLogs(new ArrayList<>(vertices)," ","","");
    }
}
