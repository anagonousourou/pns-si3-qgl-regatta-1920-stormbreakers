package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;

class OarConfigTest {
    private static Fraction HALF = new Fraction(1,2);
    private static Fraction QUARTER = new Fraction(1,4);

    /*
     * Tests for equals
     */

    @Test
    void testEqualsWhenWrongObject() {
        OarConfig oarConfig = new OarConfig(HALF,3);
        Integer other = 0;
        assertNotEquals(oarConfig,other);
    }

    @Test void testEqualsWhenNullObject() {
        OarConfig oarConfig = new OarConfig(HALF,3);
        OarConfig other = null;
        assertNotEquals(oarConfig,other);
    }

    @Test void testEqualsWhenSameObject() {
        OarConfig oarConfig = new OarConfig(HALF,3);
        assertEquals(oarConfig,oarConfig);
    }

    @Test void testEqualsWhenSameValues() {
        OarConfig oarConfig1 = new OarConfig(HALF,3);
        OarConfig oarConfig2 = new OarConfig(HALF,3);
        assertEquals(oarConfig1,oarConfig2);
    }

    @Test void testEqualsWhenDifferentFraction() {
        OarConfig oarConfig1 = new OarConfig(HALF,3);
        OarConfig oarConfig2 = new OarConfig(QUARTER,3);
        assertNotEquals(oarConfig1,oarConfig2);
    }

    @Test void testEqualsWhenDifferentOarSidesDifference() {
        OarConfig oarConfig1 = new OarConfig(QUARTER,3);
        OarConfig oarConfig2 = new OarConfig(QUARTER,1);
        assertNotEquals(oarConfig1,oarConfig2);
    }

    /*
     * End of tests for equals
     */
}