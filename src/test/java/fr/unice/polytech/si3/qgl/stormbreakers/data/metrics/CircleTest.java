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

    /*
     * Tests for equals
     */

    @Test void testEqualsWhenWrongObject() {
        Circle circle = new Circle(0);
        Integer other = 0;
        assertNotEquals(circle,other);
    }

    @Test void testEqualsWhenNullObject() {
        Circle circle = new Circle(0);
        Fraction other = null;
        assertNotEquals(circle,other);
    }

    @Test void testEqualsWhenSameObject() {
        Circle circle = new Circle(0);
        assertEquals(circle,circle);
    }

    @Test void testEqualsWhenSameValues() {
        Circle circ1 = new Circle(0);
        Circle circ2 = new Circle(0);
        assertEquals(circ1,circ2);
    }

    @Test void testEqualsWhenDifferent() {
        Circle circ1 = new Circle(0);
        Circle circ2 = new Circle(10);
        assertNotEquals(circ1,circ2);
    }

    /*
     * End of tests for equals
     */
}