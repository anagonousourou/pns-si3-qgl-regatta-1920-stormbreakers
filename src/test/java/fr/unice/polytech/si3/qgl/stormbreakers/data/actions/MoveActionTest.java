package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveActionTest {

    /*
     * Tests for equals
     */

    @Test void testEqualsWhenWrongObject() {
        MoveAction move = new MoveAction(0,0,0);
        Integer other = 0;
        assertNotEquals(move,other);
    }

    @Test void testEqualsWhenNullObject() {
        MoveAction move = new MoveAction(0,0,0);
        MoveAction other = null;
        assertNotEquals(move,other);
    }

    @Test void testEqualsWhenSameObject() {
        MoveAction move = new MoveAction(0,0,0);
        assertEquals(move,move);
    }

    @Test void testEqualsWhenSameValues() {
        MoveAction move1 = new MoveAction(0,4,2);
        MoveAction move2 = new MoveAction(0,4,2);
        assertEquals(move1,move2);
    }

    @Test void testEqualsWhenDifferentId() {
        MoveAction move1 = new MoveAction(0,4,2);
        MoveAction move2 = new MoveAction(0,4,7);
        assertNotEquals(move1,move2);
    }

    @Test void testEqualsWhenDifferentXdist() {
        MoveAction move1 = new MoveAction(0,4,2);
        MoveAction move2 = new MoveAction(0,1,2);
        assertNotEquals(move1,move2);
    }

    @Test void testEqualsWhenDifferentYdist() {
        MoveAction move1 = new MoveAction(0,4,2);
        MoveAction move2 = new MoveAction(1,4,2);
        assertNotEquals(move1,move2);
    }

    /*
     * End of tests for equals
     */
}