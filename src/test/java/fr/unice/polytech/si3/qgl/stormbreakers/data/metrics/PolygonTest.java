package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;

class PolygonTest {

    List<Point2D> rectangleVertices;
    private Polygon rectangle;
    private Polygon orientedRectangle;

    List<Point2D> triangleVertices;
    private Polygon triangle;

    private String polygonJsonExample;

    @BeforeEach
    void setUp() throws IOException {
        polygonJsonExample = new String(
                this.getClass().getResourceAsStream("/polygonTest/example.json").readAllBytes());

        Point2D rectA = new Point2D(10, 5);
        Point2D rectB = new Point2D(-10, 5);
        Point2D rectC = new Point2D(-10, -5);
        Point2D rectD = new Point2D(10, -5);
        rectangleVertices = List.of(rectA, rectB, rectC, rectD);

        rectangle = new Polygon(0.0, rectangleVertices);
        orientedRectangle = new Polygon(0.5 * Math.PI, rectangleVertices);

        // LATER: 04/03/2020 Use more than rectangle

        Point2D triA = new Point2D(0, 10);
        Point2D triB = new Point2D(-5, 0);
        Point2D triC = new Point2D(5, 0);
        triangleVertices = List.of(triA, triB, triC);

        triangle = new Polygon(0.0, triangleVertices);
    }

    @Test
    void testCorrectAmoutOfVertices() {
        assertEquals(4, rectangle.getVertices().size());
        assertEquals(3, triangle.getVertices().size());
    }

    @Test
    void testCorrectAmoutOfSides() {
        assertEquals(4, rectangle.getHull().size());
        assertEquals(3, triangle.getHull().size());
    }

    @Test
    void testPolygonHullClosedOnConstruct() {
        List<LineSegment2D> rectangleHull = rectangle.getHull();
        assertEquals(rectangleHull.get(0).firstPoint(), rectangleHull.get(rectangleHull.size() - 1).lastPoint());
    }

