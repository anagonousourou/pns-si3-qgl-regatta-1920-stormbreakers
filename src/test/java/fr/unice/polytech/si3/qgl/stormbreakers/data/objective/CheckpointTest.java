package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

class CheckpointTest {
    private Shape rectangle;
    private Position offset;
    private Position origin;

    @BeforeEach
    void setUp() {
        rectangle = new Rectangle(10,10,0);
        offset = new Position(42,72);
        origin = new Position(0,0);
    }

    @Test
    void isPosInsideWithoutOffset() {
        Checkpoint checkpoint = new Checkpoint(origin,rectangle);
        assertTrue(checkpoint.isPtInside(origin.getPoint2D()));
    }

    @Test
    void isPosInsideWithOffsetWhenFalse() {
        Checkpoint checkpoint = new Checkpoint(offset,rectangle);
        assertFalse(checkpoint.isPtInside(origin.getPoint2D()));
    }

    @Test
    void isPosInsideWithOffsetWhenTrue() {
        Checkpoint checkpoint = new Checkpoint(offset,rectangle);
        assertTrue(checkpoint.isPtInside(offset.getPoint2D()));
    }

    /*
     * Tests for equals
     */

    @Test
    void testEqualsWhenWrongObject() {
        Checkpoint checkpoint = new Checkpoint(new Position(0,0),rectangle);
        Integer other = 0;
        assertNotEquals(checkpoint,other);
    }

    @Test void testEqualsWhenNullObject() {
        Checkpoint checkpoint = new Checkpoint(new Position(0,0),rectangle);
        Checkpoint other = null;
        assertNotEquals(checkpoint,other);
    }

    @Test void testEqualsWhenSameObject() {
        Checkpoint checkpoint1 = new Checkpoint(new Position(0,0),rectangle);
        assertEquals(checkpoint1,checkpoint1);
    }

    @Test void testEqualsWhenSameValues() {
        Checkpoint checkpoint1 = new Checkpoint(new Position(0,0),new Circle(42));
        Checkpoint checkpoint2 = new Checkpoint(new Position(0,0),new Circle(42));
        assertEquals(checkpoint1,checkpoint2);
    }

    @Test void testEqualsWhenDifferent() {
        Checkpoint checkpoint1 = new Checkpoint(new Position(0,0),new Circle(42));
        Checkpoint checkpoint2 = new Checkpoint(new Position(10,10),new Circle(10));
        assertNotEquals(checkpoint1,checkpoint2);
    }

    /*
     * End of tests for equals
     */

    @Test
    void toStringNotTooLong() {
        Checkpoint checkpoint = new Checkpoint(new Position(10,10),new Circle(10));
        assertFalse(checkpoint.toString().length() > 200);
    }

}