package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;

public class RectanglePositionedTest {
    RectanglePositioned rectanglePositioned;

    Rectangle rectangle1=new Rectangle(30,90,0);
    Rectangle rectangle2=new Rectangle(30,90,Math.PI/2);
    Rectangle rectangle3=new Rectangle(30,30,Math.PI/4);
    Rectangle rectangle4=new Rectangle(30,90,(2*Math.PI)/3);

    Position position1=new Position(10, 10);
    Position position2=new Position(10, -10);

    @Test
    void pointsTestNoOrientation(){
        rectanglePositioned=new RectanglePositioned(rectangle1, position1);

        assertEquals(new Point2D(55,25),rectanglePositioned.pointA(rectangle1, position1));
        assertEquals(rectanglePositioned.pointB(rectangle1, position1),new Point2D(55,-5));
        assertEquals(rectanglePositioned.pointC(rectangle1, position1),new Point2D(-35,-5));
        assertEquals(rectanglePositioned.pointD(rectangle1, position1),new Point2D(-35,25));
    }

    @Test
    void pointsTestOrientationHalfPi(){
        rectanglePositioned=new RectanglePositioned(rectangle2, position1);

        assertEquals(new Point2D(-5,55),rectanglePositioned.pointA(rectangle2, position1));
        assertEquals(rectanglePositioned.pointB(rectangle2, position1),new Point2D(25,55));
        assertEquals(rectanglePositioned.pointC(rectangle2, position1),new Point2D(25,-35));
        assertEquals(rectanglePositioned.pointD(rectangle2, position1),new Point2D(-5,-35));
    }

    @Test
    void pointsTestOrientationQuarterPi(){
        rectanglePositioned=new RectanglePositioned(rectangle3, position1);

        assertEquals(new Point2D(10,10+Math.sqrt(2)*15),rectanglePositioned.pointA(rectangle3, position1));
        assertEquals(rectanglePositioned.pointB(rectangle3, position1),new Point2D(10+Math.sqrt(2)*15,10));
        assertEquals(rectanglePositioned.pointC(rectangle3, position1),new Point2D(10,10-Math.sqrt(2)*15));
        assertEquals(rectanglePositioned.pointD(rectangle3, position1),new Point2D(10-Math.sqrt(2)*15,10));
    }

    @Test
    void intersectTest(){
        rectanglePositioned=new RectanglePositioned(rectangle3, position1);

        LineSegment2D line1=new LineSegment2D(10,0,10,50);
        LineSegment2D line2=new LineSegment2D(50,0,50,50);
        assertTrue( rectanglePositioned.intersectsWith(line1));

        assertFalse(rectanglePositioned.intersectsWith(line2));
        
    }

}