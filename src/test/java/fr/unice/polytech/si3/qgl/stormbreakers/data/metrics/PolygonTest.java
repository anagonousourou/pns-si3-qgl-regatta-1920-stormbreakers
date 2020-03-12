package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PolygonTest {

    private Polygon rectangle;
    private Polygon triangle;

    private String polygonJsonExample;

    @BeforeEach
    void setUp() throws IOException {
        polygonJsonExample = new String(this.getClass().getResourceAsStream("/polygonTest/example.json").readAllBytes());

        Point2D rectA = new Point2D(10,5);
        Point2D rectB = new Point2D(-10,5);
        Point2D rectC = new Point2D(-10,-5);
        Point2D rectD = new Point2D(10,-5);
        rectangle = new Polygon(0.0,List.of(rectA,rectB,rectC,rectD));

        // LATER: 04/03/2020 Use more than rectangle

        Point2D triA = new Point2D(0,10);
        Point2D triB = new Point2D(-5,0);
        Point2D triC = new Point2D(5,0);
        triangle = new Polygon(0.0,List.of(triA,triB,triC));
    }

    @Test
    void testPolygonHullClosedOnConstruct() {
        List<LineSegment2D> rectangleHull = rectangle.getHull();
        assertEquals(rectangleHull.get(0).firstPoint(),rectangleHull.get(rectangleHull.size()-1).lastPoint());
    }

    @Test
    void testDeserialization() {
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> {mapper.readValue(polygonJsonExample,Polygon.class);});
    }

    @Test
    void isPtInsideTestWhenNonOrientedPolygon() {
        assertTrue(rectangle.isPtInside(new Point2D(0,0))); // Is in

        assertFalse(rectangle.isPtInside(new Point2D(0,6))); // Is above
        assertFalse(rectangle.isPtInside(new Point2D(-11,0))); // Is to the left
        assertFalse(rectangle.isPtInside(new Point2D(0,-6))); // Is under
        assertFalse(rectangle.isPtInside(new Point2D(11,0))); // Is to the right

        assertTrue(rectangle.isPtInside(new Point2D(0,5))); // Is on upper edge
        assertTrue(rectangle.isPtInside(new Point2D(-10,0))); // Is on left edge
    }

    // LATER: 04/03/2020 isPtInsideTestWhenOrientedPolygon

    @Test
    void intersectsWithWhenNonOrientedPolygon() {
        assertTrue(rectangle.intersectsWith(new LineSegment2D(new Point2D(0,0),new Point2D(0,6)))); // Crossing one edge
        assertFalse(rectangle.intersectsWith(new LineSegment2D(new Point2D(20,20),new Point2D(30,45)))); // Completely out

        assertTrue(rectangle.intersectsWith(new LineSegment2D(new Point2D(-11,0),new Point2D(0,6)))); // Crosses both upper and left edges
    }

    // LATER: 03/03/2020 Test intersectsWithWhenNonOrientedPolygonWhenOrientedPolygon

}