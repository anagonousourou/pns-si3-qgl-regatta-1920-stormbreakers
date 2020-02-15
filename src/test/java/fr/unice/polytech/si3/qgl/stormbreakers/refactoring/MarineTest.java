package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;

class MarineTest {
    private IntPosition origin = new IntPosition(0,0);
    private IntPosition marinPos = new IntPosition(3,3);
    private IntPosition targetPosInReach = new IntPosition(4,6);
    private IntPosition targetPosOutOfReach = new IntPosition(6,6);

    private Marine sailor = new Marine(0,marinPos);


    @Test
    void howToGoToTestWhenSameSpot() {
        MoveAction actual =  sailor.howToMoveTo(marinPos);
        MoveAction expected = new MoveAction(0,0,0);

        assertEquals(expected.getSailorId(),actual.getSailorId(), "Correct Id");
        assertEquals(expected.getXdistance(),actual.getXdistance(), "Correct X distance");
        assertEquals(expected.getYdistance(),actual.getYdistance(), "Correct Y distance");
    }

    @Test
    void howToGoToTestWhenDifferentSpot() {
        MoveAction actual =  sailor.howToMoveTo(targetPosInReach);
        MoveAction expected = new MoveAction(0,1,3);

        assertEquals(expected.getSailorId(),actual.getSailorId(), "Correct Id");
        assertEquals(expected.getXdistance(),actual.getXdistance(), "Correct X distance");
        assertEquals(expected.getYdistance(),actual.getYdistance(), "Correct Y distance");
    }

    @Test
    void getDistanceToTest() {
        assertEquals(4,sailor.getDistanceTo(targetPosInReach));
        assertEquals(6,sailor.getDistanceTo(targetPosOutOfReach));
    }

    @Test
    void getDistanceToTestWhen0() {
        assertEquals(0,sailor.getDistanceTo(marinPos));
    }

    @Test
    void getDistanceToTestWhenNegativeMovementNeeded() {
        assertEquals(6,sailor.getDistanceTo(origin));
    }

    @Test
    void canReachTestWhenInReach() {
        assertTrue(sailor.canReach(targetPosInReach));
    }

    @Test
    void canReachTestWhenTooFar() {
        assertFalse(sailor.canReach(targetPosOutOfReach));
    }
}