    @Test
    void testDeserialization() {
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            mapper.readValue(polygonJsonExample, Polygon.class);
        });
    }

    @Test
    void testActualPos() {
        Point2D A = new Point2D(0, 10);
        Point2D B = new Point2D(-5, 0);
        Point2D C = new Point2D(5, 0);
        List<Point2D> vertices = List.of(A, B, C);
        Polygon polygon = new Polygon(0.0, vertices);

        // LATER: 15/03/2020 Maybe avoid computing points
        List<Point2D> translatedVertices = vertices.stream().map(point -> point.getTranslatedBy(0, 10))
                .collect(Collectors.toList());
        Polygon translatedPolygon = new Polygon(0.0, translatedVertices);
        polygon.setAnchor(new Position(0, 10));
        assertEquals(translatedPolygon.getHull(), polygon.getHull());

        // LATER: 15/03/2020 Maybe avoid computing points
        List<Point2D> rotatedVertices = vertices.stream().map(point -> point.getRotatedBy(Math.toRadians(50)))
                .collect(Collectors.toList());
        Polygon rotatedPolygon = new Polygon(0.0, rotatedVertices);
        polygon.setAnchor(new Position(0, 0, Math.toRadians(50)));
        assertEquals(rotatedPolygon.getHull(), polygon.getHull());

        Point2D Ap = new Point2D(5, 0);
        Point2D Bp = new Point2D(10, 10);
        Point2D Cp = new Point2D(0, 10);
        List<Point2D> alteredVertices = List.of(Ap, Bp, Cp);
        Polygon alteredPolygon = new Polygon(0.0, alteredVertices);
        polygon.setAnchor(new Position(5, 10, Math.PI));
        assertEquals(alteredPolygon.getHull(), polygon.getHull());

    }

    // ------------

    @Test
    void isPtInsideTestWhenNonOrientedPolygon() {
        assertTrue(rectangle.isPtInside(new Point2D(0, 0))); // Is in

        assertFalse(rectangle.isPtInside(new Point2D(0, 6))); // Is above
        assertFalse(rectangle.isPtInside(new Point2D(-11, 0))); // Is to the left
        assertFalse(rectangle.isPtInside(new Point2D(0, -6))); // Is under
        assertFalse(rectangle.isPtInside(new Point2D(11, 0))); // Is to the right

        assertTrue(rectangle.isPtInside(new Point2D(0, 5))); // Is on upper edge
        assertTrue(rectangle.isPtInside(new Point2D(-10, 0))); // Is on left edge
    }

    @Test
    void isPtInsideTestWhenOrientedPolygon() {

        assertTrue(orientedRectangle.isPtInside(new Point2D(0, 0))); // Is in

        assertFalse(orientedRectangle.isPtInside(new Point2D(0, 11))); // Is above
        assertFalse(orientedRectangle.isPtInside(new Point2D(-6, 0))); // Is to the left
        assertFalse(orientedRectangle.isPtInside(new Point2D(0, -11))); // Is under
        assertFalse(orientedRectangle.isPtInside(new Point2D(6, 0))); // Is to the right

        assertTrue(orientedRectangle.isPtInside(new Point2D(0, 10))); // Is on upper edge
        assertTrue(orientedRectangle.isPtInside(new Point2D(-5, 0))); // Is on left edge
    }

    @Test
    void isPtInsideTestWhenExtremelyThinBarrier() {
        Point2D A = new Point2D(-5, 0);
        Point2D B = new Point2D(5, 0);
        Point2D C = new Point2D(0, Utils.EPSILON);

        List<Point2D> barrierVertices = List.of(A, B, C);
        Polygon extremelyThinBarrier = new Polygon(0, barrierVertices);
        assertTrue(extremelyThinBarrier.isPtInside(new Point2D(0, 0)));
    }

    @Test
    void collidesWithSegmentTestWhenNonOrientedPolygon() {
        assertTrue(rectangle.collidesWith(new LineSegment2D(new Point2D(0, 0), new Point2D(0, 6)))); // Crossing one
                                                                                                     // edge
        assertFalse(rectangle.collidesWith(new LineSegment2D(new Point2D(20, 20), new Point2D(30, 45)))); // Completely
                                                                                                          // out

        assertTrue(rectangle.collidesWith(new LineSegment2D(new Point2D(-11, 0), new Point2D(0, 6)))); // Crosses both
                                                                                                       // upper and left
                                                                                                       // edges
    }

    @Test
    void collidesWithSegmentTestWhenOrientedPolygon() {
        assertTrue(orientedRectangle.collidesWith(new LineSegment2D(new Point2D(0, 0), new Point2D(0, 11)))); // Crossing
                                                                                                              // one
                                                                                                              // edge
        assertFalse(orientedRectangle.collidesWith(new LineSegment2D(new Point2D(20, 20), new Point2D(30, 45)))); // Completely
                                                                                                                  // out

        assertTrue(orientedRectangle.collidesWith(new LineSegment2D(new Point2D(-6, 0), new Point2D(0, 11)))); // Crosses
                                                                                                               // both
                                                                                                               // upper
                                                                                                               // and
                                                                                                               // left
                                                                                                               // edges
    }

    @Test
    void collidesWithPolygon() {
        // Same pos polygons
        assertTrue(triangle.collidesWith(rectangle));

        // Edge-to-edge rectangle polygons
        assertTrue(rectangle.collidesWith(rectangle));

        // Rotated rectangles appart
        Polygon orientedRectangleApart = new Polygon(0.5 * Math.PI, rectangleVertices, new Position(20 + 5, 0));
        assertFalse(orientedRectangle.collidesWith(orientedRectangleApart)); // No collision if orientation works

        // Edge-to-edge triangle polygons
        Polygon upsideDownTriangle = new Polygon(Math.PI, triangleVertices, new Position(0, 10));
        Polygon upsideDownTriangleEdgeToEdge = new Polygon(Math.PI, triangleVertices, new Position(5, 10));
        Polygon upsideDownTriangleApart = new Polygon(Math.PI, triangleVertices, new Position(5.2, 10));
        assertTrue(triangle.collidesWith(upsideDownTriangle));
        assertTrue(triangle.collidesWith(upsideDownTriangleEdgeToEdge));
        assertFalse(triangle.collidesWith(upsideDownTriangleApart));

    }

    @Test
    void testCollidesWithCircle() {
        assertTrue(rectangle.collidesWith(new Circle(5, new Position(0, 0, 0))));
        assertTrue(rectangle.collidesWith(new Circle(10, new Position(0, 0, 0))));
        // Completely inside
        assertTrue(rectangle.collidesWith(new Circle(15, new Position(0, 0, 0))));

        assertTrue(rectangle.collidesWith(new Circle(10, new Position(20, 0, 0))));
        assertFalse(rectangle.collidesWith(new Circle(10, new Position(20.2, 0, 0))));
        assertTrue(rectangle.collidesWith(new Circle(5, new Position(0, 10, 0))));
        assertFalse(rectangle.collidesWith(new Circle(5, new Position(0, 10.2, 0))));
    }

    @Test
    void wrappingShapeTest() {
        List<Point2D> vertices = new ArrayList<>();
        vertices.add(new Point2D(0, 3));
        vertices.add(new Point2D(2, 1));
        vertices.add(new Point2D(1, -2));
        vertices.add(new Point2D(-3, 0));
        vertices.add(new Point2D(-2, 2));

        Polygon original = new Polygon(0, vertices, new Position(0, 0));
        double margin = 5;
        Polygon expanded = (Polygon) original.wrappingShape(margin);

        assertEquals(original.getVertices().size(), expanded.getVertices().size());

        List<Point2D> expandedVertices = expanded.getVertices();
        List<LineSegment2D> originalHull = original.getHull();
        for (int i = 0; i < originalHull.size(); i++) {
            Point2D vertex = expandedVertices.get(i);
            LineSegment2D border = originalHull.get(i);
            assertEquals(margin, (border.getSupportingLine()).distance(vertex), Utils.EPSILON);
        }
    }

    @Test
    void wrappingShapeTestExpandedOutwards() {
        List<Point2D> vertices = new ArrayList<>();
        vertices.add(new Point2D(5, 5));
        vertices.add(new Point2D(-5, 5));
        vertices.add(new Point2D(-5, -5));
        vertices.add(new Point2D(5, -5));

        Polygon original = new Polygon(0, vertices, new Position(0, 0));
        double margin = 4;
        Polygon expanded = (Polygon) original.wrappingShape(margin);

        LineSegment2D lineOutsideOriginal = new LineSegment2D(new Point2D(-10, 6), new Point2D(10, 6));
        LineSegment2D lineOutsideExpandedInwards = new LineSegment2D(new Point2D(-10, 0.5), new Point2D(10, 0.5));
        LineSegment2D lineOutsideExpandedOutwards = new LineSegment2D(new Point2D(-10, 14.5), new Point2D(10, 14.5));

        // Original shape
        assertTrue(original.collidesWith(lineOutsideExpandedInwards));
        assertFalse(original.collidesWith(lineOutsideOriginal));
        assertFalse(original.collidesWith(lineOutsideExpandedOutwards));

        // Expanded shape
        assertTrue(expanded.collidesWith(lineOutsideExpandedInwards));
        assertTrue(expanded.collidesWith(lineOutsideOriginal));
        assertFalse(expanded.collidesWith(lineOutsideExpandedOutwards));

    }

    @Test
    public void wrappingShapeTestRe() {

        List<Point2D> sommets = List.of(new Point2D(64, -224.0),
         new Point2D(564.0, 576.0),
                new Point2D(-96.0,
                276.0),
                 new Point2D(-296.0,
                 -224.0),
                
                new Point2D(-236.0,
                -404.0));


        Polygon rePart = new Polygon(0.0, sommets, new Position(2196, 4684.0));

        Point2D depart = new Point2D(2306.962592519181, 5190.275946479242);
        Point2D arrive = new Point2D(3600.0, 5160);
        System.out.println(depart.distanceTo(arrive));

        assertTrue(rePart.collidesWith(new LineSegment2D(depart, arrive)));
        assertTrue(rePart.wrappingShape(12).collidesWith(new LineSegment2D(depart, arrive)));

    }
}