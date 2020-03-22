package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Orientable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Polygon extends Shape implements Orientable {

    private double orientation;
    private List<Point2D> vertices;
    private List<LineSegment2D> borders;
    private List<LineSegment2D> bordersActualPos;

    enum Side {
        LEFT, RIGHT, MIDDLE
    }

    public Polygon(double orientation, List<Point2D> vertices, Position anchor) {
        super("polygon", anchor);
        this.orientation = orientation;
        this.vertices = new ArrayList<>(vertices);
        this.bordersActualPos = generateBordersInActualPos(this.anchor, orientation); // Well Oriented borders
        this.borders = generateBorders(vertices);
    }

    /**
     * Changes the shape's anchor to a given one This method recomputes the
     * polygons's hull
     */
    @Override
    public void setAnchor(Position newAnchor) {
        super.setAnchor(newAnchor);
        // Compute actual Borders
        this.bordersActualPos = generateBordersInActualPos(getAnchor(), orientation);
    }

    @JsonCreator
    public Polygon(@JsonProperty("orientation") double orientation, @JsonProperty("vertices") List<Point2D> vertices) {
        super("polygon");
        this.orientation = orientation;
        this.vertices = new ArrayList<>(vertices);
        this.bordersActualPos = generateBordersInActualPos(getAnchor(), orientation); // Well Oriented borders
        this.borders = generateBorders(vertices);
    }

    /**
     * Generates shape's borders from given vertices
     * 
     * @return List of the shape borders as LineSegment2D
     */
    private List<LineSegment2D> generateBorders(List<Point2D> vertices) {
        List<LineSegment2D> cotes = new ArrayList<>();
        List<Point2D> points = new ArrayList<>(vertices);
        points.add(vertices.get(0)); // Close the hull

        Iterator<Point2D> it = points.iterator();

        Point2D lastPoint = null;
        if (it.hasNext())
            lastPoint = it.next();
        while (it.hasNext()) {
            Point2D currentPoint = it.next();
            cotes.add(new LineSegment2D(lastPoint, currentPoint));
            lastPoint = currentPoint;
        }

        return cotes;
    }

    public List<LineSegment2D> generateBordersInThePlan(IPoint omegaPoint) {
        List<Point2D> sommets = this.getVertices().stream()
                .map(point -> point.getTranslatedBy(omegaPoint.x(), omegaPoint.y())).collect(Collectors.toList());

        return generateBorders(sommets);
    }

    private List<LineSegment2D> generateBordersInActualPos(Position actualPos, double orientation) {
        // On prend en compte la rotation du du polygone
        double totalOrientation = orientation + actualPos.getOrientation();
        if (totalOrientation == 0) {
            return generateBordersInThePlan(actualPos);
        } else {
            // Polygon is drawn with total orientation
            List<Point2D> vertices = this.vertices.stream()
                    .map(vertex -> vertex.getRotatedBy(totalOrientation).getTranslatedBy(new Vector(origin, actualPos)))
                    .collect(Collectors.toList());
            return generateBorders(vertices);
        }
    }

    // =========
    // Methods for all shapes

    public Circle getBoundingCircle() {
        return new Circle(getMaxRadius(), getAnchor());
    }

    @Override
    public boolean collidesWith(Shape shape) {
        return shape.collidesWith(this); // collide w/ polygon
    }

    @Override
    public boolean collidesWith(Polygon polygon) {
        // TODO: 12/03/2020 Missing Test where bounding circles don't collide
        if (!this.getBoundingCircle().collidesWith(polygon.getBoundingCircle())) {
            // Bounding circles don't even intersect
            return false;
        }

        List<LineSegment2D> edges = bordersActualPos;

        for (LineSegment2D border : edges) {
            if (polygon.collidesWith(border))
                return true;
        }
        return false;
    }

    @Override
    public boolean collidesWith(Circle circle) {
        List<LineSegment2D> edges = bordersActualPos;

        for (LineSegment2D border : edges) {
            if (circle.collidesWith(border)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean collidesWith(LineSegment2D lineSegment2D) {
        // TODO: 16/03/2020 If lineSegment inside
        List<LineSegment2D> edges = bordersActualPos;

        for (LineSegment2D border : edges) {
            if (border.intersects(lineSegment2D))
                return true;
        }
        return false;
    }

    // =========

    /**
     * Returns whether the given point is inside this Polygon Complexity: O(N) where
     * N is the amount of vertices
     * 
     * @param pointToTest the point for which to test whether it is inside of not
     * @return true if it is inside, false if not
     */
    @Override
    public boolean isPtInside(IPoint pointToTest) {
        // On prend en compte la rotation du polygone
        Iterator<LineSegment2D> it = bordersActualPos.iterator();
        Side lastSide = null;

        while (it.hasNext()) {
            LineSegment2D currentBorder = it.next();

            Side currentSide = getPointSideComparedToVector(currentBorder.firstPoint(), currentBorder.lastPoint(),
                    pointToTest);

            if (currentSide != Side.MIDDLE && lastSide == null) {
                // Side of point compared to one edge
                lastSide = currentSide;
            } else if (currentSide != Side.MIDDLE && currentSide != lastSide) {
                // If the point changes side when cycling through borders
                // The point is outside the CONVEX polygon
                return false;
            }
        }

        // If we reach here then the point is inside
        return true;
    }

    /**
     * Returns for a given vector AB the side to which the tested point T is NB:
     * Left and Right are determined by considering the Vector facing Front
     * 
     * @param a start point of border
     * @param b end point of border
     * @param t point to test
     * @return Side of the vector to which the point is
     */

    private Side getPointSideComparedToVector(Point2D a, Point2D b, IPoint t) {

        Vector borderVector = new Vector(a, b);
        Vector toCompare = new Vector(a, t);

        double scal = borderVector.scal(toCompare);

        Side side = null;
        if (Utils.almostEquals(scal, 0))
            side = Side.MIDDLE;
        else if (scal < 0)
            side = Side.LEFT;
        else if (scal > 0)
            side = Side.RIGHT;

        return side;
    }

    List<LineSegment2D> getHull() {
        return bordersActualPos;
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
        return "Polygon " + Logable.listToLogs(new ArrayList<>(vertices), " ", "", "");
    }

    @Override
    public String toString() {
        System.out.println(anchor);
        return "Polygon " + generateBordersInThePlan(anchor);
    }

    public double getMaxRadius() {
        IPoint center = getAnchorPoint();
        var optfarPt = this.vertices.stream().max((a, b) -> Double.compare(center.distanceTo(a), center.distanceTo(b))); // Changed
                                                                                                                         // second
                                                                                                                         // a
                                                                                                                         // to
                                                                                                                         // b
        if (optfarPt.isPresent()) {
            return center.distanceTo(optfarPt.get());
        }
        return 0.0;
    }

    @Override
    public ShapeType getTypeEnum() {
        return ShapeType.POLYGON;
    }

    @Override
    public boolean isInsideOpenShape(IPoint pt) {
        // TODO Auto-generated method stub
        return isPtInside(pt);
    }

    @Override
    public IPoint intersectionPoint(IPoint depart, IPoint arrive) {
        var optPoint = this.borders.stream().map(segment -> segment.intersectionPoint(depart, arrive))
                .filter(Objects::nonNull).findFirst();

        if (optPoint.isPresent()) {
            return optPoint.get();
        }
        System.out.println("returning null");
        return null;
    }

}
