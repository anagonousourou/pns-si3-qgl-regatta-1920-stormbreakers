package fr.unice.polytech.si3.qgl.stormbreakers.math.metrics;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.io.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.*;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

public class Polygon extends Shape implements Orientable {

    private double orientation;
    private List<Point2D> vertices;

    private List<LineSegment2D> bordersActualPos;

    enum Side {
        LEFT, RIGHT, MIDDLE
    }

    public Polygon(double orientation, List<Point2D> vertices, Position anchor) {
        super("polygon", anchor);
        this.orientation = orientation;
        this.vertices = new ArrayList<>(vertices);
        this.bordersActualPos = generateBordersInActualPos(this.anchor, orientation); // Well Oriented borders

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
            List<Point2D> sommets = getActualVertices(actualPos);
            return generateBorders(sommets);
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

        double scal = Vector.crossZ(borderVector, toCompare);

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

        return "Polygon " + bordersActualPos;
    }

    public double getMaxRadius() {
        IPoint center = new Point2D(0,0);
        var optfarPt = this.vertices.stream().max(Comparator.comparingDouble(center::distanceTo));

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
        // TODO Test
        return isPtInside(pt);
    }

    @Override
    public IPoint intersectionPoint(IPoint depart, IPoint arrive) {
        var optPoint = this.bordersActualPos.stream().map(segment -> segment.intersectionPoint(depart, arrive))
                .filter(Objects::nonNull).findFirst();

        if (optPoint.isPresent()) {
            return optPoint.get();
        }
        // TODO: 23/04/2020 Test this last part
        Logger.getInstance().log("returning null");
        return null;
    }

    // NEW

    public List<Point2D> getActualVertices(Position actualPos) {
        return this.vertices.stream().map(vertex -> vertex.getRotatedBy(orientation + actualPos.getOrientation())
                .getTranslatedBy(new Vector(origin, actualPos))).collect(Collectors.toList());
    }

    /**
     * Checks if the shape's vertices are stored in clockwise order It's when by
     * iteration over the vertices you go clockwise around the shape's surface
     * 
     * @return true if clockwise, false if counterclockwise
     */
    private boolean isClockWise() {
        List<Point2D> sommets = getVertices();
        int isCcw = 0;
        for (int ct = 0; ct < sommets.size() - 2 && isCcw == 0; ct++) {
            isCcw = IPoint.ccw(sommets.get(ct), sommets.get(ct + 1), sommets.get(ct + 2));
        }

        return isCcw != 1;
    }

    /**
     * Given any shape's segment line AB and whether the shape is or not clockwise
     * it computes a new Line parallel to the given segment and outside the original
     * shape at a given distance from the original shape
     * 
     * @param isPolygonClockwise whether the polygon is or not clockwise
     * @param a                  original segment's first point
     * @param b                  original segment's last point
     * @param distance           distance beween old and new segment line
     * @return the extracted parallel line
     */
    private Line2D extractParallelLine(boolean isPolygonClockwise, Point2D a, Point2D b, double distance) {
        Vector abVector = new Vector(a, b);
        Vector normal = abVector.normalize();
        // We expand out of the polygon
        if (isPolygonClockwise) {
            normal = normal.rotateBy90CCW();
        } else {
            normal = normal.rotateBy90CW();
        }
        normal = normal.scaleVector(distance); // We expand out by distance

        Point2D translatedA = a.getTranslatedBy(normal);
        Point2D translatedB = b.getTranslatedBy(normal);

        return new Line2D(translatedA, translatedB);
    }

    /**
     * Expands the shape by a margin The distance between the old borders and the
     * new ones will be margin
     * 
     * @param margin to expand by, if negative used as positive
     * @return new expanded shape at same position
     */
    @Override
    public Shape wrappingShape(double margin) {
        margin = Math.abs(margin); // Expansion needs positive values

        boolean isPolygonCW = this.isClockWise();
        List<Point2D> oldVertices = getVertices();
        oldVertices.add(oldVertices.get(0)); // Need line between last and first vertex

        List<Line2D> expandedLines = new ArrayList<>();
        for (int ct = 0; ct < oldVertices.size() - 1; ct++) {
            expandedLines.add(extractParallelLine(isPolygonCW, oldVertices.get(ct), oldVertices.get(ct + 1), margin));
        }
        expandedLines.add(expandedLines.get(0)); // Need collision between last and first line

        List<Point2D> expandedVertices = new ArrayList<>();
        for (int ct = 0; ct < expandedLines.size() - 1; ct++) {
            Optional<Point2D> optExpandedVertex = (expandedLines.get(ct)).intersect(expandedLines.get(ct + 1));
            if (optExpandedVertex.isEmpty())
                // TODO: 23/04/2020 Test with wrong polygon
                throw new UnsupportedOperationException("Can't find expansion point with lines :"
                        + expandedLines.get(ct) + " and " + expandedLines.get(ct + 1));
            expandedVertices.add(optExpandedVertex.get());
        }

        return new Polygon(orientation, expandedVertices, getAnchor());
    }

}