package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;

class SailorTest {
    private IntPosition origin = new IntPosition(0, 0);
    private IntPosition marinPos = new IntPosition(3, 3);
    private IntPosition targetPosInReach = new IntPosition(4, 6);
    private IntPosition targetPosOutOfReach = new IntPosition(6, 6);

    private Sailor sailor = new Sailor(0, marinPos);

    @Test
    void howToGoToTestWhenSameSpot() {
        MoveAction actual = sailor.howToMoveTo(marinPos);
        MoveAction expected = new MoveAction(0, 0, 0);

        assertEquals(expected,actual);
    }

    @Test
    void howToGoToTestWhenDifferentSpot() {
        MoveAction actual = sailor.howToMoveTo(targetPosInReach);
        MoveAction expected = new MoveAction(0, 1, 3);

        assertEquals(expected,actual);
    }

    @Test
    void getDistanceToTest() {
        assertEquals(4, sailor.getDistanceTo(targetPosInReach));
        assertEquals(6, sailor.getDistanceTo(targetPosOutOfReach));
    }

    @Test
    void getDistanceToTestWhen0() {
        assertEquals(0, sailor.getDistanceTo(marinPos));
    }

    @Test
    void getDistanceToTestWhenNegativeMovementNeeded() {
        assertEquals(6, sailor.getDistanceTo(origin));
    }

    @Test
    void canReachTestWhenInReach() {
        assertTrue(sailor.canReach(targetPosInReach));
    }

    @Test
    void canReachTestWhenTooFar() {
        assertFalse(sailor.canReach(targetPosOutOfReach));
    }

    @Test
    void onEquipmentTest() {
        assertFalse(sailor.onEquipment(), "Sailor not on equipment");

        sailor.setOnEquipment(true);

        assertTrue(sailor.onEquipment(), "Sailor NOW on equipment");
    }

    @Test
    void howToGetCloserToTestWhenCloseEnough() {
        Sailor sailorSpy = spy(sailor);
        IntPosition someReachablePos = new IntPosition(1, 1);
        sailorSpy.howToGetCloserTo(someReachablePos);

        verify(sailorSpy, atLeastOnce()).howToMoveTo(someReachablePos);
    }

    @Test
    void howToGetCloserToTestWhenFarOutOfReach() {
        IntPosition someUnreachablePos = new IntPosition(10, 10);
        MoveAction movement = sailor.howToGetCloserTo(someUnreachablePos);

        MoveAction expected = new MoveAction(sailor.getId(),5,0);
        assertEquals(expected,movement);
    }

    @Test
    void howToGetCloserToTestWhenXRowReachableButYTooFar() {
        IntPosition someUnreachablePos = new IntPosition(5, 10);
        MoveAction movement = sailor.howToGetCloserTo(someUnreachablePos);

        // X satisfait donc on deplace selon Y aussi
        MoveAction expected = new MoveAction(sailor.getId(),2,3);
        assertEquals(expected,movement);
    }

    @Test
    void howToGetCloserToTestWhenYColReachableButXTooFar() {
        IntPosition someUnreachablePos = new IntPosition(10, 5);
        MoveAction movement = sailor.howToGetCloserTo(someUnreachablePos);

        // X prioritaire donc tout sur X
        MoveAction expected = new MoveAction(sailor.getId(),5,0);
        assertEquals(expected,movement);
    }
}