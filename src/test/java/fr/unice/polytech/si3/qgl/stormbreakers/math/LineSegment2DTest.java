package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LineSegment2DTest {
	
	Double delta = Math.pow(10, -3);

	@Test
	void testCannotCreateLineSegmentOfVectorZeroDirection() {
		assertThrows(DegeneratedLine2DException.class, () -> new LineSegment2D(new Point2D(0,0), new Point2D(0,0)));
		assertThrows(DegeneratedLine2DException.class, () -> new LineSegment2D(new Point2D(5,5), new Point2D(5,5)));
		assertDoesNotThrow(() -> new LineSegment2D(new Point2D(0,0), new Point2D(5,5)));
	}

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
        assertFalse(LineSegment2D.intersects(edge2, edge3));
        assertFalse(LineSegment2D.intersects(edge3, edge2));
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
	}

	@Test
	public void testGetParallelDouble() {
    	Point2D p1 = new Point2D(1, 1);
    	Point2D p2 = new Point2D(1, 3);
    	LineSegment2D line1 = new LineSegment2D(p1, p2);
    	
    	Point2D p1p = new Point2D(2, 1);
    	Point2D p2p = new Point2D(2, 3);
    	LineSegment2D line1p = new LineSegment2D(p1p, p2p);

		assertEquals(line1.parallel(new Vector(1, 0)), line1p);
	}
	
	@Test
	public void testIntersection(){
		LineSegment2D edge1 = new LineSegment2D(1, 1, 3, 2);
		LineSegment2D edge2 = new LineSegment2D(1, 1, 0, 4);

		Optional<Point2D> intersection12Opt = edge1.intersection(edge2);
		Optional<Point2D> intersection21Opt = edge1.intersection(edge2);
		assertTrue(intersection12Opt.isPresent());
		assertEquals(new Point2D(1, 1), intersection12Opt.get());
		assertTrue(intersection21Opt.isPresent());
		assertEquals(new Point2D(1, 1), intersection21Opt.get());
		
		LineSegment2D edge3 = new LineSegment2D(3, 2, 0, 4);
		Optional<Point2D> intersection13Opt = edge1.intersection(edge3);
		Optional<Point2D> intersection31Opt = edge3.intersection(edge1);
		Optional<Point2D> intersection23Opt = edge2.intersection(edge3);
		Optional<Point2D> intersection32Opt = edge3.intersection(edge2);
		assertTrue(intersection13Opt.isPresent());
		assertEquals(new Point2D(3, 2), intersection13Opt.get());
		assertTrue(intersection31Opt.isPresent());
		assertEquals(new Point2D(3, 2), intersection31Opt.get());
		assertTrue(intersection23Opt.isPresent());
		assertEquals(new Point2D(0, 4), intersection23Opt.get());
		assertTrue(intersection32Opt.isPresent());
		assertEquals(new Point2D(0, 4), intersection32Opt.get());
		
		LineSegment2D edge4 = new LineSegment2D(0, 0, 5, 1);
		assertTrue(edge1.intersection(edge4).isEmpty());
		assertTrue(edge2.intersection(edge4).isEmpty());
		assertTrue(edge3.intersection(edge4).isEmpty());

		LineSegment2D edge5 = new LineSegment2D(1, 1, 5, 5);
		LineSegment2D edge6 = new LineSegment2D(1, 5, 5, 1);
		Optional<Point2D> intersection56Opt = edge5.intersection(edge6);
		Optional<Point2D> intersection65Opt = edge6.intersection(edge5);
		assertTrue(intersection56Opt.isPresent());
		assertEquals(new Point2D(3, 3), intersection56Opt.get());
		assertTrue(intersection65Opt.isPresent());
		assertEquals(new Point2D(3, 3), intersection65Opt.get());


		// Test when two non intersection parallel line segments
		LineSegment2D baseSegment = new LineSegment2D(new Point2D(4,4),new Point2D(4,8));
		LineSegment2D parallelSegment1 = new LineSegment2D(new Point2D(10,4), new Point2D(10,8)); // Parallel line
		LineSegment2D parallelSegment2 = new LineSegment2D(new Point2D(4,10),new Point2D(4,20)); // Same line
		assertTrue(baseSegment.intersection(parallelSegment1).isEmpty());
		assertTrue(baseSegment.intersection(parallelSegment2).isEmpty());
		assertTrue(parallelSegment1.intersection(parallelSegment2).isEmpty());
	}

	

	@Test
    public void testLineSegmentSameExtremities()
    {
    	Point2D P = new Point2D(2400, 1500);
        assertThrows(DegeneratedLine2DException.class,() -> new LineSegment2D(P,P));
    }
	
    @Test 
    void distanceTest() {
    	LineSegment2D line = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
    	Point2D p= new Point2D(12,12);
    	Point2D p2= new Point2D(18,6);
    	assertEquals(6.36,line.distance(p),Math.pow(10, -2));
    	assertEquals(8.06,line.distance(p2),Math.pow(10, -2));

    }
    
    @Test 
    void closestPointToTest() {
    	LineSegment2D line = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
    	Point2D p= new Point2D(12,12);
    	assertEquals(7.5,line.closestPointTo(p).x(),Math.pow(10, -2));
    	assertEquals(7.5,line.closestPointTo(p).y(),Math.pow(10, -2));

    	Point2D p2= new Point2D(18,6);
    	assertEquals(10,line.closestPointTo(p2).x(),Math.pow(10, -2));
    	assertEquals(5,line.closestPointTo(p2).y(),Math.pow(10, -2));

    }

	@Test
	void getMiddleTest() {
		LineSegment2D line = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
		Point2D point = new Point2D(7.5,7.5);
		assertEquals(line.getMiddle(), point);

		// Centered (startpoint = origin)

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
}