package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;

import javax.sound.sampled.Line;

public class LineSegment2DTest {
	
	Double delta = Math.pow(10, -3);

	@Test
	void testCannotCreateLineSegmentOfVectorZeroDirection() {
		assertThrows(DegeneratedLine2DException.class, () -> new LineSegment2D(new Point2D(0,0), new Point2D(0,0)));
		assertThrows(DegeneratedLine2DException.class, () -> new LineSegment2D(new Point2D(5,5), new Point2D(5,5)));
		assertDoesNotThrow(() -> new LineSegment2D(new Point2D(0,0), new Point2D(5,5)));
	}

	@Test
	public void testLineSegmentSameExtremities()
	{
		Point2D P = new Point2D(2400, 1500);
		assertThrows(DegeneratedLine2DException.class,() -> new LineSegment2D(P,P));
	}

    @Test
    public void testIntersects(){
		Point2D p1 = new Point2D(10, 10);
		Point2D p2 = new Point2D(20, 30);
		Point2D p3 = new Point2D(0, 30);
		Point2D p4 = new Point2D(40, 10);
		Point2D p5 = new Point2D(15, 20);
		Point2D p6 = new Point2D(30, 0);
		Point2D p7 = new Point2D(20,0);
		Point2D p8 = new Point2D(20,19);
		
		LineSegment2D edge1 = new LineSegment2D(p1, p2);
		LineSegment2D edge2 = new LineSegment2D(p3, p4);
		LineSegment2D edge3 = new LineSegment2D(p5, p6);
		LineSegment2D edge4 = new LineSegment2D(p7, p8);
		LineSegment2D edge5 = new LineSegment2D(p5, p2);

		// orthogonal intersecting
		assertTrue(LineSegment2D.intersects(edge1, edge2));
		assertTrue(LineSegment2D.intersects(edge2, edge1));

		// at touching limit
		assertTrue(LineSegment2D.intersects(edge1, edge3));
		assertTrue(LineSegment2D.intersects(edge3, edge1));

		// collinear points
		assertTrue(LineSegment2D.intersects(edge1, edge5));
		assertTrue(LineSegment2D.intersects(edge5, edge1));

		// Same line segment
		assertTrue(LineSegment2D.intersects(edge1, edge1));

		// Non parrallel non touching lines
        assertFalse(LineSegment2D.intersects(edge2, edge3));
        assertFalse(LineSegment2D.intersects(edge3, edge2));

        // Almost touching
        assertFalse(LineSegment2D.intersects(edge2, edge4));
		assertFalse(LineSegment2D.intersects(edge2, edge4));
	}

	@Test
	public void testGetOtherPoint() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(40, 50);
        Point2D p3 = new Point2D(20, 30);
        LineSegment2D edge = new LineSegment2D(p1, p2);
        
