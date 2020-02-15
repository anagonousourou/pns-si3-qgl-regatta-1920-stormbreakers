package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;

public class CaptainTest {

    Captain rogers;
    InputParser parser = new InputParser();

    @Test
    void accelerateTest() {
        MediatorCrewEquipment mediatorCrewEquipment = mock(MediatorCrewEquipment.class);
        rogers = new Captain(null, null, null, null, null, mediatorCrewEquipment);
        List<SailorAction> sailorsActions = List.of(new OarAction(1), new OarAction(3));
        when(mediatorCrewEquipment.nbOars()).thenReturn(4);

        when(mediatorCrewEquipment.activateOarsEachSide()).thenReturn(sailorsActions);

        when(mediatorCrewEquipment.canAccelerate()).thenReturn(true, false);

        assertTrue(sailorsActions.containsAll(this.rogers.accelerate(1230, 200)));

        assertEquals(List.of(), rogers.accelerate(200, 210), "Nothing to do");

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

        assertTrue(m1.isDoneTurn(), "le marin 1 doit etre occupe");
        assertTrue(m3.isDoneTurn(), "le marin 3 doit etre occupe");
        assertFalse(m2.isDoneTurn(), "le marin 2 n'est pas occupe");
        assertFalse(m4.isDoneTurn(), "le marin 4 n'est pas occupe");

    }

    @Test
    void calculateSpeedTest() {
        MediatorCrewEquipment mediatorCrewEquipment = mock(MediatorCrewEquipment.class);
        rogers = new Captain(null, null, null, null, null, mediatorCrewEquipment);

        when(mediatorCrewEquipment.nbOars()).thenReturn(2);
        assertEquals(165 * ((double) 1 / 2),
                rogers.calculateSpeedFromOarsAction(List.of(new OarAction(1), new Turn(5, 0.2))), " should be equal");

        assertEquals(0, rogers.calculateSpeedFromOarsAction(List.of(new Turn(5, 0.2))),
                " should be 0 since no oarsAction");

    }

    @Test
    void actionsToOrientateTest() {
        MediatorCrewEquipment mediatorCrewEquipment = mock(MediatorCrewEquipment.class);
        rogers = new Captain(null, null, null, null, null, mediatorCrewEquipment) {

            @Override
            int fromAngleToDiff(double orientation) {
                // stub the method so we won't have to mock the Navigator
                return -1;
            }
        };

        when(mediatorCrewEquipment.rudderIsPresent()).thenReturn(true);
        when(mediatorCrewEquipment.rudderIsAccesible()).thenReturn(true);

        List<SailorAction> actionsRudder = List.of(new Turn(5, 0.2), new MoveAction(5, -1, 2));
        when(mediatorCrewEquipment.activateRudder(anyDouble())).thenReturn(actionsRudder);

        assertEquals(actionsRudder, rogers.actionsToOrientate(0.32), "Should return the actionsRudder");

        List<SailorAction> bunchOfActions = List.of(new OarAction(1), new MoveAction(1, 0, 0));

        when(mediatorCrewEquipment.activateOarsNotStrict(anyInt())).thenReturn(bunchOfActions);

        assertEquals(bunchOfActions, rogers.actionsToOrientate(1.5), "Should return bunchOfActions");

        assertEquals(List.of(), rogers.actionsToOrientate(0.00012), "Orientation low no need to turn");

    }

    @Test
    void speedTakingIntoAccountWindTest() {
        // trucs à mocker : weatherAnalyst

        try {
            String game = new String(this.getClass().getResourceAsStream("/captaintest/init.json").readAllBytes());
            Crew crew = new Crew(this.parser.fetchAllSailors(game));
            EquipmentManager equipmentManager = new EquipmentManager(parser.fetchEquipments(game), parser.fetchBoatWidth(game), parser);
            MediatorCrewEquipment mediatorCrewEquipment = new MediatorCrewEquipment(crew, equipmentManager);

            WeatherAnalyst weatherAnalyst=mock(WeatherAnalyst.class);

            rogers=new Captain(null, null, crew, null, weatherAnalyst, mediatorCrewEquipment);

            when(weatherAnalyst.additionalSpeedExists()).thenReturn(false);

            List<SailorAction> result=rogers.adjustSpeedTakingIntoAccountWind(300, 0);
            System.out.println(result);
            assertFalse(result.isEmpty(),"Send some actions");

            when(weatherAnalyst.additionalSpeedExists()).thenReturn(true);
            when(weatherAnalyst.potentialSpeedAcquirable()).thenReturn(200.56);

            List<SailorAction> results=rogers.adjustSpeedTakingIntoAccountWind(300, 0);
            System.out.println(results);
            assertTrue(results.stream().anyMatch(action->action.getType().equals(ActionType.LIFTSAIL.actionCode)),"Il y a au moins un liftaction" );


        } catch (IOException e) {
            
        }
        
    }

}