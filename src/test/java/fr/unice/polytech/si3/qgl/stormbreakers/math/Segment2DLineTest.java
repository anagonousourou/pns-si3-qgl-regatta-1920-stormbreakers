package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Segment2DLineTest {
    

    @Test
    public void testIntersects(){
		Point2D p1 = new Point2D(10, 10);
		Point2D p2 = new Point2D(20, 30);
		Point2D p3 = new Point2D(00, 30);
		Point2D p4 = new Point2D(40, 10);
		Point2D p5 = new Point2D(15, 20);
		Point2D p6 = new Point2D(30, 00);
		
		LineSegment2D edge1 = new LineSegment2D(p1, p2);
		LineSegment2D edge2 = new LineSegment2D(p3, p4);
		LineSegment2D edge3 = new LineSegment2D(p5, p6);
		
		assertTrue(LineSegment2D.intersects(edge1, edge2));
		assertTrue(LineSegment2D.intersects(edge2, edge1));
		assertTrue(LineSegment2D.intersects(edge1, edge3));
		assertTrue(LineSegment2D.intersects(edge3, edge1));
		assertTrue(!LineSegment2D.intersects(edge2, edge3));
		assertTrue(!LineSegment2D.intersects(edge3, edge2));
	}

	@Test
	public void testGetOtherPoint() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(40, 50);
        Point2D p3 = new Point2D(20, 30);
        LineSegment2D edge = new LineSegment2D(p1, p2);
        
        assertEquals(p2, edge.opposite(p1));    
        assertEquals(p1, edge.opposite(p2));    
        assertEquals(null, edge.opposite(p3));    
	}
	@Test
	public void testLength() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(40, 50);
        LineSegment2D edge = new LineSegment2D(p1, p2);
        assertEquals(50, edge.length(), 1e-14);    
	}

	@Test
	public void testGetParallelDouble() {
    	Point2D p1 = new Point2D(1, 1);
    	Point2D p2 = new Point2D(1, 3);
    	LineSegment2D line1 = new LineSegment2D(p1, p2);
    	
    	Point2D p1p = new Point2D(2, 1);
    	Point2D p2p = new Point2D(2, 3);
    	LineSegment2D line1p = new LineSegment2D(p1p, p2p);
    	
    	assertTrue(line1.parallel(1).equals(line1p));
	}
	
	@Test
	public void testIntersection(){
		LineSegment2D edge1 = new LineSegment2D(1, 1, 3, 2);
		LineSegment2D edge2 = new LineSegment2D(1, 1, 0, 4);
		assertTrue(edge1.intersection(edge2).equals(new Point2D(1, 1)));
		assertTrue(edge2.intersection(edge1).equals(new Point2D(1, 1)));
		
		LineSegment2D edge3 = new LineSegment2D(3, 2, 0, 4);
		assertTrue(edge1.intersection(edge3).equals(new Point2D(3, 2)));
		assertTrue(edge3.intersection(edge1).equals(new Point2D(3, 2)));
		assertTrue(edge2.intersection(edge3).equals(new Point2D(0, 4)));
		assertTrue(edge3.intersection(edge2).equals(new Point2D(0, 4)));
		
		LineSegment2D edge4 = new LineSegment2D(0, 0, 5, 1);
		assertEquals(null,edge1.intersection(edge4));
		assertEquals(edge2.intersection(edge4), null);
		assertEquals(edge3.intersection(edge4), null);
		
		edge1 = new LineSegment2D(1, 1, 5, 5);
		edge2 = new LineSegment2D(1, 5, 5, 1);
		assertTrue(edge1.intersection(edge2).equals(new Point2D(3, 3)));
		assertTrue(edge2.intersection(edge1).equals(new Point2D(3, 3)));
	}

	

	@Test
    public void testDistancePointSameExtremities()
    {
        final LineSegment2D lineA = new LineSegment2D(2400, 1500, 2400, 1500);
        double dist = lineA.distance(new Point2D(2440, 1530));
        assertEquals(50, dist, .01);
    }
}