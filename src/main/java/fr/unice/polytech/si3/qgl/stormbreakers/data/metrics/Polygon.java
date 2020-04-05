package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Orientable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

public class Polygon extends Shape implements Orientable {

    private final double orientation;
    private List<Point2D> vertices;
    private final List<Point2D> actualVertices;
    private final Point2D fakeBarycenter;
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private List<LineSegment2D> bordersActualPos;

    enum Side {
        LEFT, RIGHT, MIDDLE
    }

    public Polygon(double orientation, List<Point2D> sommets, Position anchor) {
        super("polygon", anchor);
        this.orientation = orientation;
        double xbar = 0.0;
        double ybar = 0.0;
        for (Point2D v : sommets) {
            xbar += v.x();
            ybar += v.y();
        }
        xbar = xbar / (double) sommets.size();
        ybar = ybar / (double) sommets.size();
        this.fakeBarycenter = new Point2D(xbar, ybar);
        this.vertices = new ArrayList<>(sommets);
        this.actualVertices = getActualVertices(anchor);

        this.setUpAABB();

        this.bordersActualPos = generateBordersInActualPos(this.anchor, orientation); // Well Oriented borders

    }

    void setUpAABB() {
        for (int i = 0; i < this.actualVertices.size(); i++) {
            double x = actualVertices.get(i).x();
            double y = actualVertices.get(i).y();
            this.minX = Math.min(minX, x);
            this.maxX = Math.max(maxX, x);
            this.minY = Math.min(minY, y);
            this.maxY = Math.max(maxY, y);
        }
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
        double xbar = 0.0;
        double ybar = 0.0;
        for (Point2D v : vertices) {
            xbar += v.x();
            ybar += v.y();
        }
        xbar = xbar / (double) vertices.size();
        ybar = ybar / (double) vertices.size();
        this.fakeBarycenter = new Point2D(xbar, ybar);
        this.vertices = new ArrayList<>(vertices);
        this.actualVertices = getActualVertices(new Position(0, 0));
        this.setUpAABB();
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
        // LATER: 12/03/2020 Missing Test where bounding circles don't collide
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
        // LATER: 16/03/2020 If lineSegment inside
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
        
        if(this.isOutsideBoundingRectangle(pointToTest)){
            
            return false;
        }

        boolean flag = false;

        int n = this.actualVertices.size();
        for (int i = 0, j = n - 1; i < n; j = i++) {
            if (((actualVertices.get(i).y() > pointToTest.y()) != (actualVertices.get(j).y() > pointToTest.y()))
                    && (pointToTest.x() < ((actualVertices.get(j).x() - actualVertices.get(i).x())
                            * (pointToTest.y() - actualVertices.get(i).y()))
                            / (actualVertices.get(j).y() - actualVertices.get(i).y()) + actualVertices.get(i).x())) {
                flag = !flag;
            }

        }

        return flag;
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
        IPoint center = getAnchorPoint();
        var optfarPt = this.vertices.stream().max((a, b) -> Double.compare(center.distanceTo(a), center.distanceTo(b)));

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
        // LATER URGENT Auto-generated method stub
        return isPtInside(pt);
    }

    @Override
    public IPoint intersectionPoint(IPoint depart, IPoint arrive) {
        var optPoint = this.bordersActualPos.stream().map(segment -> segment.intersectionPoint(depart, arrive))
                .filter(Objects::nonNull).findFirst();

        if (optPoint.isPresent()) {
            return optPoint.get();
        }
        Logger.getInstance().log("returning null");
        return null;
    }

    // NEW

    public List<Point2D> getActualVertices(Position actualPos) {
        return this.vertices.stream().map(vertex -> vertex.getRotatedBy(orientation + actualPos.getOrientation())
                .getTranslatedBy(new Vector(origin, actualPos))).collect(Collectors.toList());
    }

    public boolean isOutsideBoundingRectangle(IPoint p){
        return (p.x() < this.minX || p.x() > this.maxX || p.y() < this.minY || p.y() > this.maxY);

    }
    /**
     * Expands the shape by a margin The distance between the old borders and the
     * new ones will be margin We use an homothetie
     * 
     * @param margin to expand by, if negative used as positive
     * @return new expanded shape at same position
     */
    @Override
    public Shape wrappingShape(double margin) {
        double marge = Math.abs(margin);
        double k;
        double xN;
        double yN;
        List<Point2D> extendedPoints = new ArrayList<>();
        for (Point2D vertex : this.vertices) {
            k = ((vertex.distanceTo(fakeBarycenter) + marge) / vertex.distanceTo(fakeBarycenter));
            xN = k * vertex.x() + fakeBarycenter.x() * (1 - k);
            yN = k * vertex.y() + fakeBarycenter.y() * (1 - k);
            extendedPoints.add(new Point2D(xN, yN));
        }
        return new Polygon(this.orientation, extendedPoints, this.anchor);

    }

}
