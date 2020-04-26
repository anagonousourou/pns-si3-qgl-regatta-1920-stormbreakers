package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;

public class StreamTest {
    
    @Test
    void isCompatibleWithTest(){
        Stream stream=new Stream(Position.create(900, 900,-0.52),new Rectangle(300, 600, 0), 100);

        assertFalse(stream.isCompatibleWith(Position.create(1500, 300), Position.create(300, 1500)) );

        assertFalse(stream.isCompatibleWith(Position.create(900, 600), Position.create(900, 1500)) );

        assertTrue(stream.isCompatibleWith(Position.create(300, 1500), Position.create(1500, 300)));

        assertTrue(stream.isCompatibleWith(Position.create(300, 900), Position.create(1500, 900)));
    }

    @Test
    void intersectsWithTest(){
        Stream stream=new Stream(Position.create(900, 900,-0.52),new Rectangle(300, 600, 0), 100);
        
        LineSegment2D segment2d=new LineSegment2D(Position.create(1500, 300), Position.create(300, 1500));

        assertTrue(stream.intersectsWith(segment2d));
        LineSegment2D othersegment=new LineSegment2D( Position.create(500, 500),Position.create(0, 900) );
        assertFalse(stream.intersectsWith(othersegment));
    }

    @Test
    void isCompletelyCompatibleWithTest(){
        Stream stream=new Stream(new Position(0,0),new Rectangle(300, 300, 0), 100);
        assertTrue(stream.isCompletelyCompatibleWith(new Position(-150, -150), new Position(350, 350)));
        assertFalse(stream.isCompletelyCompatibleWith(new Position(0, -200), new Position(0, 400)));
    }

    @Test
    void isPartiallyCompatibleWithTest(){
        Stream stream=new Stream(new Position(0,0,0.15),new Rectangle(300, 300, 0), 100);
        assertFalse(stream.isCompletelyCompatibleWith(new Position(-200, 160), new Position(350, 77.5)));
        assertTrue(stream.isPartiallyCompatibleWith(new Position(-200, 160), new Position(350, 77.5)));
        assertTrue(stream.isCompatibleWith(new Position(-200, 160), new Position(350, 77.5)));
    }

    @Test
    void speedProvidedTest(){
        //LATER add more tests
        Stream stream=new Stream(new Position(400,200),new Rectangle(200, 300, 0), 100);
        assertEquals(100.0, stream.speedProvided( new Position(100, 200) , new Position(800,200)), Utils.EPS);
        assertEquals(-100.0, stream.speedProvided(new Position(800,200),new Position(100, 200) ), Utils.EPS);
        assertEquals(Math.cos(0.78539816)*100, stream.speedProvided(new Position(400,200),new Position(500, 300) ), Utils.EPS);
    }
}