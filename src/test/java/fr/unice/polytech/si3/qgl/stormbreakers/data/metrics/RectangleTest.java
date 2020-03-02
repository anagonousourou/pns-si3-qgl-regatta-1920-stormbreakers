package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    private Rectangle rectangle;

    @BeforeEach
    void setUp() {
        // Width is along the y axis
        rectangle = new Rectangle(10, 20, 0.0);
        setupGoodOrientation();
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
    
    

    private void setupGoodOrientation() {
    	
    	//checkpoint 
		Checkpoint cp1= new Checkpoint(new Position(14,10 ), new Circle(10));
		
		//rectangle
		Point2D rect1 = new Point2D(6,6);
		
		//boat
		Point2D boat1 = new Point2D(4,6);
		
	}
    
    @Test void testhaveGoodOrientation() {
    	
    }

    /*
     * End of tests for equals
     */

}