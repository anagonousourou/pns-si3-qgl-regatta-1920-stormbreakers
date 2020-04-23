package fr.unice.polytech.si3.qgl.stormbreakers.math.metrics;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Line2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

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

    @Test void testIntersectSegment() {

    	Position d1 = new Position(-90, 20);
    	Position a1 = new Position(90, 20);
    	Position d2 = new Position(20, -60);
    	Position dIn = new Position(40,40);
    	Position aIn = new Position(-10, -10);
    	Position dEdge = new Position(-50, 10);
    	Position aEdge = new Position(-50, -50);
    	Position dEdge2 = new Position(-50, -10);
    	
    	LineSegment2D l = new LineSegment2D(d1,a1);
    	LineSegment2D l1 = new LineSegment2D(d2,a1);
    	LineSegment2D lInCircle = new LineSegment2D(dIn, aIn);
    	LineSegment2D lEdgeCircle = new LineSegment2D(dEdge, aEdge);
    	LineSegment2D lEdgeCircleNoPoint = new LineSegment2D(dEdge2, aEdge);
    	
    	assertFalse(c1.intersect(l).isEmpty());
        // TODO: 23/04/2020 Checkout this test
    	//assertTrue(c1.intersect(l1).isEmpty());
    	assertFalse(c1.intersect(lInCircle).isEmpty());
    	assertTrue(c1.intersect(lEdgeCircleNoPoint).isEmpty());
    	
       	Point2D c1XlEdgeCircle = c1.intersect(lEdgeCircle).get();
        assertTrue(Utils.almostEquals(Math.abs(c1XlEdgeCircle.x()),Math.abs(-50),Utils.EPSILON_COLLISION)); // absolute values because of x symmetry
        assertTrue(Utils.almostEquals(c1XlEdgeCircle.y(),(0),Utils.EPSILON_COLLISION));
        

       	Point2D c1Xl = c1.intersect(l).get();
        assertTrue(Utils.almostEquals(Math.abs(c1Xl.x()),Math.abs(-45.83),Utils.EPSILON_COLLISION)); // absolute values because of x symmetry
        assertTrue(Utils.almostEquals(c1Xl.y(),(20),Utils.EPSILON_COLLISION));
       	
    	
    }
    
    @Test void testIntersectsSegment() {

    	Point2D d1 = new Point2D(-70, 20);
    	Point2D a1 = new Point2D(90, -40);
    	Point2D d2 = new Point2D(-50, -60);
    	Position dEdge = new Position(-50, -20);
    	Position aEdge = new Position(-50, 20);
    	
    	//lineSegment
    	LineSegment2D ls = new LineSegment2D(d1,a1);
    	LineSegment2D ls1 = new LineSegment2D(d2,a1);
    	LineSegment2D lsEdgeCircle = new LineSegment2D(dEdge, aEdge);
    	assertTrue(c1.collidesWith(ls));
    	assertFalse(c1.collidesWith(ls1));
    	assertTrue(c1.collidesWith(lsEdgeCircle));
    	
    	//line 
    	Line2D l = new Line2D(d1,a1);
    	Line2D l1 = new Line2D(d2,a1);
    	assertTrue(c1.intersects(l));
    	assertFalse(c1.intersects(l1));
    }
    
    @Test
    void findIntersectingPointTest() {
    	Point2D d1 = new Point2D(0, -55);
    	Point2D a1 = new Point2D(0, 55);
    	Point2D result1=new Point2D(0, 50);
    	Point2D result2=new Point2D(0, -50);
    	Line2D l = new Line2D(d1,a1);
    	
    	assertEquals(c1.findBothIntersectingPoints(l).getFirst(),result1);
    	assertEquals(c1.findBothIntersectingPoints(l).getSecond(),result2);
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

}