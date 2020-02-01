package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckpointTest {
    private Shape rectangle;
    private Shape circle;
    private Position offset;
    private Position origin;

    @BeforeEach
    void setUp() {
        rectangle = new Rectangle(10,10,0);
        circle = new Circle(10);
        offset = new Position(42,72);
        origin = new Position(0,0);
    }

    @Test
    void isPosInsideWithoutOffset() {
        Checkpoint checkpoint = new Checkpoint(origin,rectangle);
        assertTrue(checkpoint.isPosInside(origin));
    }

    @Test
    void isPosInsideWithOffsetWhenFalse() {
        Checkpoint checkpoint = new Checkpoint(offset,rectangle);
        assertFalse(checkpoint.isPosInside(origin));
    }

    @Test
    void isPosInsideWithOffsetWhenTrue() {
        Checkpoint checkpoint = new Checkpoint(offset,rectangle);
        assertTrue(checkpoint.isPosInside(offset));
    }
}