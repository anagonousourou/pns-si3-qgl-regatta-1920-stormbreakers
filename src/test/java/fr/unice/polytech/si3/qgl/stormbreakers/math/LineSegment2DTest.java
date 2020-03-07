package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LineSegment2DTest {
    

    @Test
    public void testIntersects(){
		Point2D p1 = new Point2D(10, 10);
		Point2D p2 = new Point2D(20, 30);
		Point2D p3 = new Point2D(0, 30);
		Point2D p4 = new Point2D(40, 10);
		Point2D p5 = new Point2D(15, 20);
		Point2D p6 = new Point2D(30, 0);
		
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
        
        assertEquals(p2, edge.oppositeBoundingPoint(p1));
        assertEquals(p1, edge.oppositeBoundingPoint(p2));
        assertEquals(null, edge.oppositeBoundingPoint(p3));
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
    	
    	assertTrue(line1.parallel(new Vector(1,0)).equals(line1p));
	}
	
	@Test
	public void testIntersection(){
		LineSegment2D edge1 = new LineSegment2D(1, 1, 3, 2);
		LineSegment2D edge2 = new LineSegment2D(1, 1, 0, 4);
		assertEquals(new Point2D(1, 1), edge1.intersection(edge2).get());
		assertEquals(new Point2D(1, 1), edge2.intersection(edge1).get());
		
		LineSegment2D edge3 = new LineSegment2D(3, 2, 0, 4);
		assertEquals(new Point2D(3, 2), edge1.intersection(edge3).get());
		assertEquals(new Point2D(3, 2), edge3.intersection(edge1).get());
		assertEquals(new Point2D(0, 4), edge2.intersection(edge3).get());
		assertEquals(new Point2D(0, 4), edge3.intersection(edge2).get());
		
		LineSegment2D edge4 = new LineSegment2D(0, 0, 5, 1);
		assertTrue(edge1.intersection(edge4).isEmpty());
		assertTrue(edge2.intersection(edge4).isEmpty());
		assertTrue(edge3.intersection(edge4).isEmpty());
		
		edge1 = new LineSegment2D(1, 1, 5, 5);
		edge2 = new LineSegment2D(1, 5, 5, 1);
		assertEquals(new Point2D(3, 3), edge1.intersection(edge2).get());
		assertEquals(new Point2D(3, 3), edge2.intersection(edge1).get());
	}

	

	@Test
    public void testLineSegmentSameExtremities()
    {
    	Point2D P = new Point2D(2400, 1500);
        assertThrows(DegeneratedLine2DException.class,() -> new LineSegment2D(P,P));
    }
}