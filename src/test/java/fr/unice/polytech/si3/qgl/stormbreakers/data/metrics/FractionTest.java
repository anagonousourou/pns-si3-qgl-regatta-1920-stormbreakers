package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FractionTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void testInit() {
        assertDoesNotThrow(() -> new Fraction(1,1));
    }

    @Test
    void testInitWhenDenom0() {
        assertThrows(IllegalArgumentException.class,() -> new Fraction(1,0));
    }

    @Test
    void testEvalWhen0() {
        Fraction fraction = new Fraction(0,42);
        assertEquals(0,fraction.eval());
    }

    @Test
    void testEvalWhenSimple() {
        Fraction fraction = new Fraction(5,2);
        assertEquals(2.5,fraction.eval());
    }

    @Test void testEqualsWhenWrongObject() {
        Fraction fraction = new Fraction(1,1);
        Integer other = 0;
        assertNotEquals(fraction,other);
    }

    @Test void testEqualsWhenNullObject() {
        Fraction fraction = new Fraction(1,1);
        Fraction other = null;
        assertNotEquals(fraction,other);
    }

    @Test void testEqualsWhenSameObject() {
        Fraction fraction1 = new Fraction(1,1);
        assertEquals(fraction1,fraction1);
    }

    @Test void testEqualsWhenSameValues() {
        Fraction fraction1 = new Fraction(1,1);
        Fraction fraction2 = new Fraction(1,1);
        assertEquals(fraction1,fraction2);
    }

    @Test void testEqualsWhenSimilar() {
        Fraction fraction1 = new Fraction(1,1);
        Fraction fraction2 = new Fraction(10,10);
        assertEquals(fraction1,fraction2);
    }

    @Test void testEqualsWhenDifferent() {
        Fraction fraction1 = new Fraction(1,1);
        Fraction fraction2 = new Fraction(42,10);
        assertNotEquals(fraction1,fraction2);
    }

}