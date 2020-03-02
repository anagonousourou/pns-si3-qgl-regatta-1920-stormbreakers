package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

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
}