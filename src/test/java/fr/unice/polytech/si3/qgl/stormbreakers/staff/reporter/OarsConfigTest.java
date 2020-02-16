package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.OarsConfig;

class OarsConfigTest {
    private static Fraction HALF = new Fraction(1,2);
    private static Fraction QUARTER = new Fraction(1,4);

    /*
     * Tests for equals
     */

    @Test
    void testEqualsWhenWrongObject() {
        OarsConfig oarsConfig = new OarsConfig(HALF,3);
        Integer other = 0;
        assertNotEquals(oarsConfig,other);
    }

    @Test void testEqualsWhenNullObject() {
        OarsConfig oarsConfig = new OarsConfig(HALF,3);
        OarsConfig other = null;
        assertNotEquals(oarsConfig,other);
    }

    @Test void testEqualsWhenSameObject() {
        OarsConfig oarsConfig = new OarsConfig(HALF,3);
        assertEquals(oarsConfig,oarsConfig);
    }

    @Test void testEqualsWhenSameValues() {
        OarsConfig oarConfig1 = new OarsConfig(HALF,3);
        OarsConfig oarConfig2 = new OarsConfig(HALF,3);
        assertEquals(oarConfig1,oarConfig2);
    }

    @Test void testEqualsWhenDifferentFraction() {
        OarsConfig oarConfig1 = new OarsConfig(HALF,3);
        OarsConfig oarConfig2 = new OarsConfig(QUARTER,3);
        assertNotEquals(oarConfig1,oarConfig2);
    }

    @Test void testEqualsWhenDifferentOarSidesDifference() {
        OarsConfig oarConfig1 = new OarsConfig(QUARTER,3);
        OarsConfig oarConfig2 = new OarsConfig(QUARTER,1);
        assertNotEquals(oarConfig1,oarConfig2);
    }

    /*
     * End of tests for equals
     */
}