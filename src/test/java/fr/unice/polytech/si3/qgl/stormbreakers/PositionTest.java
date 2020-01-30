package fr.unice.polytech.si3.qgl.stormbreakers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

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
}