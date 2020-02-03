package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PositionTest {
    private Position a;
    private Position b;
    private Position c;
    private Position d;

    private static final double epsilon = Math.pow(10,-10);

    @BeforeEach
    void setUp(){
        a=new Position(1.0, 1.0,3 );
        b=new Position(1.0, 1.0,3 );
        c=new Position(1.5, 1.0,3 );
        d=new Position(0.0, 1.0,3 );
        
        
        
    }
    
    @Test
    void distanceToTest(){
        assertEquals(0.0,a.distanceTo(b));
        assertEquals(0.5,a.distanceTo(c));
    }

    @Test
    void thetaToTest(){
        assertEquals(0.0,a.thetaTo(b));
        assertEquals(Math.PI/4, a.thetaTo(d));
        assertEquals(-Math.PI/4, d.thetaTo(a));
    }


    @Test
    void testGetRotatedByHalfPi() {
        Position pos = new Position(1,0);
        Position expected = new Position(0,1);
        Position result = pos.getRotatedBy(Math.PI/2);

        assertTrue(expected.distanceTo(result) < epsilon);
    }

    @Test
    void testGetRotatedByMinusHalfPi() {
        Position pos = new Position(1,0);
        Position expected = new Position(0,-1);
        Position result = pos.getRotatedBy(-Math.PI/2);

        assertTrue(expected.distanceTo(result) < epsilon);
    }

    @Test
    void testGetRotatedBySixthOfPi() {
        Position pos = new Position(1,0);
        Position expected = new Position(Math.sqrt(3)/2,0.5);
        Position result = pos.getRotatedBy(Math.PI/6);

        assertTrue(expected.distanceTo(result) < epsilon);
    }

    /*
     * Tests for equals
     */

    @Test void testEqualsWhenWrongObject() {
        Position position = new Position(0,0);
        Integer other = 0;
        assertNotEquals(position,other);
    }

    @Test void testEqualsWhenNullObject() {
        Position position = new Position(0,0);
        Fraction other = null;
        assertNotEquals(position,other);
    }

    @Test void testEqualsWhenSameObject() {
        Position position = new Position(0,0);
        assertEquals(position,position);
    }

    @Test void testEqualsWhenSameValues() {
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(0,0);
        assertEquals(pos1,pos2);
    }

    @Test void testEqualsWhenDifferent() {
        Position pos1 = new Position(0,0);
        Position pos2 = new Position(10,10);
        assertNotEquals(pos1,pos2);
    }

    /*
     * End of tests for equals
     */
}