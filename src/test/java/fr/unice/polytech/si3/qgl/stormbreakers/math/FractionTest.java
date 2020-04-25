package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;

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

        fraction = new Fraction(-5, 2);
        assertEquals(-2.5, fraction.eval());

        fraction = new Fraction(5, -2);
        assertEquals(-2.5, fraction.eval());

        fraction = new Fraction(-5, -2);
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
        assertEquals(1, Fraction.gcd(17, 6), "must be 1");
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
    /*
     * End of tests for equals
     */

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

    @Test
    void testAddFraction(){
        Fraction f1=new Fraction(1,2);
        Fraction f2=new Fraction(5, 3);

        assertEquals(new Fraction(13,6), f1.add(f2) ,"Should be equal" );
    }

    @Test
    void testMultFraction(){
        Fraction f1=new Fraction(1,2);
        Fraction f2=new Fraction(5, 3);

        assertEquals(new Fraction(5,6), f1.multiply(f2), "Should be equal");
    }

    @Test
    void testSubstractFraction(){
        Fraction f1=new Fraction(1,2);
        Fraction f2=new Fraction(5, 3);

        assertEquals(new Fraction(-7,6), f1.subtract(f2), "Should be equal");
    }
    

    @Test
    void testDivideFraction(){
        Fraction f1=new Fraction(1,2);
        Fraction f2=new Fraction(5, 3);

        assertEquals(new Fraction(3,10), f1.divide(f2), "Should be equal");

        Fraction f3=new Fraction(3,2);
        Fraction f4=new Fraction(1, -3);

        assertEquals(new Fraction(-9,2), f3.divide(f4), "Should be equal");
    }

    @Test
    void testLcm(){
        assertEquals(6, Fraction.lcm(2, 3), "lcm de 2 et m est 6");

        assertEquals(12, Fraction.lcm(12, 4), "lcm de 12 et 4 est 12");
        assertEquals(36, Fraction.lcm(12, 18), "lcm de 12 et 18 est 36");
    }


    @Test
    void gettersTest(){
        Fraction f1=new Fraction(1,2);
        Fraction f2=new Fraction(8, 12);
        assertEquals(1, f1.getNumerator(), "must be 1");
        assertEquals(2, f1.getDenominator(), "must be 2");

        assertEquals(2, f2.getNumerator(), "must be 2");
        assertEquals(3, f2.getDenominator(), "must be 3");
    }

    @Test
    void toStringTest(){
        Fraction f1=new Fraction(1,2);
        Fraction f2=new Fraction(8, 12);

        assertTrue(f1.toString().contains("1"));
        assertTrue(f1.toString().contains("2"));
        assertTrue(f1.toString().contains("/"));

        assertTrue(f2.toString().contains("2"));
        assertTrue(f2.toString().contains("3"));
        assertTrue(f2.toString().contains("/"));
    }

}