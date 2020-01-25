package fr.unice.polytech.si3.qgl.stormbreakers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

public class PositionTest {
    Position a;
    Position b;
    Position c;
    Position d;

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
}