package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public class SurfaceTest {
    private Surface carre;
    private Surface triangle;
    LineSegment2D line1 = new LineSegment2D(new Point2D(0, 0), new Point2D(300, 300));
    LineSegment2D line2 = new LineSegment2D(new Point2D(0, 100), new Point2D(100, 0));
    LineSegment2D line3 = new LineSegment2D(new Point2D(50, 350), new Point2D(500, 350));

    LineSegment2D lineTriangle1 = new LineSegment2D(new Point2D(50, 50), new Point2D(900, 50));
    LineSegment2D lineTriangle2 = new LineSegment2D(new Point2D(5, 210), new Point2D(1000, 210));
    LineSegment2D lineTriangle3 = new LineSegment2D(new Point2D(850, -10), new Point2D(860, 300));

    

    @BeforeEach
    void setUp() {
        triangle = new Surface() {

            @Override
            public double getOrientation() {
                return 0;
            }

            @Override
            public double y() {
                return 100;
            }

            @Override
            public double x() {
                return 800;
            }

            @Override
            public Shape getShape() {
                return new Polygon(0, List.of(new Point2D(100, 100), new Point2D(0, -100),
                        new Point2D(100, -100)

                ));
            }
        };

        carre = new Surface() {

            @Override
            public double getOrientation() {
                return 0;
            }

            @Override
            public double y() {
                return 300;
            }

            @Override
            public double x() {
                return 300;
            }

            @Override
            public Shape getShape() {
                return new Polygon(0, List.of(new Point2D(100, 100), new Point2D(-100, 100), new Point2D(-100, -100),
                        new Point2D(100, -100)

                ));
            }
        };
    }

    @Test
    void intersectsWithTest(){
        assertTrue(carre.intersectsWith(line1));
        assertFalse(carre.intersectsWith(line2));
        assertTrue(carre.intersectsWith(line3));
    }

    @Test
    void intersectsWithTriangleTest(){
        assertTrue(triangle.intersectsWith(lineTriangle1));
        assertFalse(triangle.intersectsWith(lineTriangle2));
        assertTrue(triangle.intersectsWith(lineTriangle3));
    }
}