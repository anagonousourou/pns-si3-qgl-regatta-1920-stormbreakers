package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CanCollideTest {
    // Existing shapes
    // Circle - Polygon - Rectangle

    private List<Point2D> examplePolygonVertices;

    @BeforeEach
    void setUp() {
        Point2D A = new Point2D(0, 0);
        Point2D B = new Point2D(10, -13);
        Point2D C = new Point2D(-50, 0.05);
        examplePolygonVertices = List.of(A, B, C);
    }

    @Test
    void collidesWithShapeTestCirclePolygon() {
        Circle circle = new Circle(150, new Position(300, 1800));
        Point2D A = new Point2D(2,-1.5);
        Point2D B = new Point2D(2,1.5);
        Point2D C = new Point2D(-2,1.5);
        Point2D D = new Point2D(-2,-1.5);
        List<Point2D> vertices = List.of(A,B,C,D);
        Polygon polygon = new Polygon(0, vertices, new Position(304, 1721));

        assertTrue(circle.collidesWith(polygon));

        circle = new Circle(150, new Position(460, 1950));
        A = new Point2D(2,-1.5);
        B = new Point2D(2,1.5);
        C = new Point2D(-2,1.5);
        D = new Point2D(-2,-1.5);
        vertices = List.of(A,B,C,D);
        polygon = new Polygon(0, vertices, new Position(304, 1721));

        assertFalse(circle.collidesWith(polygon));
    }


    @Test
    void collidesWithShapeTestCircleRectangle() {
        Circle circle = new Circle(150, new Position(300, 1800));
        Rectangle rectangle = new Rectangle(3,4, 0, new Position(304, 1721));

        assertTrue(circle.collidesWith(rectangle));

        circle = new Circle(150, new Position(460, 1950));
        rectangle = new Rectangle(3,4, 0, new Position(304, 1721));

        assertFalse(circle.collidesWith(rectangle));
    }

    @Test
    void collidesWithShapeTestPolygonRectangle() {
        Rectangle rectangle = new Rectangle(3,4, 0, new Position(302, 1720));

        Point2D A = new Point2D(2,-1.5);
        Point2D B = new Point2D(2,1.5);
        Point2D C = new Point2D(-2,1.5);
        Point2D D = new Point2D(-2,-1.5);
        List<Point2D> vertices = List.of(A,B,C,D);
        Polygon polygon = new Polygon(0, vertices, new Position(304, 1721));

        assertTrue(polygon.collidesWith(rectangle));


        rectangle = new Rectangle(3,4, 0, new Position(460, 1950));

        A = new Point2D(2,-1.5);
        B = new Point2D(2,1.5);
        C = new Point2D(-2,1.5);
        D = new Point2D(-2,-1.5);
        vertices = List.of(A,B,C,D);
        polygon = new Polygon(0, vertices, new Position(304, 1721));

        assertFalse(polygon.collidesWith(rectangle));
    }
}

