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
import java.util.stream.Collectors;

public class Polygon extends Shape implements CanCollide, Orientable {

    private double orientation;
    private List<Point2D> vertices;
    private List<LineSegment2D> borders;
    enum Side {
        LEFT,
        RIGHT,
        MIDDLE
    }

    @JsonCreator
    public Polygon(@JsonProperty("orientation") double orientation, @JsonProperty("vertices") List<Point2D> vertices) {
        super("polygon");
        this.orientation = orientation;
        this.vertices = new ArrayList<>(vertices);
        this.vertices.add(vertices.get(0)); // Close the hull
        this.borders = generateBorders();
    }

    /**
     * Generates shape's borders from it's verticies
     * 
     * @return List of the shape borders as LineSegment2D
     */
    private List<LineSegment2D> generateBorders() {
        List<LineSegment2D> cotes = new ArrayList<>();
        Iterator<Point2D> it = vertices.iterator();

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
        List<LineSegment2D> cotes = new ArrayList<>();
        List<Point2D> sommets = this.getVertices().stream().map(point -> point.getTranslatedBy(omegaPoint.x(), omegaPoint.y()))
                .collect(Collectors.toList());

        
         

        Iterator<Point2D> it = sommets.iterator();
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


    


    /**
     * Returns whether the given point is inside this Polygon
     * Complexity: O(N) where N is the amount of vertices
     * @param pointToTest the point for which to test whether it is inside of not
     * @return true if it is inside, false if not
     */
    @Override
    public boolean isPtInside(IPoint pointToTest) {
        // LATER: 04/03/2020 Tests avec orientation
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
            } else if (currentSide!=Side.MIDDLE && currentSide!=reqSide) {
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
     * @param a start point of border
     * @param b end point of border
     * @param T point to test
     * @return Side of the vector to which the point is
     */
    private Side getPointSideComparedToBorder(Point2D a, Point2D b, IPoint t) {
        Vector borderVector = new Vector(a,b);
        Vector toCompare = new Vector(a,t);

        double scal = borderVector.scal(toCompare);

        Side side = null;
        if (scal < 0) side=Side.LEFT;
        else if (scal > 0) side=Side.RIGHT;
        else if (Utils.almostEquals(scal, 0)) side=Side.MIDDLE;

        return side;
    }

    /**
     * Tells whether this lineSegment intersects or not with the shape borders
     * 
     * @param lineSegment2D line for which to test collision
     */
    public boolean intersectsWith(LineSegment2D lineSegment2D) {
        return this.borders.stream().anyMatch(border -> border.intersects(lineSegment2D));
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
        return "Polygon " + Logable.listToLogs(new ArrayList<>(vertices), " ", "", "");
    }

    public double getMaxRadius(){
        IPoint center=new Point2D(0,0);
        var optfarPt= this.vertices.stream().max((a,b)->Double.compare(center.distanceTo(a),center.distanceTo(a)));
        if(optfarPt.isPresent()){
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

    
}
