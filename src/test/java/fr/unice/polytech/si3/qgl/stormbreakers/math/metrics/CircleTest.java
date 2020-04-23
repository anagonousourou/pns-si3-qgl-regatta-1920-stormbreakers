package fr.unice.polytech.si3.qgl.stormbreakers.math.metrics;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Line2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CircleTest {

    private Circle circle;
    private Circle c1;
    @BeforeEach
    void setUp() {
        circle = new Circle(10);
    	c1 = new Circle(50);
    }

    @Test
    void testIsInsideWhenTrue() {
        assertTrue(circle.isPtInside(new Point2D(0,0)));
    }

    @Test
    void testIsInsideWhenAtBorder() {
        assertTrue(circle.isPtInside(new Point2D(10,0)));
    }

    @Test
    void testIsInsideWhenSlightlyOut() {
        assertFalse(circle.isPtInside(new Point2D(10,0.01)));
    }

    @Test
    void testIsInsideWhenFalse() {
        assertFalse(circle.isPtInside(new Point2D(10,10)));
    }

    /*
     * Tests for equals
     */

    @Test void testEqualsWhenWrongObject() {
        Circle circle = new Circle(0);
        Integer other = 0;
        assertNotEquals(circle,other);
    }

    @Test void testEqualsWhenNullObject() {
        Circle circle = new Circle(0);
        Fraction other = null;
        assertNotEquals(circle,other);
    }

    @Test void testEqualsWhenSameObject() {
        Circle circle = new Circle(0);
        assertEquals(circle,circle);
    }

    @Test void testEqualsWhenSameValues() {
        Circle circ1 = new Circle(0);
        Circle circ2 = new Circle(0);
        assertEquals(circ1,circ2);
    }

    @Test void testEqualsWhenDifferentOrientation() {
        // orientation doesn't matter for a circle
        Circle circ1 = new Circle(0, new Position(0,0,Math.PI));
        Circle circ2 = new Circle(0, new Position(0,0,0.0));
        assertEquals(circ1,circ2);
    }

    @Test void testEqualsWhenDifferent() {
        Circle circ1 = new Circle(0);
        Circle circ2 = new Circle(10);
        assertNotEquals(circ1,circ2);
    }

    /*
     * End of tests for equals
     */

    @Test void testIntersectSegment() { // non boolean methods
    	Position d1 = new Position(-90, 20);
    	Position a1 = new Position(90, 20);
    	Position d2 = new Position(20, -60);
    	Position dIn = new Position(40,40);
    	Position aIn = new Position(-10, -10);
    	Position dEdge = new Position(-50, 10);
    	Position aEdge = new Position(-50, -50);
    	Position dEdge2 = new Position(-50, -10);
    	Position A6 = new Position(-80,60);
    	Position B6 = new Position(80,60);
    	Position A7 = new Position(60,60);
    	Position B7 = new Position(70,70);
    	
    	LineSegment2D l = new LineSegment2D(d1,a1); // long segment across
    	LineSegment2D l1 = new LineSegment2D(d2,a1); // Non intersecting line
    	LineSegment2D lInCircle = new LineSegment2D(dIn, aIn); // Half way in (intersects)
    	LineSegment2D lEdgeCircle = new LineSegment2D(dEdge, aEdge); // Tangent
    	LineSegment2D lEdgeCircleNoPoint = new LineSegment2D(dEdge2, aEdge); // Tangent support only
    	LineSegment2D noIntersectWithSupport = new LineSegment2D(A6,B6); // above
        LineSegment2D onlyIntersectWithSupport = new LineSegment2D(A7,B7); // Intersecting support only

    	assertTrue(c1.intersect(l).isPresent());
    	assertTrue(c1.intersect(l1).isEmpty());
    	assertTrue(c1.intersect(lInCircle).isPresent());
    	assertTrue(c1.intersect(lEdgeCircleNoPoint).isEmpty());
    	assertTrue(c1.intersect(noIntersectWithSupport).isEmpty());
    	assertTrue(c1.intersect(onlyIntersectWithSupport).isEmpty());

        // Reminder : c1 radius 50, centered on (0,0)

        Optional<Point2D> c1XlEdgeCircleOpt = c1.intersect(lEdgeCircle);
        assertTrue(c1XlEdgeCircleOpt.isPresent());
        Point2D c1XlEdgeCircle = c1XlEdgeCircleOpt.get();
       	assertEquals(Math.abs(-50),Math.abs(c1XlEdgeCircle.x()),Utils.EPSILON_COLLISION);// absolute values because of x symmetry
        assertEquals(0,c1XlEdgeCircle.y(),Utils.EPSILON_COLLISION);

       	Point2D c1Xl = c1.intersect(l).get();
       	assertEquals(Math.abs(-45.83),Math.abs(c1Xl.x()),Utils.EPSILON_COLLISION);// absolute values because of x symmetry
        assertEquals(20,c1Xl.y(),Utils.EPSILON_COLLISION);
    }
    
    @Test void testIntersectsSegment() { // boolean methods
    	Point2D d1 = new Point2D(-70, 20);
    	Point2D a1 = new Point2D(90, -40);
    	Point2D d2 = new Point2D(-50, -60);
    	Position dEdge = new Position(-50, -20);
    	Position aEdge = new Position(-50, 20);
    	Point2D A4 = new Point2D(-41,60);
    	Point2D B4 = new Point2D(-11.5, 30);
    	Point2D A5 = new Point2D(9,11);
    	Point2D B5 = new Point2D(19,30);

    	
    	//lineSegment
    	LineSegment2D ls = new LineSegment2D(d1,a1); // intersecting long line
    	LineSegment2D ls1 = new LineSegment2D(d2,a1); // non intersecting long line
    	LineSegment2D lsEdgeCircle = new LineSegment2D(dEdge, aEdge); // tangent line
        LineSegment2D overlapping = new LineSegment2D(A4,B4); // short overlapping line
        LineSegment2D inner = new LineSegment2D(A5,B5); // short inner line
        assertTrue(c1.collidesWith(ls));
    	assertFalse(c1.collidesWith(ls1));
    	assertTrue(c1.collidesWith(lsEdgeCircle));
        assertTrue(c1.collidesWith(overlapping));
        assertTrue(c1.collidesWith(inner));
    	
    	//line 
    	Line2D l = new Line2D(d1,a1);
    	Line2D l1 = new Line2D(d2,a1);
    	Line2D tangent = new Line2D(dEdge.getPoint2D(),aEdge.getPoint2D());
    	assertTrue(c1.intersects(l));
    	assertTrue(c1.intersects(tangent));
    	assertFalse(c1.intersects(l1));
    }

    @Test void testIntersectionPointWithLine() {
        Circle circle = new Circle(50,new Position(10, 10));
        Line2D above = new Line2D(new Point2D(-1,80),new Point2D(1, 80));
        Line2D tangent = new Line2D(new Point2D(-1, 60),new Point2D(1, 60));
        Line2D overlapping = new Line2D(new Point2D(-1, 15),new Point2D(1, 15));

        assertEquals(Optional.empty(),circle.intersect(above));
        assertEquals(Optional.of(new Point2D(10,60)),circle.intersect(tangent));
        List<Point2D> possibleOutputs = List.of(
                new Point2D(59.75,15),
                new Point2D(-39.75,15)
        );
        Optional<Point2D> circleXoverlapping = circle.intersect(overlapping);
        assertTrue(circleXoverlapping.isPresent() && possibleOutputs.contains(circle.intersect(overlapping).get()));
    }
    
    @Test
    void findIntersectingPointTest() {
    	Point2D d1 = new Point2D(0, -55);
    	Point2D a1 = new Point2D(0, 55);
    	Point2D result1=new Point2D(0, 50);
    	Point2D result2=new Point2D(0, -50);
    	Line2D l = new Line2D(d1,a1);
    	
    	assertEquals(result1,c1.findBothIntersectingPoints(l).getFirst());
    	assertEquals(result2,c1.findBothIntersectingPoints(l).getSecond());

        // TODO: 23/04/2020 Find first intersecting point tests
        Line2D above = new Line2D(new Point2D(-1,80),new Point2D(1, 80));
        assertThrows(UnsupportedOperationException.class, () -> c1.findFirstIntersectingPoint(above));

        Line2D tangent = new Line2D(new Point2D(-1, 50),new Point2D(1, 50));
        assertEquals(new Point2D(0,50),c1.findFirstIntersectingPoint(tangent));

        Line2D overlappingHalfRadius = new Line2D(new Point2D(-1, 25),new Point2D(1, 25));
        assertEquals(new Point2D(50*Math.sqrt(3)/2.0,25),c1.findFirstIntersectingPoint(overlappingHalfRadius));

        Line2D overlapping = new Line2D(new Point2D(-1, 5),new Point2D(1, 5));
        List<Point2D> possibleOutputs = List.of(
                new Point2D(49.75,5),
                new Point2D(-49.75,5)
        );
        assertTrue(possibleOutputs.contains(c1.findFirstIntersectingPoint(overlapping)));



    }

    @Test
    void collidesWithTestWhenTwoCircles() {
        Circle c1 = new Circle(50,new Position(0,0));

        Circle c2 = new Circle(50,new Position(0,50)); // 1 radius apart
        Circle c3 = new Circle(50,new Position(0,100)); // 1 diameter apart
        assertTrue(c1.collidesWith(c2));
        assertTrue(c1.collidesWith(c3));

        Circle c4 = new Circle(50,new Position(0,100.2)); // >1 diameter apart
        assertFalse(c1.collidesWith(c4));
    }

    @Test void testIsInsideOpenShape() {
        Circle circle1 = new Circle(50,new Position(0,0));
        assertTrue(circle1.isInsideOpenShape(new Point2D(10,10))); // inside
        assertFalse(circle1.isInsideOpenShape(new Point2D(50,0))); // on border
        assertFalse(circle1.isInsideOpenShape(new Point2D(0,50))); // on border
        assertFalse(circle1.isInsideOpenShape(new Point2D(100,100))); // outside
    }
}