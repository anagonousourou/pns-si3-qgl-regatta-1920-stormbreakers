package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    private Rectangle rectangle;

    @BeforeEach
    void setUp() {
        // Width is along the y axis
        rectangle = new Rectangle(10, 20, 0.0);
    }

    @Test
    void testIsInsideWhenTrue() {
        assertTrue(rectangle.isPosInside(0, 0));
    }

    @Test
    void testIsInsideWhenSlightlyIn() {
        assertTrue(rectangle.isPosInside(9.5, 4.5));
    }

    @Test
    void testIsInsideWhenAtBorder() {
        assertTrue(rectangle.isPosInside(10, 5));
    }

    @Test
    void testIsInsideWhenSlightlyOut() {
        assertFalse(rectangle.isPosInside(10.5, 5.5));
    }

    @Test
    void testIsInsideWhenNotRotatedAndTrue() {
        Rectangle rect = new Rectangle(10, 20, 0);
        assertTrue(rect.isPosInside(10, 0));
    }

    @Test
    void testIsInsideWhenRotatedAndFalse() {
        Rectangle rect = new Rectangle(10, 20, Math.PI / 2);
        assertFalse(rect.isPosInside(10, 0));
    }

    @Test
    void testIsInsideWhenRotatedAndTrue() {
        Rectangle rect = new Rectangle(10, 20, Math.PI / 2);
        assertTrue(rect.isPosInside(0, 10));
    }

    @Test
    void testIsInsideWhenFalse() {
        assertFalse(rectangle.isPosInside(100, 100));
    }

    /*
     * Tests for equals
     */

    @Test void testEqualsWhenWrongObject() {
        Rectangle rectangle = new Rectangle(0,0,0);
        Integer other = 0;
        assertNotEquals(rectangle,other);
    }

    @Test void testEqualsWhenNullObject() {
        Rectangle rectangle = new Rectangle(0,0,0);
        Fraction other = null;
        assertNotEquals(rectangle,other);
    }

    @Test void testEqualsWhenSameObject() {
        Rectangle rectangle = new Rectangle(0,0,0);
        assertEquals(rectangle,rectangle);
    }

    @Test void testEqualsWhenSameValues() {
        Rectangle rect1 = new Rectangle(0,0,0);
        Rectangle rect2 = new Rectangle(0,0,0);
        assertEquals(rect1,rect2);
    }

    @Test void testEqualsWhenDifferent() {
        Rectangle rect1 = new Rectangle(0,0,0);
        Rectangle rect2 = new Rectangle(10,10,10);
        assertNotEquals(rect1,rect2);
    }

    /*
     * End of tests for equals
     */

}