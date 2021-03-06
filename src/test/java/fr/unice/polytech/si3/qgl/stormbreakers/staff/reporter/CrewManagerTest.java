package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;

class CrewManagerTest {




    @Test
    void marineClosestToTestWhenEmptyList() {
        List<Sailor> sailors = List.of();
        CrewManager crewManager = new CrewManager(sailors);

        IntPosition pos = new IntPosition(0,0);
        assertTrue(crewManager.sailorClosestTo(pos,sailors).isEmpty());
    }
    
    @Test
    void executeMovingsInSailorAction() {
        List<Sailor> sailors = new ArrayList<>();
        Sailor sailor1 = new Sailor(0,0,0); 
        sailors.add(sailor1); 
        Sailor sailor2 = new Sailor(1,1,0);
        sailors.add(sailor2);
        CrewManager crewManager = new CrewManager(sailors);
        var mov= new MoveAction(0, 2, 2);
        var oar= new OarAction(1);
        List<SailorAction> actions= new ArrayList<>();
        actions.add(mov);
        actions.add(oar);
        crewManager.executeMovingsInSailorAction(actions);
        assertEquals(sailor1.getPosition(), new IntPosition(2, 2));
        assertEquals(sailor2.getPosition(), new IntPosition(1, 0));
    }

    @Test
    void sailorClosestToTestWhenTie() {
        List<Sailor> sailors = new ArrayList<>();
        sailors.add(new Sailor(0,new IntPosition(2,0)));
        sailors.add(new Sailor(1,new IntPosition(1,1)));
        CrewManager crewManager = new CrewManager(sailors);

        IntPosition pos = new IntPosition(0,0);
        Optional<Sailor> optionalSailor = crewManager.sailorClosestTo(pos,sailors);
        assertTrue(optionalSailor.isPresent());
        // First in the list
        assertEquals(0,optionalSailor.get().getId());
    }

    @Test
    void sailorClosestToTestWhenOnSpot() {
        List<Sailor> sailors = new ArrayList<>();
        sailors.add(new Sailor(0,new IntPosition(0,0)));
        sailors.add(new Sailor(1,new IntPosition(1,1)));
        CrewManager crewManager = new CrewManager(sailors);

        IntPosition pos = new IntPosition(0,0);
        Optional<Sailor> optionalSailor = crewManager.sailorClosestTo(pos,sailors);
        assertTrue(optionalSailor.isPresent());
        // First in the list
        assertEquals(0,optionalSailor.get().getId());
    }

    @Test
    void getAvailableSailorsInTest() {
        CrewManager crewManager = new CrewManager(List.of());

        Sailor sailor1 = new Sailor(0,0,0); sailor1.setDoneTurn(true);
        Sailor sailor2 = new Sailor(1,0,0); sailor2.setDoneTurn(false); // available
        Sailor sailor3 = new Sailor(2,0,0); sailor3.setDoneTurn(true);
        Sailor sailor4 = new Sailor(3,0,0); sailor4.setDoneTurn(false); // available
        Sailor sailor5 = new Sailor(4,0,0); sailor5.setDoneTurn(true);
        List<Sailor> sailors = List.of(sailor1,sailor2,sailor3,sailor4,sailor5);

        List<Sailor> result = crewManager.getAvailableSailorsIn(sailors);

        assertEquals(2,result.size());
        result.forEach(sailor -> assertFalse(sailor.isDoneTurn()));
    }

    @Test
    void getSailorsWhoCanReachTest() {
        CrewManager crewManager = new CrewManager(List.of());

        Sailor sailor1 = mock(Sailor.class); when(sailor1.canReach(any(IntPosition.class))).thenReturn(false);
        Sailor sailor2 = mock(Sailor.class); when(sailor2.canReach(any(IntPosition.class))).thenReturn(true);
        Sailor sailor3 = mock(Sailor.class); when(sailor3.canReach(any(IntPosition.class))).thenReturn(false);
        Sailor sailor4 = mock(Sailor.class); when(sailor4.canReach(any(IntPosition.class))).thenReturn(true);
        Sailor sailor5 = mock(Sailor.class); when(sailor5.canReach(any(IntPosition.class))).thenReturn(false);

        List<Sailor> sailors = List.of(sailor1,sailor2,sailor3,sailor4,sailor5);

        IntPosition target = new IntPosition(0,0);
        List<Sailor> result = crewManager.getSailorsWhoCanReach(sailors,target);

        assertEquals(2,result.size());
        result.forEach(sailor -> assertTrue(sailor.canReach(target)));
    }

    @Test
    void bringClosestSailorCloserToTest() {
        CrewManager crewManager = new CrewManager(List.of());

        Sailor sailor1 = new Sailor(0,0,0); sailor1.setDoneTurn(false); // available
        Sailor sailor2 = new Sailor(1,1,1); sailor2.setDoneTurn(false); // available
        Sailor sailor3 = new Sailor(2,2,2); sailor3.setDoneTurn(false); // available
        Sailor sailor4 = new Sailor(3,3,3); sailor4.setDoneTurn(false); // available
        Sailor sailor5 = new Sailor(4,4,4); sailor5.setDoneTurn(false); // available
        List<Sailor> sailors = new ArrayList<>( List.of(sailor1,sailor2,sailor3,sailor4,sailor5) ); // Needs to be mutable

        MoveAction moveAction = crewManager.bringClosestSailorCloserTo(sailors,new IntPosition(5,5));
        assertEquals(sailor5.getId(),moveAction.getSailorId());
        assertEquals(4,sailors.size()); // Un de moins car déplacé
        assertEquals(1,moveAction.getXdistance());
        assertEquals(1,moveAction.getYdistance());
    }

    @Test
    void bringClosestSailorCloserToTestWhenNoSailors() {
        CrewManager crewManager = new CrewManager(List.of());

        List<Sailor> sailors = new ArrayList<>();

        MoveAction moveAction = crewManager.bringClosestSailorCloserTo(sailors,new IntPosition(5,5));
        assertNull(moveAction);

    }
}