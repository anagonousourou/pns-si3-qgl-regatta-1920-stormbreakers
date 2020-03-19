package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    private Rectangle rectangle;
    private Point2D courant;

    private Checkpoint cp1;
    private Checkpoint cp2;

    private Point2D boat1;
    private Point2D boat2;

    @BeforeEach
    void setUp() {
        // Width is along the y axis
        rectangle = new Rectangle(10, 20, 0.0);
        setupGoodOrientation();
    }

    @Test
    void testCannotCreateRectangleOfWidthOrHeightZero() {
        assertThrows(DegeneratedLine2DException.class, () -> new Rectangle(0,1,2));
        assertThrows(DegeneratedLine2DException.class, () -> new Rectangle(1,0,2));
        assertDoesNotThrow(() -> new Rectangle(1,1,2));
    }


	  @Test
    void testIsInsideWhenTrue() {
        assertTrue(rectangle.isPtInside(new Point2D(0, 0)));
    }

    @Test
    void testIsInsideWhenSlightlyIn() {
        assertTrue(rectangle.isPtInside(new Point2D(9.5, 4.5)));
    }

    @Test
    void testIsInsideWhenAtBorder() {
        assertTrue(rectangle.isPtInside(new Point2D(10, 5)));
    }

    @Test
    void testIsInsideWhenSlightlyOut() {
        assertFalse(rectangle.isPtInside(new Point2D(10.5, 5.5)));
    }

    @Test
    void testIsInsideWhenNotRotatedAndTrue() {
        Rectangle rect = new Rectangle(10, 20, 0);
        assertTrue(rect.isPtInside(new Point2D(10, 0)));
    }

    @Test
    void testIsInsideWhenRotatedAndFalse() {
        Rectangle rect = new Rectangle(10, 20, Math.PI / 2);
        assertFalse(rect.isPtInside(new Point2D(10, 0)));
    }

    @Test
    void testIsInsideWhenRotatedAndTrue() {
        Rectangle rect = new Rectangle(10, 20, Math.PI / 2);
        assertTrue(rect.isPtInside(new Point2D(0, 10)));
    }

    @Test
    void testIsInsideWhenFalse() {
        assertFalse(rectangle.isPtInside(new Point2D(100, 100)));
    }

    /*
     * Tests for equals
     */

    @Test void testEqualsWhenWrongObject() {
        Rectangle rectangle = new Rectangle(1, 1, 0);
        Integer other = 0;
        assertNotEquals(rectangle, other);
    }

    @Test void testEqualsWhenNullObject() {
        Rectangle rectangle = new Rectangle(1, 1, 0);
        Fraction other = null;
        assertNotEquals(rectangle, other);
    }

    @Test void testEqualsWhenSameObject() {
        Rectangle rectangle = new Rectangle(1, 1, 0);
        assertEquals(rectangle,rectangle);
    }

    @Test void testEqualsWhenSameValues() {
        Rectangle rect1 = new Rectangle(1, 1, 0);
        Rectangle rect2 = new Rectangle(1, 1, 0);
        assertEquals(rect1,rect2);
    }

    @Test void testEqualsWhenDifferent() {
        Rectangle rect1 = new Rectangle(1, 1, 0);
        Rectangle rect2 = new Rectangle(10, 10, 10);
        assertNotEquals(rect1,rect2);
    }

    private void setupGoodOrientation() {

        // checkpoint
        cp1 = new Checkpoint(new Position(14, 10), new Circle(10));
        cp2 = new Checkpoint(new Position(0, 10), new Circle(10));

        // rectangle
        courant = new Point2D(6, 6);
        // boat
        boat1 = new Point2D(4, 6);
        boat2 = new Point2D(15, 6);

    }

    @Test
    void testhaveGoodOrientation() {
        assertTrue(rectangle.haveGoodOrientation(cp1, boat1, courant));
        assertFalse(rectangle.haveGoodOrientation(cp1, boat2, courant));
        assertFalse(rectangle.haveGoodOrientation(cp2, boat1, courant));
    }

    /*
     * End of tests for equals
     */
    @Test
    void isInsideOpenShapeTest() {

        assertTrue( rectangle.isInsideOpenShape(new Position(0,0)) );
        assertFalse( rectangle.isInsideOpenShape(new Position(0,5.0)) );
        assertFalse( rectangle.isInsideOpenShape(new Position(0,-5.0)) );
        assertTrue( rectangle.isInsideOpenShape(new Position(0,4.9)) );
        assertFalse( rectangle.isInsideOpenShape(new Position(10,5)) );
    }


}