package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircleTest {

    private Circle circle;

    @BeforeEach
    void setUp() {
        circle = new Circle(10);
    }

    @Test
    void testIsInsideWhenTrue() {
        assertTrue(circle.isPosInside(0,0));
    }

    @Test
    void testIsInsideWhenAtBorder() {
        assertTrue(circle.isPosInside(10,0));
    }

    @Test
    void testIsInsideWhenSlightlyOut() {
        assertFalse(circle.isPosInside(10,0.01));
    }

    @Test
    void testIsInsideWhenFalse() {
        assertFalse(circle.isPosInside(10,10));
    }
}