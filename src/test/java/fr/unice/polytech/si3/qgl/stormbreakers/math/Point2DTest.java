package fr.unice.polytech.si3.qgl.stormbreakers.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point2DTest {

    private static final double EPSILON = Math.pow(10,-10);

    private boolean almostEquals(double expected, double result) {
        return Math.abs(expected-result) < EPSILON;
    }

    private boolean almostEquals(Point2D expected, Point2D result) {
        return result.getDistanceTo(expected) < EPSILON;
    }

    @Test
    void testGetAngleFromXAxisWhenOrigin() {
        assertThrows(RuntimeException.class, () -> new Point2D(0,0).getAngleFromXAxis());
    }

    @Test
    void testGetAngleFromXAxisWhenOnXAxis() {
        assertEquals(0, new Point2D(42,0).getAngleFromXAxis());
        assertEquals(Math.PI, new Point2D(-42,0).getAngleFromXAxis());
    }

    @Test
    void testGetAngleFromXAxisWhenAboveXAxis() {
        assertEquals(Math.PI/3, new Point2D(0.5,Math.sqrt(3)/2).getAngleFromXAxis());
        assertEquals(Math.PI/4, new Point2D(Math.sqrt(2)/2,Math.sqrt(2)/2).getAngleFromXAxis());
        assertTrue(almostEquals(Math.PI/6,new Point2D(Math.sqrt(3)/2,0.5).getAngleFromXAxis()));

        assertEquals(Math.PI/3+Math.PI, new Point2D(-0.5,-Math.sqrt(3)/2).getAngleFromXAxis());
        assertEquals(Math.PI/4+Math.PI, new Point2D(-Math.sqrt(2)/2,-Math.sqrt(2)/2).getAngleFromXAxis());
        assertEquals(Math.PI/6+Math.PI, new Point2D(-Math.sqrt(3)/2,-0.5).getAngleFromXAxis());
    }

    @Test
    void testGetAngleFromXAxisWhenUnderXAxis() {
        assertEquals((2*Math.PI)-Math.PI/3, new Point2D(0.5,-Math.sqrt(3)/2).getAngleFromXAxis());
        assertEquals((2*Math.PI)-Math.PI/4, new Point2D(Math.sqrt(2)/2,-Math.sqrt(2)/2).getAngleFromXAxis());
        assertTrue(almostEquals((2*Math.PI)-Math.PI/6, new Point2D(Math.sqrt(3)/2,-0.5).getAngleFromXAxis()));
    }

    @Test
    void testGetAngleFromUnitVector() {
        Point2D point2D = new Point2D(Vector.createUnitVector(Math.PI/8));
        assertTrue(almostEquals(Math.PI/8,point2D.getAngleFromXAxis()));

        point2D = new Point2D(Vector.createUnitVector(Math.PI/8+Math.PI));
        assertTrue(almostEquals(Math.PI/8+Math.PI,point2D.getAngleFromXAxis()));
    }

    @Test
    void testGetRotatedBy() {
        assertTrue(almostEquals(new Point2D(0,1),new Point2D(1,0).getRotatedBy(Math.PI/2)));
        assertTrue(almostEquals(new Point2D(0,-1),new Point2D(1,0).getRotatedBy(-Math.PI/2)));

        assertTrue(almostEquals(new Point2D(-1,0),new Point2D(1,0).getRotatedBy(Math.PI)));
        assertTrue(almostEquals(new Point2D(-1,0),new Point2D(1,0).getRotatedBy(-Math.PI)));

        assertTrue(almostEquals(new Point2D(1,0),new Point2D(Math.sqrt(2)/2,Math.sqrt(2)/2).getRotatedBy(-Math.PI/4)));
        assertTrue(almostEquals(new Point2D(0,1),new Point2D(Math.sqrt(2)/2,Math.sqrt(2)/2).getRotatedBy(Math.PI/4)));

        assertTrue(almostEquals(new Point2D(1,0),new Point2D(1,0).getRotatedBy(2*Math.PI)));
        assertTrue(almostEquals(new Point2D(0,1),new Point2D(0,1).getRotatedBy(2*Math.PI)));
    }


    /*
     * Tests for equals
     */

    @Test void testEqualsWhenWrongObject() {
        Point2D point = new Point2D(0,0);
        Integer other = 0;
        assertNotEquals(point,other);
    }

    @Test void testEqualsWhenNullObject() {
        Point2D point = new Point2D(0,0);
        Point2D other = null;
        assertNotEquals(point,other);
    }

    @Test void testEqualsWhenSameObject() {
        Point2D point = new Point2D(0,0);
        assertEquals(point,point);
    }

    @Test void testEqualsWhenSameValues() {
        Point2D point1 = new Point2D(0,0);
        Point2D point2 = new Point2D(0,0);
        assertEquals(point1,point2);
    }

    @Test void testEqualsWhenDifferent() {
        Point2D point1 = new Point2D(0,0);
        Point2D point2 = new Point2D(10,10);
        assertNotEquals(point1,point2);
    }

    /*
     * End of tests for equals
     */
}