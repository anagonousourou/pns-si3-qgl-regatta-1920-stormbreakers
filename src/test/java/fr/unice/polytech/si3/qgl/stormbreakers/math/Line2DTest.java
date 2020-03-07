package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class Line2DTest {

    private static final Point2D origin = new Point2D(0,0);
    private Point2D toProject;

    @BeforeEach
    void setUp(){
        toProject = new Point2D(12,4);
    }

    // TODO: 05/03/2020 Add missing tests

    @Test
    void testCannotCreateLineOfVectorZeroDirection() {
        assertThrows(DegeneratedLine2DException.class, () -> new Line2D(new Point2D(0,0), new Vector(0,0)));
        assertThrows(DegeneratedLine2DException.class, () -> new Line2D(new Point2D(5,5), new Point2D(5,5)));
        assertDoesNotThrow(() -> new Line2D(new Point2D(0,0), new Point2D(5,5)));
    }

    @Test
    void projectOntoTestWhenVerticalLine() {
        double anyY = 8;

        double xVal1 = 5;
        Line2D vercticalLine1 = new Line2D(new Point2D(xVal1,0), Line2D.verticalDirection);
        Point2D inPoint = new Point2D(xVal1,anyY);
        Point2D outPoint = new Point2D(xVal1+5,anyY);
        assertEquals(inPoint, vercticalLine1.projectOnto(outPoint));

        double xVal2 = 5;
        Line2D vercticalLine2 = new Line2D(new Point2D(xVal2,0), Line2D.verticalDirection);
        Point2D inPoint2 = new Point2D(xVal2,anyY);
        Point2D outPoint2 = new Point2D(xVal2-15,anyY);
        assertEquals(inPoint2, vercticalLine2.projectOnto(outPoint2));
    }

    @Test
    void projectOntoTestWhenNonVerticalLine() {
        Line2D line1 = new Line2D(new Point2D(0,0),new Vector(12,4));
        assertEquals(toProject,line1.projectOnto(toProject));

        Vector direction2 = new Vector(8,16);
        Line2D line2 = new Line2D(origin,direction2);
        Point2D pointOnLine2 = origin.getTranslatedBy(direction2.scaleVector(4));
        Point2D pointOutOfLine2 = pointOnLine2.getTranslatedBy(direction2.getRotatedBy(Math.PI/2));
        assertEquals(pointOnLine2,line2.projectOnto(pointOutOfLine2));
    }

    @Test
    void intersectTestWhenVerticalLines() {
        Line2D vertical1 = new Line2D(new Point2D(0,44),Line2D.verticalDirection);
        Line2D vertical1bis = new Line2D(new Point2D(0,0),Line2D.verticalDirection);
        Line2D vertical2 = new Line2D(new Point2D(4,2),Line2D.verticalDirection);
        Line2D vertical3 = new Line2D(new Point2D(6,-2),Line2D.verticalDirection);

        assertTrue(vertical1.intersect(vertical1bis).isPresent());

        assertTrue(vertical1.intersect(vertical1).isPresent());
        assertTrue(vertical2.intersect(vertical2).isPresent());
        assertTrue(vertical3.intersect(vertical3).isPresent());

        assertFalse(vertical1.intersect(vertical2).isPresent());
        assertFalse(vertical1.intersect(vertical3).isPresent());
        assertFalse(vertical2.intersect(vertical3).isPresent());
    }

    @Test
    void intersectTestWhenNonVerticalLines() {
        // Non Parallel lines

        Line2D nonVerticalA1 = new Line2D(new Point2D(1,8),new Point2D(7,2.5));
        Line2D nonVerticalA2 = new Line2D(new Point2D(7.5,5),new Point2D(1.5,4));

        Optional<Point2D> intersectionAOpt = nonVerticalA1.intersect(nonVerticalA2);
        assertTrue(intersectionAOpt.isPresent());
        assertEquals(new Point2D(4.77,4.54),intersectionAOpt.get());


        Line2D nonVerticalB1 = new Line2D(new Point2D(2,6),new Point2D(8,2));
        Line2D nonVerticalB2 = new Line2D(new Point2D(10,6),new Point2D(2,2));

        Optional<Point2D> intersectionBOpt = nonVerticalB1.intersect(nonVerticalB2);
        assertTrue(intersectionBOpt.isPresent());
        assertEquals(new Point2D(5.43,3.71),intersectionBOpt.get());

        // Parallel Lines
        Line2D nonVerticalC1 = new Line2D(new Point2D(5,10),new Point2D(10,5));
        Line2D nonVerticalC2 = new Line2D(new Point2D(5,15),new Point2D(15,5));

        Optional<Point2D> intersectionCOpt = nonVerticalC1.intersect(nonVerticalC2);
        assertFalse(intersectionCOpt.isPresent());


    }

    @Test
    void intersectTestWhenOneOfEach() {
        Line2D nonVerticalA1 = new Line2D(new Point2D(1,8),new Point2D(7,2.5));
        Line2D verticalA2 = new Line2D(new Point2D(0,0),Line2D.verticalDirection);

        Optional<Point2D> intersectionAOpt = nonVerticalA1.intersect(verticalA2);
        assertTrue(intersectionAOpt.isPresent());
        assertEquals(new Point2D(0,8.92),intersectionAOpt.get());


        Line2D nonVerticalB1 = new Line2D(new Point2D(2,6),new Point2D(8,2));
        Line2D verticalB2 = new Line2D(new Point2D(4,2),Line2D.verticalDirection);

        Optional<Point2D> intersectionBOpt = nonVerticalB1.intersect(verticalB2);
        assertTrue(intersectionBOpt.isPresent());
        assertEquals(new Point2D(4,4.67),intersectionBOpt.get());


        Line2D nonVerticalC1 = new Line2D(new Point2D(5,10),new Point2D(10,5));
        Line2D verticalC2 = new Line2D(new Point2D(6,-2),Line2D.verticalDirection);

        Optional<Point2D> intersectionCOpt = nonVerticalC1.intersect(verticalC2);
        assertTrue(intersectionCOpt.isPresent());
        assertEquals(new Point2D(6,9),intersectionCOpt.get());
    }
}