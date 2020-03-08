package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LineSegment2DTest {

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
	
    @Test 
    void distanceTest() {
    	LineSegment2D line = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
    	Point2D p= new Point2D(12,12);
    	Point2D p2= new Point2D(18,6);
    	assertEquals(line.distance(p),6.36,Math.pow(10, -2));
    	assertEquals(line.distance(p2),8.06,Math.pow(10, -2));

    }
    
    @Test 
    void closestPointToTest() {
    	LineSegment2D line = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
    	Point2D p= new Point2D(12,12);
    	assertEquals(line.closestPointTo(p).x(),7.5,Math.pow(10, -2));
    	assertEquals(line.closestPointTo(p).y(),7.5,Math.pow(10, -2));

    	Point2D p2= new Point2D(18,6);
    	assertEquals(line.closestPointTo(p2).x(),10,Math.pow(10, -2));
    	assertEquals(line.closestPointTo(p2).y(),5,Math.pow(10, -2));

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

	@Test @Disabled
	void pointFromSegmentParameterTest() {
		Point2D A = new Point2D(42,9);
		Point2D B = new Point2D(6,66);
		LineSegment2D nonVerticalLineSegment = new LineSegment2D(A,B);
		// TODO: 08/03/2020 Replace tests with Utils.almostEqual(Point2D P1, Point2D P2, double distanceDelta )
		assertEquals(A,nonVerticalLineSegment.pointFromSegmentParameter(0));
		assertEquals(nonVerticalLineSegment.getMiddle(),nonVerticalLineSegment.pointFromSegmentParameter(0.5));
		assertEquals(B,nonVerticalLineSegment.pointFromSegmentParameter(1));

		Point2D C = new Point2D(6,42);
		Point2D D = new Point2D(6,0);
		LineSegment2D verticalLineSegment = new LineSegment2D(C,D);
		assertEquals(C,verticalLineSegment.pointFromSegmentParameter(0));
		assertEquals(verticalLineSegment.getMiddle(),verticalLineSegment.pointFromSegmentParameter(0.5));
		assertEquals(D,verticalLineSegment.pointFromSegmentParameter(1));
	}

	@Test @Disabled
	void testSegmentParameterReciprocity() {
		LineSegment2D lineSegment2D = new LineSegment2D(new Point2D(5,10),new Point2D(10,5));
		double step = 0.25;
		for (double k = 0; k<=1; k+=step){
			assertEquals(k, lineSegment2D.segmentParameterOf(lineSegment2D.pointFromSegmentParameter(k)),Utils.EPSILON);
		}
		// TODO: 08/03/2020 Replace tests with Utils.almostEqual(Point2D P1, Point2D P2, double distanceDelta )
		/*
		for (double n=0; n<=1; n+=step){
			Point2D point2D = lineSegment2D.pointFromSegmentParameter(n);
			assertEquals(point2D, lineSegment2D.pointFromSegmentParameter(lineSegment2D.segmentParameterOf(point2D)));
		}
		 */
	}
}