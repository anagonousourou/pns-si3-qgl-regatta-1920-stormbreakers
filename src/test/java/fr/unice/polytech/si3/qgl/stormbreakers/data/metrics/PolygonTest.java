package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PolygonTest {

    private Polygon rectangle;
    private Polygon triangle;

    @BeforeEach
    void setUp() {
        Point2D rectA = new Point2D(10,5);
        Point2D rectB = new Point2D(-10,5);
        Point2D rectC = new Point2D(-10,-5);
        Point2D rectD = new Point2D(10,-5);
        rectangle = new Polygon(0.0,List.of(rectA,rectB,rectC,rectD));


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

}