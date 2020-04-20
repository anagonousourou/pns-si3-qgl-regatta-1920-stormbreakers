package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;

public class CourantTest {
    
    @Test
    void isCompatibleWithTest(){
        Courant courant=new Courant(Position.create(900, 900,-0.52),new Rectangle(300, 600, 0), 100);

        assertFalse(courant.isCompatibleWith(Position.create(1500, 300), Position.create(300, 1500)) );

        assertFalse(courant.isCompatibleWith(Position.create(900, 600), Position.create(900, 1500)) );

        assertTrue(courant.isCompatibleWith(Position.create(300, 1500), Position.create(1500, 300)));

        assertTrue(courant.isCompatibleWith(Position.create(300, 900), Position.create(1500, 900)));
    }

    @Test
    void intersectsWithTest(){
        Courant courant=new Courant(Position.create(900, 900,-0.52),new Rectangle(300, 600, 0), 100);
        
        LineSegment2D segment2d=new LineSegment2D(Position.create(1500, 300), Position.create(300, 1500));

        assertTrue(courant.intersectsWith(segment2d));
        LineSegment2D othersegment=new LineSegment2D( Position.create(500, 500),Position.create(0, 900) );
        assertFalse(courant.intersectsWith(othersegment));
    }

    @Test
    void isCompletelyCompatibleWithTest(){
        Courant courant=new Courant(new Position(0,0),new Rectangle(300, 300, 0), 100);
        assertTrue(courant.isCompletelyCompatibleWith(new Position(-150, -150), new Position(350, 350)));
        assertFalse(courant.isCompletelyCompatibleWith(new Position(0, -200), new Position(0, 400)));
    }

    @Test
    void isPartiallyCompatibleWithTest(){
        Courant courant=new Courant(new Position(0,0,0.15),new Rectangle(300, 300, 0), 100);
        assertFalse(courant.isCompletelyCompatibleWith(new Position(-200, 160), new Position(350, 77.5)));
        assertTrue(courant.isPartiallyCompatibleWith(new Position(-200, 160), new Position(350, 77.5)));
        assertTrue(courant.isCompatibleWith(new Position(-200, 160), new Position(350, 77.5)));
    }

    @Test
    void speedProvidedTest(){
        //LATER add more tests
        Courant courant=new Courant(new Position(400,200),new Rectangle(200, 300, 0), 100);
        assertEquals(100.0, courant.speedProvided( new Position(100, 200) , new Position(800,200)), Utils.EPS);
        assertEquals(-100.0, courant.speedProvided(new Position(800,200),new Position(100, 200) ), Utils.EPS);
        assertEquals(Math.cos(0.78539816)*100, courant.speedProvided(new Position(400,200),new Position(500, 300) ), Utils.EPS);
    }
}