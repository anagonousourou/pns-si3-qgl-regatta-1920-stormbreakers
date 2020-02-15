package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;

public class CaptainTest {

    Captain rogers;

    @Test
    void speedSimple() {
        MediatorCrewEquipment mediatorCrewEquipment = mock(MediatorCrewEquipment.class);
        rogers = new Captain(null, null, null, null, null, mediatorCrewEquipment);
        List<SailorAction> sailorsActions = List.of(new OarAction(1), new OarAction(3));
        when(mediatorCrewEquipment.nbOars()).thenReturn(4);

        when(mediatorCrewEquipment.addOaringSailorsOnEachSide()).thenReturn(sailorsActions);

        assertTrue(sailorsActions.containsAll(this.rogers.accelerate(1230, 200)));

    }

    @Test
    void validateActionsTest() {
        var m1 = new Marine(1, 1, 1);
        var m2 = new Marine(2, 2, 2);
        var m3 = new Marine(3, 3, 3);
        var m4 = new Marine(4, 4, 4);
        List<Marine> marins = List.of(m1, m2, m3, m4);
        Crew crew = new Crew(marins);

        marins.forEach(m -> assertFalse(m.isDoneTurn(), "par défaut  doneTurn est à false"));

        MediatorCrewEquipment mediatorCrewEquipment = new MediatorCrewEquipment(crew, null);

        rogers = new Captain(null, null, crew, null, null, mediatorCrewEquipment);

        rogers.validateActions(List.of(new OarAction(1), new OarAction(3)));

        assertTrue(
            m1.isDoneTurn(),"le marin 1 doit etre occupe"
        );
        assertTrue(
            m3.isDoneTurn(),"le marin 3 doit etre occupe"
        );
        assertFalse(
            m2.isDoneTurn(),"le marin 2 n'est pas occupe"
        );
        assertFalse(
            m4.isDoneTurn(),"le marin 4 n'est pas occupe"
        );

    }

}