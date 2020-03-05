package fr.unice.polytech.si3.qgl.stormbreakers.math;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Line2DTest {

    private static final Point2D origin = new Point2D(0,0);
    private Point2D toProject;

    @BeforeEach
    void setUp(){
        toProject = new Point2D(12,4);
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
}