package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class CrewTest {




    @Test
    void marineClosestToTestWhenEmptyList() {
        List<Marine> sailors = List.of();
        Crew crew = new Crew(sailors);

        IntPosition pos = new IntPosition(0,0);
        assertTrue(crew.marineClosestTo(pos,sailors).isEmpty());
    }

    @Test
    void marineClosestToTestWhenTie() {
        List<Marine> sailors = new ArrayList<>();
        sailors.add(new Marine(0,new IntPosition(2,0)));
        sailors.add(new Marine(1,new IntPosition(1,1)));
        Crew crew = new Crew(sailors);

        IntPosition pos = new IntPosition(0,0);
        Optional<Marine> optionalMarine = crew.marineClosestTo(pos,sailors);
        assertTrue(optionalMarine.isPresent());
        // First in the list
        assertEquals(0,optionalMarine.get().getId());
    }

    @Test
    void marineClosestToTestWhenOnSpot() {
        List<Marine> sailors = new ArrayList<>();
        sailors.add(new Marine(0,new IntPosition(0,0)));
        sailors.add(new Marine(1,new IntPosition(1,1)));
        Crew crew = new Crew(sailors);

        IntPosition pos = new IntPosition(0,0);
        Optional<Marine> optionalMarine = crew.marineClosestTo(pos,sailors);
        assertTrue(optionalMarine.isPresent());
        // First in the list
        assertEquals(0,optionalMarine.get().getId());
    }

    @Test
    void getAvailableSailorsInTest() {
        Crew crew = new Crew(List.of());

        Marine sailor1 = new Marine(0,0,0); sailor1.setDoneTurn(true);
        Marine sailor2 = new Marine(1,0,0); sailor2.setDoneTurn(false); // available
        Marine sailor3 = new Marine(2,0,0); sailor3.setDoneTurn(true);
        Marine sailor4 = new Marine(3,0,0); sailor4.setDoneTurn(false); // available
        Marine sailor5 = new Marine(4,0,0); sailor5.setDoneTurn(true);
        List<Marine> sailors = List.of(sailor1,sailor2,sailor3,sailor4,sailor5);

        List<Marine> result = crew.getAvailableSailorsIn(sailors);

        assertEquals(2,result.size());
        result.forEach(sailor -> assertFalse(sailor.isDoneTurn()));
    }

    @Test
    void getSailorsWhoCanReachTest() {
        Crew crew = new Crew(List.of());

        Marine sailor1 = mock(Marine.class); when(sailor1.canReach(any(IntPosition.class))).thenReturn(false);
        Marine sailor2 = mock(Marine.class); when(sailor2.canReach(any(IntPosition.class))).thenReturn(true);
        Marine sailor3 = mock(Marine.class); when(sailor3.canReach(any(IntPosition.class))).thenReturn(false);
        Marine sailor4 = mock(Marine.class); when(sailor4.canReach(any(IntPosition.class))).thenReturn(true);
        Marine sailor5 = mock(Marine.class); when(sailor5.canReach(any(IntPosition.class))).thenReturn(false);

        List<Marine> sailors = List.of(sailor1,sailor2,sailor3,sailor4,sailor5);

        IntPosition target = new IntPosition(0,0);
        List<Marine> result = crew.getSailorsWhoCanReach(sailors,target);

        assertEquals(2,result.size());
        result.forEach(sailor -> assertTrue(sailor.canReach(target)));
    }
}