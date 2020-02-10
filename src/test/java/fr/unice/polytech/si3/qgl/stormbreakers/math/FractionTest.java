package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;

class FractionTest {

    @Test
    void testInit() {
        assertDoesNotThrow(() -> new Fraction(1, 1));
    }

    @Test
    void testInitWhenDenom0() {
        assertThrows(ArithmeticException.class, () -> new Fraction(1, 0));
    }

    @Test
    void testEvalWhen0() {
        Fraction fraction = new Fraction(0, 42);
        assertEquals(0, fraction.eval());
    }

    @Test
    void testEvalWhenSimple() {
        Fraction fraction = new Fraction(5, 2);
        assertEquals(2.5, fraction.eval());
    }

    /*
     * Tests for equals
     */

    @Test
    void testEqualsWhenWrongObject() {
        Fraction fraction = new Fraction(1, 1);
        Integer other = 0;
        assertNotEquals(fraction, other);
    }

    @Test
    void gcdTest(){
        assertEquals(2, Fraction.gcd(2, 4), "must be 2");
    }

    @Test
    void testEqualsWhenNullObject() {
        Fraction fraction = new Fraction(1, 1);
        Fraction other = null;
        assertNotEquals(fraction, other);
    }

    @Test
    void testEqualsWhenSameObject() {
        Fraction fraction1 = new Fraction(1, 1);
        assertEquals(fraction1, fraction1);
    }

    @Test
    void testEqualsWhenSameValues() {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(1, 1);
        assertEquals(fraction1, fraction2);
    }

    @Test
    void testEqualsWhenSimilar() {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(10, 10);
        assertEquals(fraction1, fraction2);
    }

    @Test
    void testEqualsWhenDifferent() {
        Fraction fraction1 = new Fraction(1, 1);
        Fraction fraction2 = new Fraction(42, 10);
        assertNotEquals(fraction1, fraction2);
    }

    @Test
    void testHashMap(){
        Map<Fraction,Integer> map= new HashMap<>();

        map.put(new Fraction(-1,2), 2);
        map.put(new Fraction(-1,3), 3);
        map.put(new Fraction(-1,1), 1);

        assertEquals(Objects.hash(-1,2), Objects.hash(-1,2), "must generate same hashcode");
        assertEquals(new Fraction(-1,2).hashCode(), new Fraction(-1,2).hashCode(), "must have same hashcode");
        assertEquals(new Fraction(-1,2), new Fraction(-1,2), "must be equals");
        assertDoesNotThrow(()->{
            map.get(new Fraction(-1,2));
        });
    }
    /*
     * End of tests for equals
     */

}