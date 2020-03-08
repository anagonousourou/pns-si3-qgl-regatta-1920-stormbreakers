package fr.unice.polytech.si3.qgl.stormbreakers.math;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class VectorTest {

    private Vector vect00;

    private double EPS = Utils.EPSILON;

    @BeforeEach
    void setUp() {
        vect00 = new Vector(0,0);
    }

    @Test
    void testNormWhen0() {
        assertEquals(0, vect00.norm());
    }

    @Test
    void testNormWhenZeroX() {
        assertEquals(42, new Vector(0,42).norm());
    }

    @Test
    void testNormWhenZeroY() {
        assertEquals(42, new Vector(42, 0).norm());
    }

    @Test
    void testNormWhenXY() {
        assertEquals(Math.sqrt(125), new Vector(10, 5).norm());
    }

    @Test
    void testSquaredNorm() {
        assertEquals(0, vect00.squaredNorm());
        assertEquals(42*42, new Vector(0,42).squaredNorm());
        assertEquals(42*42, new Vector(42, 0).squaredNorm());
        assertEquals(125, new Vector(10, 5).squaredNorm());
    }

    @Test
    void testScalWhenXY() {
        Vector vectA = new Vector(10,20);
        Vector vectB = new Vector(42,12);
        assertEquals(660,vectA.scal(vectB));
    }

    @Test
    void testScalWhenNoX() {
        Vector vectA = new Vector(0,20);
        Vector vectB = new Vector(42,12);
        assertEquals(240,vectA.scal(vectB));
    }

    @Test
    void testScalWhenNoY() {
        Vector vectA = new Vector(10,20);
        Vector vectB = new Vector(42,0);
        assertEquals(420,vectA.scal(vectB));
    }

    @Test
    void testAngleBetweenWhenSameVector() {
        Vector vect = new Vector(0,1);
        assertEquals(0,vect.nonOrientedAngleWith(vect));
    }

    @Test
    void testAngleBetweenWhenPIBy2() {
        Vector vectA = new Vector(0,1);
        Vector vectB = new Vector(1,0);
        assertEquals(Math.PI/2,vectA.nonOrientedAngleWith(vectB));
        assertEquals(Math.PI/2,vectB.nonOrientedAngleWith(vectA));
    }

    @Test
    void testAngleBetweenWhenPos00() {
        Vector vect = new Vector(0,1);
        // When comparing with (0,0), the result is not a number
        assertTrue(Double.isNaN(vect.nonOrientedAngleWith(vect00)));
    }

    @Test
    void testUnitVectorIsNorm1() {
        Vector u = Vector.createUnitVector(3);
        assertTrue(Utils.almostEquals(1,u.norm()));
    }

    @Test
    void testUnitVectorHasTrueAngle() {
        double angle = Math.toRadians(-30);
        Vector u = Vector.createUnitVector(angle);
        Vector ux = new Vector(1,0);
        assertTrue(Utils.almostEquals( Math.abs(angle) , Math.abs(ux.nonOrientedAngleWith(u))));
    }

    @Test
    void testScaleVector() {
        assertEquals(new Vector(5,5),new Vector(1,1).scaleVector(5));
        assertEquals(new Vector(1,1),new Vector(5,5).scaleVector(0.2));

        assertEquals(new Vector(42,0),new Vector(1,0).scaleVector(42));
        assertEquals(new Vector(0,42),new Vector(0,1).scaleVector(42));
    }


    @Test
    void testNormalize() {
        assertTrue(Utils.almostEquals(1,new Vector(434,214).normalize().norm()));
        assertTrue(Utils.almostEquals(1, new Vector( 153,0).normalize().norm()));
        assertTrue(Utils.almostEquals(1, new Vector(243,346).normalize().norm()));
        assertTrue(Utils.almostEquals(1, new Vector(0,622).normalize().norm()));
    }


    /*
     * Tests for equals
     */

    @Test void testEqualsWhenWrongObject() {
        Vector vector = new Vector(0,0);
        Integer other = 0;
        assertNotEquals(vector,other);
    }

    @Test void testEqualsWhenNullObject() {
        Vector vector = new Vector(0,0);
        Vector other = null;
        assertNotEquals(vector,other);
    }

    @Test void testEqualsWhenSameObject() {
        Vector vector = new Vector(0,0);
        assertEquals(vector,vector);
    }

    @Test void testEqualsWhenSameValues() {Vector vector = new Vector(0,0);
        Vector vector2 = new Vector(0,0);
        assertEquals(vector,vector2);
    }

    @Test void testEqualsWhenSimilarValues() {
        double subEPSILON = EPS/10;

        Vector vector = new Vector(0,0);

        Vector vector2 = new Vector(subEPSILON,0);
        assertEquals(vector,vector2);

        Vector vector3 = new Vector(0, subEPSILON);
        assertEquals(vector,vector3);

        Vector vector4 = new Vector(subEPSILON, subEPSILON);
        assertEquals(vector,vector4);

        Vector vector5 = new Vector(EPS, 0);
        assertNotEquals(vector,vector5);

    }

    @Test void testEqualsWhenDifferent() {Vector vector = new Vector(0,0);
        Vector vector2 = new Vector(10,10);
        assertNotEquals(vector,vector2);
    }

    /*
     * End of tests for equals
     */

    @Test
    void getRotatedByTestCorrectOrientation() {
        Vector unitX = Vector.UnitX;
        assertEquals(Vector.createUnitVector(0.5 * Math.PI),unitX.getRotatedBy(0.5 * Math.PI));
        assertEquals(Vector.createUnitVector(Math.PI),unitX.getRotatedBy(Math.PI));
        assertEquals(Vector.createUnitVector(0.75 * Math.PI),unitX.getRotatedBy(0.75 * Math.PI));
        assertEquals(unitX,unitX.getRotatedBy(2 * Math.PI)); // Modulo 2 PI
    }

    @Test
    void getRotatedByTestPreserveMagnitude() {
        Vector vector1 = new Vector(56,43);
        Vector rotatedVector1 = vector1.getRotatedBy(Math.PI / 6);
        assertTrue(Utils.almostEquals(vector1.norm(),rotatedVector1.norm()));
    }

    @Test
    void areCollinearTest() {
        // Direction shouldn't matter
        Vector A = new Vector(1,0);
        Vector B = new Vector(-1,0);
        assertTrue(Vector.areCollinear(A,B));

        // Magnitude shouldn't matter
        Vector C = new Vector(1,0);
        Vector D = new Vector(8,0);
        assertTrue(Vector.areCollinear(C,D));

        // Non zero oriented test
        Vector E = Vector.createUnitVector(Math.PI/2);
        Vector F = Vector.createUnitVector(Math.PI/2 + Math.PI);
        assertTrue(Vector.areCollinear(E,F));

        // Non collinear vectors test
        Vector G = new Vector(0,1);
        Vector H = new Vector(1,1);
        assertFalse(Vector.areCollinear(G,H));

        // Vector Zero test
        Vector I = new Vector(0,1);
        Vector J = new Vector(0,0);
        assertTrue(Vector.areCollinear(I,J));
    }

    @Test
    void createUnitVectorTest() {
        double half1 = 0.5;
        double halfSqrt2 = Math.sqrt(2)*0.5;
        double halfSqrt3 = Math.sqrt(3)*0.5;

        // Premier quart
        assertEquals(new Vector(1,0), Vector.createUnitVector(0));
        assertEquals(new Vector(halfSqrt3,half1), Vector.createUnitVector(Math.PI/6));
        assertEquals(new Vector(halfSqrt2,halfSqrt2), Vector.createUnitVector(Math.PI/4));
        assertEquals(new Vector(half1,halfSqrt3), Vector.createUnitVector(Math.PI/3));
        assertEquals(new Vector(0,1), Vector.createUnitVector(Math.PI/2));

        // Second quart
        assertEquals(new Vector(-halfSqrt2,halfSqrt2), Vector.createUnitVector(3*Math.PI/4));
        assertEquals(new Vector(-1,0), Vector.createUnitVector(Math.PI));

        // Troisième quart
        assertEquals(new Vector(-1,0), Vector.createUnitVector(-Math.PI));
        assertEquals(new Vector(0,-1), Vector.createUnitVector(3*Math.PI/2));

        // Quatrieme quart
        assertEquals(new Vector(0,-1), Vector.createUnitVector(-Math.PI/2));
        assertEquals(new Vector(1,0), Vector.createUnitVector(2*Math.PI));
    }


    @Test
    void getOrientationTest() {
        double half1 = 0.5;
        double halfSqrt2 = Math.sqrt(2)*0.5;
        double halfSqrt3 = Math.sqrt(3)*0.5;

        // Premier quart
        assertEquals(0,new Vector(1,0).getOrientation());
        assertEquals(Math.PI/6, new Vector(halfSqrt3,half1).getOrientation(), EPS);
        assertEquals(Math.PI/4, new Vector(halfSqrt2,halfSqrt2).getOrientation(), EPS);
        assertEquals(Math.PI/3, new Vector(half1,halfSqrt3).getOrientation(), EPS);
        assertEquals(Math.PI/2, new Vector(0,1).getOrientation());

        // Second quart
        assertEquals(3*Math.PI/4, new Vector(-halfSqrt2,halfSqrt2).getOrientation(), EPS);
        assertEquals(Math.PI, new Vector(-1,0).getOrientation());

        // Troisième quart
        assertEquals(3*Math.PI/2, new Vector(0,-1).getOrientation());
    }

    @Test
    void getRotatedByTest() {
        // Unit vectors, easy angles
        Vector vectorA = new Vector(1,0);
        assertEquals(vectorA,vectorA.getRotatedBy(0));
        Vector rotatedByPIA = new Vector(-1,0);
        assertEquals(rotatedByPIA,vectorA.getRotatedBy(Math.PI));
        Vector rotatedByHalfPIA = new Vector(0,1);
        assertEquals(rotatedByHalfPIA,vectorA.getRotatedBy(0.5 * Math.PI));

        // Unit vectors, Rotate by angles > 2Pi
        Vector vectorB = new Vector(1,0);
        assertEquals(vectorB,vectorB.getRotatedBy(2*Math.PI));
        Vector rotatedByPIB = new Vector(-1,0);
        assertEquals(rotatedByPIB,vectorB.getRotatedBy(3*Math.PI));
        Vector rotatedByHalfPIB = new Vector(0,1);
        assertEquals(rotatedByHalfPIB,vectorB.getRotatedBy(2.5 * Math.PI));

        // Unit vectors, Rotate by angles < 0
        Vector vectorC = new Vector(1,0);
        assertEquals(vectorC,vectorC.getRotatedBy(-2*Math.PI));
        Vector rotatedByPIC = new Vector(-1,0);
        assertEquals(rotatedByPIC,vectorC.getRotatedBy(-Math.PI));
        Vector rotatedByHalfPIC = new Vector(0,1);
        assertEquals(rotatedByHalfPIC,vectorC.getRotatedBy(-1.5 * Math.PI));

        // Unit vectors, rotation added
        assertEquals(Vector.createUnitVector(Math.PI), Vector.createUnitVector(Math.PI).getRotatedBy(0));
        assertEquals(Vector.createUnitVector(Math.PI), Vector.createUnitVector(Math.PI/2).getRotatedBy(Math.PI/2));
        assertEquals(Vector.createUnitVector(0), Vector.createUnitVector(Math.PI/2).getRotatedBy(-Math.PI/2));
    }

}