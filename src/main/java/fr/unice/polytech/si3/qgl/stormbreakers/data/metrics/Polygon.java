package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Orientable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

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
        this.vertices = new ArrayList<>(vertices);
        this.vertices.add(vertices.get(0)); // Close the hull
        this.borders = generateBorders();
    }

    /**
     * Generates shape's borders from it's verticies
     * @return List of the shape borders as LineSegment2D
     */
    private List<LineSegment2D> generateBorders() {
        List<LineSegment2D> borders = new ArrayList<>();
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


    enum Side {
        Left,
        Right,
        Middle
    }


    /**
     * Returns whether the given point is inside this Polygon
     * Complexity: O(N) where N is the amount of vertices
     * @param pointToTest the point for which to test whether it is inside of not
     * @return true if it is inside, false if not
     */
    @Override
    public boolean isPtInside(Point2D pointToTest) {
        // TODO: 04/03/2020 Tests avec orientation
        Iterator<Point2D> it = vertices.iterator();
        Point2D lastPoint = null;

        Side reqSide = null;

        if (it.hasNext()) lastPoint = it.next();
        while (it.hasNext()) {
            Point2D currentPoint = it.next();

            Side currentSide = getPointSideComparedToBorder(lastPoint,currentPoint,pointToTest);

            if (reqSide==null) {
                // Side of point compared to first border
                reqSide = currentSide;
            } else if (currentSide!=Side.Middle && currentSide!=reqSide) {
                // If the point changes side when cycling through borders
                // The point is outside the CONVEX polygon
                return false;
            }

            lastPoint=currentPoint;
        }

        // If we reach here then the point is inside
        return true;
    }

    /**
     * Returns for a given vector AB the side to which the tested point T is
     * NB: Left and Right are determined by considering the Vector facing Front
     * @param A start point of border
     * @param B end point of border
     * @param T point to test
     * @return Side of the vector to which the point is
     */
    private Side getPointSideComparedToBorder(Point2D A, Point2D B, Point2D T) {
        Vector borderVector = new Vector(A,B);
        Vector toCompare = new Vector(A,T);

        double scal = borderVector.scal(toCompare);

        Side side = null;
        if (scal < 0) side=Side.Left;
        else if (scal > 0) side=Side.Right;
        else if (scal == 0) side=Side.Middle;

        return side;
    }

    /**
     * Tells whether the given lineSegment intersects or not with the shape borders
     * Complexity: O(N*ComplexityOf(segment.intersect(Segment)))
     *      where N is the amount of vertices
     * @param lineSegment2D line for which to test collision
     */
    public boolean intersectsWith(LineSegment2D lineSegment2D) {
        // TODO: 04/03/2020 Tests avec orientation
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
