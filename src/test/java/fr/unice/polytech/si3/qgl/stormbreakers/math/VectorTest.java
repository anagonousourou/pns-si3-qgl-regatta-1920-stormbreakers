package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VectorTest {

    private Vector vect00;
    private double EPSILON = Math.pow(10,-10);

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
        assertEquals(0,vect.angleBetween(vect));
    }

    @Test
    void testAngleBetweenWhenPIBy2() {
        Vector vectA = new Vector(0,1);
        Vector vectB = new Vector(1,0);
        assertEquals(Math.PI/2,vectA.angleBetween(vectB));
        assertEquals(Math.PI/2,vectB.angleBetween(vectA));
    }

    @Test
    void testAngleBetweenWhenPos00() {
        Vector vect = new Vector(0,1);
        // When comparing with (0,0), the result is not a number
        assertTrue(Double.isNaN(vect.angleBetween(vect00)));
    }

    @Test
    void testUnitVectorIsNorm1() {
        Vector u = Vector.createUnitVector(3);
        assertTrue(1-u.norm()<EPSILON);
    }

    @Test
    void testUnitVectorHasTrueAngle() {
        double angle = Math.toRadians(-30);
        Vector u = Vector.createUnitVector(angle);
        Vector ux = new Vector(1,0);
        assertTrue(angle-ux.angleBetween(u)<EPSILON);
    }
}