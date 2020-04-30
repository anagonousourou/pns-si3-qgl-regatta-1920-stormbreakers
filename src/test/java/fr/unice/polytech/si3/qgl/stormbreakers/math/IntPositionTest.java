package fr.unice.polytech.si3.qgl.stormbreakers.math;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

class IntPositionTest {
// -- EQUALS and HASHCODE --

    /*
     * Tests for equals
     */

    @Test
    void testEqualsWhenWrongObject() {
        IntPosition intPos = new IntPosition(-12,4);
        Integer other = 0;
        assertNotEquals(intPos,other);
    }

    @Test void testEqualsWhenNullObject() {
        IntPosition intPos = new IntPosition(-12,4);
        Integer other = null;
        assertNotEquals(intPos,other);
    }

    @Test void testEqualsWhenSameObject() {
        IntPosition intPos = new IntPosition(-12,4);
        assertEquals(intPos,intPos);
    }

    @Test void testEqualsWhenSameValues() {
        IntPosition intPos1 = new IntPosition(-12,4);
        IntPosition intPos2 = new IntPosition(-12,4);
        assertEquals(intPos1,intPos2);
    }

    @Test void testEqualsWhenDifferent() {
        IntPosition intPos1 = new IntPosition(-12,90);
        IntPosition intPos2 = new IntPosition(-12,4);
        IntPosition intPos3 = new IntPosition(45,4);

        assertNotEquals(intPos1,intPos2);
        assertNotEquals(intPos2,intPos3);
    }

    /*
     * Tests for hashcode
     */

    @Test void testSameHashcode() {
        IntPosition intPos1 = new IntPosition(-12,90);
        IntPosition intPos2 = new IntPosition(-12,90);
        assertEquals(intPos1.hashCode(),intPos2.hashCode());
    }

    @Test void testDifferentHashcode() {
        IntPosition intPos1 = new IntPosition(-12,90);
        IntPosition intPos2 = new IntPosition(-12,4);
        IntPosition intPos3 = new IntPosition(45,4);

        assertNotEquals(intPos1.hashCode(),intPos2.hashCode());
        assertNotEquals(intPos2.hashCode(),intPos3.hashCode());
    }
}