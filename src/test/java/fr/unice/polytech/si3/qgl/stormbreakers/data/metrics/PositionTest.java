package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;

import static org.junit.jupiter.api.Assertions.*;

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