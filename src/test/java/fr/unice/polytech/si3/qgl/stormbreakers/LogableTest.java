package fr.unice.polytech.si3.qgl.stormbreakers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.EquipmentType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;

class LogableTest {

    @Test
    void testListLoggingActions() {
        List<Logable> logables = new ArrayList<>();
        logables.add(new MoveAction(0, 1, 2));
        logables.add(new OarAction(0));

        String result = Logable.listToLogs(logables, ",", "[", "]");
        assertTrue(result.contains(ActionType.MOVING.shortCode));
        assertTrue(result.contains(ActionType.OAR.shortCode));
    }

    @Test
    void testListLoggingEquipment() {
        List<Logable> logables = new ArrayList<>();
        logables.add(new Oar(0, 0));
        logables.add(new Gouvernail(0, 1));

        String result = Logable.listToLogs(logables, ",", "[", "]");
        assertTrue(result.contains(EquipmentType.OAR.shortCode));
        assertTrue(result.contains(EquipmentType.RUDDER.shortCode));
    }

}