        assertEquals(p2, edge.oppositeBoundingPoint(p1));
        assertEquals(p1, edge.oppositeBoundingPoint(p2));
        assertNull(edge.oppositeBoundingPoint(p3));
	}
	@Test
	public void testLength() {
        Point2D p1 = new Point2D(10, 10);
        Point2D p2 = new Point2D(40, 50);
        LineSegment2D edge = new LineSegment2D(p1, p2);
        assertEquals(50, edge.length(), 1e-14);

		Point2D p3 = new Point2D(0, -20);
		Point2D p4 = new Point2D(-23.69, 0);
		LineSegment2D edge2 = new LineSegment2D(p3, p4);
		assertEquals(31, edge2.length(), 5e-3);
	}

	@Test
	public void testGetParallelDouble() {
    	Point2D p1 = new Point2D(1, 1);
    	Point2D p2 = new Point2D(1, 3);
    	LineSegment2D line1 = new LineSegment2D(p1, p2);
    	
    	Point2D p1p = new Point2D(2, 1);
    	Point2D p2p = new Point2D(2, 3);
    	LineSegment2D line1p = new LineSegment2D(p1p, p2p);

		assertEquals(line1p,line1.parallel(new Vector(1, 0)));

		Point2D p1pV2 = new Point2D(-4, -1);
		Point2D p2pV2 = new Point2D(-4, 1);
		LineSegment2D line2p = new LineSegment2D(p1pV2, p2pV2);

		assertEquals(line2p,line1.parallel(new Vector(-5,-2)));
	}
	
	@Test
	public void testIntersection(){
		// Segments with common first end
		LineSegment2D edge1 = new LineSegment2D(1, 1, 3, 2);
		LineSegment2D edge2 = new LineSegment2D(1, 1, 0, 4);

		Optional<Point2D> intersection12Opt = edge1.intersection(edge2);
		Optional<Point2D> intersection21Opt = edge1.intersection(edge2);
		assertTrue(intersection12Opt.isPresent());
		assertEquals(new Point2D(1, 1), intersection12Opt.get());
		assertTrue(intersection21Opt.isPresent());
		assertEquals(new Point2D(1, 1), intersection21Opt.get());

		// Segments with connected ends
		LineSegment2D edge3 = new LineSegment2D(3, 2, 0, 4);
		Optional<Point2D> intersection13Opt = edge1.intersection(edge3);
		Optional<Point2D> intersection31Opt = edge3.intersection(edge1);
		assertTrue(intersection13Opt.isPresent());
		assertEquals(new Point2D(3, 2), intersection13Opt.get());
		assertTrue(intersection31Opt.isPresent());
		assertEquals(new Point2D(3, 2), intersection31Opt.get());

		// Segments with common last end
		Optional<Point2D> intersection23Opt = edge2.intersection(edge3);
		Optional<Point2D> intersection32Opt = edge3.intersection(edge2);
		assertTrue(intersection23Opt.isPresent());
		assertEquals(new Point2D(0, 4), intersection23Opt.get());
		assertTrue(intersection32Opt.isPresent());
		assertEquals(new Point2D(0, 4), intersection32Opt.get());

		// Segment which doesn't intersect with previous ones
		LineSegment2D edge4 = new LineSegment2D(0, 0, 5, 1);
		assertTrue(edge1.intersection(edge4).isEmpty());
		assertTrue(edge2.intersection(edge4).isEmpty());
		assertTrue(edge3.intersection(edge4).isEmpty());


		// Non parallel intersection segments
		LineSegment2D edge5 = new LineSegment2D(1, 1, 5, 5);
		LineSegment2D edge6 = new LineSegment2D(1, 5, 5, 1);
		Optional<Point2D> intersection56Opt = edge5.intersection(edge6);
		Optional<Point2D> intersection65Opt = edge6.intersection(edge5);
		assertTrue(intersection56Opt.isPresent());
		assertEquals(new Point2D(3, 3), intersection56Opt.get());
		assertTrue(intersection65Opt.isPresent());
		assertEquals(new Point2D(3, 3), intersection65Opt.get());


		// Parallel segments (not) intersecting
		LineSegment2D baseSegment = new LineSegment2D(new Point2D(4,4),new Point2D(4,8));
		LineSegment2D parallelSegment1 = new LineSegment2D(new Point2D(10,4), new Point2D(10,8)); // Parallel line
		LineSegment2D parallelSegment2 = new LineSegment2D(new Point2D(4,10),new Point2D(4,20)); // Same line
		assertTrue(baseSegment.intersection(parallelSegment1).isEmpty());
		assertTrue(baseSegment.intersection(parallelSegment2).isEmpty());
		assertTrue(parallelSegment1.intersection(parallelSegment2).isEmpty());
	}
	
    @Test 
    void distanceTest() {
    	LineSegment2D line = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
    	Point2D p= new Point2D(12,12);
    	Point2D p2= new Point2D(18,6);
    	assertEquals(6.36,line.distance(p),1e-2);
    	assertEquals(8.06,line.distance(p2),1e-2);

    	// Closest to edges
		LineSegment2D line2 = new LineSegment2D(new Point2D(1,5),new Point2D(10,8));
		Point2D p3= new Point2D(-20,20);
		Point2D p4= new Point2D(22,0);
		assertEquals(25.81,line2.distance(p3),1e-2);
		assertEquals(14.42,line2.distance(p4),1e-2);
	}
    
    @Test 
    void closestPointToTest() {
    	LineSegment2D line = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
    	// Closest to endpoint
    	Point2D p= new Point2D(12,12);
    	assertEquals(7.5,line.closestPointTo(p).x(),Math.pow(10, -2));
    	assertEquals(7.5,line.closestPointTo(p).y(),Math.pow(10, -2));

    	// Closest to an inner point
    	Point2D p2= new Point2D(18,6);
    	assertEquals(10,line.closestPointTo(p2).x(),Math.pow(10, -2));
    	assertEquals(5,line.closestPointTo(p2).y(),Math.pow(10, -2));

    	// Closest to startpoint
		Point2D p3= new Point2D(5,15);
		assertEquals(5,line.closestPointTo(p3).x(),Math.pow(10, -2));
		assertEquals(10,line.closestPointTo(p3).y(),Math.pow(10, -2));

    }

	@Test
	void getMiddleTest() {
		LineSegment2D line = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
		Point2D point = new Point2D(7.5,7.5);
		assertEquals(line.getMiddle(), point);

		// startpoint = origin

		LineSegment2D lineSegmentA = new LineSegment2D(new Point2D(0,0), new Point2D(1,0));
		assertEquals(new Point2D(0.5,0), lineSegmentA.getMiddle());

		LineSegment2D lineSegmentB = new LineSegment2D(new Point2D(0,0), new Point2D(0,1));
		assertEquals(new Point2D(0,0.5), lineSegmentB.getMiddle());

		LineSegment2D lineSegmentC = new LineSegment2D(new Point2D(0,0), new Point2D(1,1));
		assertEquals(new Point2D(0.5,0.5), lineSegmentC.getMiddle());

		// Off-centered

		LineSegment2D lineSegmentAOffcentered = new LineSegment2D(new Point2D(4,4), new Point2D(5,4));
		assertEquals(new Point2D(4.5,4), lineSegmentAOffcentered.getMiddle());

		LineSegment2D lineSegmentBOffcentered = new LineSegment2D(new Point2D(4,4), new Point2D(4,5));
		assertEquals(new Point2D(4,4.5), lineSegmentBOffcentered.getMiddle());

		LineSegment2D lineSegmentCOffcentered = new LineSegment2D(new Point2D(4,4), new Point2D(5,5));
		assertEquals(new Point2D(4.5,4.5), lineSegmentCOffcentered.getMiddle());

		LineSegment2D lineSegmentDOffcentered = new LineSegment2D(new Point2D(11,11), new Point2D(7,7));
		assertEquals(new Point2D(9,9), lineSegmentDOffcentered.getMiddle());

		LineSegment2D lineSegmentEOffcentered = new LineSegment2D(new Point2D(-3,-5), new Point2D(8,7));
		assertEquals(new Point2D(2.5,1), lineSegmentEOffcentered.getMiddle());


	}
    
    @Test 
    void isCollinearPointOnSegmentTest() {
    	LineSegment2D line = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
    	Point2D p= new Point2D(12,12);
    	Point2D p2 = new Point2D(7.5,7.5);
    	Point2D p3 = new Point2D(15,0);
    	assertFalse(line.isCollinearPointOnSegment(p));
    	assertTrue(line.isCollinearPointOnSegment(p2));
    	assertFalse(line.isCollinearPointOnSegment(p3));
    }

	@Test
	void segmentParameterOfTest() {
		Point2D A = new Point2D(42,9);
		Point2D B = new Point2D(6,66);
		LineSegment2D nonVerticalLineSegment = new LineSegment2D(A,B);
		assertEquals(0,nonVerticalLineSegment.segmentParameterOf(A),Utils.EPSILON);
		assertEquals(0.5,nonVerticalLineSegment.segmentParameterOf(nonVerticalLineSegment.getMiddle()),Utils.EPSILON);
		assertEquals(1,nonVerticalLineSegment.segmentParameterOf(B),Utils.EPSILON);

		Point2D C = new Point2D(6,42);
		Point2D D = new Point2D(6,0);
		LineSegment2D verticalLineSegment = new LineSegment2D(C,D);
		assertEquals(0,verticalLineSegment.segmentParameterOf(C),Utils.EPSILON);
		assertEquals(0.5,verticalLineSegment.segmentParameterOf(verticalLineSegment.getMiddle()),Utils.EPSILON);
		assertEquals(1,verticalLineSegment.segmentParameterOf(D),Utils.EPSILON);
	}

	@Test
	void pointFromSegmentParameterTest() {
		Point2D A = new Point2D(42,9);
		Point2D B = new Point2D(6,66);
		LineSegment2D nonVerticalLineSegment = new LineSegment2D(A,B);
		assertTrue(Utils.almostEquals(A, nonVerticalLineSegment.point(0), delta));
		Point2D tmp1 = nonVerticalLineSegment.getMiddle();
		Point2D tmp2 = nonVerticalLineSegment.point(0.5);
		assertTrue(Utils.almostEquals(tmp1, tmp2, delta));
		assertTrue(Utils.almostEquals(B,nonVerticalLineSegment.point(1), delta));

		Point2D C = new Point2D(6,42);
		Point2D D = new Point2D(6,0);
		LineSegment2D verticalLineSegment = new LineSegment2D(C,D);
		assertTrue(Utils.almostEquals(C,verticalLineSegment.point(0), delta));
		assertTrue(Utils.almostEquals(verticalLineSegment.getMiddle(),verticalLineSegment.point(0.5), delta));
		assertTrue(Utils.almostEquals(D,verticalLineSegment.point(1), delta));
	}

	@Test
	void testSegmentParameterReciprocity() {
		LineSegment2D lineSegment2D = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
		double step = 0.25;
		for (double k = 0; k<=1; k+=step){
			assertEquals(k, lineSegment2D.segmentParameterOf(lineSegment2D.point(k)),Utils.EPSILON);
		}
		for (double n=0; n<=1; n+=step){
			Point2D point2D = lineSegment2D.point(n);
			assertTrue(Utils.almostEquals(point2D, lineSegment2D.point(lineSegment2D.segmentParameterOf(point2D)), delta));
		}
	}

	@Test
	void containsPointTest() {
		LineSegment2D lineSegmentHorizontal = new LineSegment2D(new Point2D(-5,3),new Point2D(5,3));
		assertTrue(lineSegmentHorizontal.contains(new Point2D(0,3)));
		assertFalse(lineSegmentHorizontal.contains(new Point2D(0,2.8)));
		assertFalse(lineSegmentHorizontal.contains(new Point2D(0,3.2)));
		assertTrue(lineSegmentHorizontal.contains(new Point2D(-5,3)));
		assertFalse(lineSegmentHorizontal.contains(new Point2D(-5.2,3)));
		assertTrue(lineSegmentHorizontal.contains(new Point2D(5,3)));
		assertFalse(lineSegmentHorizontal.contains(new Point2D(5.2,3)));

		LineSegment2D lineSegmentOblique = new LineSegment2D(new Point2D(-5,-5),new Point2D(5,5));
		assertTrue(lineSegmentOblique.contains(new Point2D(0,0)));
		assertFalse(lineSegmentOblique.contains(new Point2D(0,0.2)));
		assertFalse(lineSegmentOblique.contains(new Point2D(0,-0.2)));
		assertFalse(lineSegmentOblique.contains(new Point2D(0.2,0)));
		assertFalse(lineSegmentOblique.contains(new Point2D(-0.2,0)));
		assertTrue(lineSegmentOblique.contains(new Point2D(-5,-5)));
		assertFalse(lineSegmentOblique.contains(new Point2D(-5.2,-5.2)));
		assertTrue(lineSegmentOblique.contains(new Point2D(5,5)));
		assertFalse(lineSegmentOblique.contains(new Point2D(5.2,5.2)));

		LineSegment2D lineSegmentVertical = new LineSegment2D(new Point2D(3,-5),new Point2D(3,5));
		assertTrue(lineSegmentVertical.contains(new Point2D(3,0)));
		assertFalse(lineSegmentVertical.contains(new Point2D(2.8,0)));
		assertFalse(lineSegmentVertical.contains(new Point2D(3.2,0)));
		assertTrue(lineSegmentVertical.contains(new Point2D(3,-5)));
		assertFalse(lineSegmentVertical.contains(new Point2D(3,-5.2)));
		assertTrue(lineSegmentVertical.contains(new Point2D(3,5)));
		assertFalse(lineSegmentVertical.contains(new Point2D(3,5.2)));
	}

	// -- EQUALS and HASHCODE --

	/*
	 * Tests for equals
	 */

	@Test
	void testEqualsWhenWrongObject() {
		LineSegment2D segment2D = new LineSegment2D(new Point2D(-12,38),new Point2D(4,59));
		Integer other = 0;
		assertNotEquals(segment2D,other);
	}

	@Test void testEqualsWhenNullObject() {
		LineSegment2D segment2D = new LineSegment2D(new Point2D(-12,38),new Point2D(4,59));
		Integer other = null;
		assertNotEquals(segment2D,other);
	}

	@Test void testEqualsWhenSameObject() {
		LineSegment2D segment2D = new LineSegment2D(new Point2D(-12,38),new Point2D(4,59));
		assertEquals(segment2D,segment2D);
	}

	@Test void testEqualsWhenSameValues() {
		LineSegment2D segment1 = new LineSegment2D(new Point2D(-12,38),new Point2D(4,59));
		LineSegment2D segment2 = new LineSegment2D(new Point2D(-12,38),new Point2D(4,59));
		assertEquals(segment1,segment2);
	}

	@Test void testEqualsWhenSimilar() {
		LineSegment2D segment1 = new LineSegment2D(new Point2D(8,8),new Point2D(99,99));
		LineSegment2D segment2 = new LineSegment2D(new Point2D(14,14),new Point2D(16,16));
		assertNotEquals(segment1,segment2);
	}

	@Test void testEqualsWhenDifferent() {
		LineSegment2D segment1 = new LineSegment2D(new Point2D(8,8),new Point2D(16,16));
		LineSegment2D segment2 = new LineSegment2D(new Point2D(8,8),new Point2D(99,8));
		assertNotEquals(segment1,segment2);
	}

	/*
	 * Tests for hashcode
	 */

	@Test void testSameHashcode() {
		LineSegment2D segment1 = new LineSegment2D(new Point2D(14,14),new Point2D(99,99));
		LineSegment2D segment2 = new LineSegment2D(new Point2D(14,14),new Point2D(99,99));
		assertEquals(segment1.hashCode(),segment2.hashCode());
	}

	@Test void testDifferentHashcode() {
		LineSegment2D segment1 = new LineSegment2D(new Point2D(8,8),new Point2D(16,16));
		LineSegment2D segment2 = new LineSegment2D(new Point2D(8,8),new Point2D(99,8));
		LineSegment2D segment3 = new LineSegment2D(new Point2D(14,14),new Point2D(99,99));

		assertNotEquals(segment1.hashCode(),segment2.hashCode());
		assertNotEquals(segment1.hashCode(),segment3.hashCode());
	}
}