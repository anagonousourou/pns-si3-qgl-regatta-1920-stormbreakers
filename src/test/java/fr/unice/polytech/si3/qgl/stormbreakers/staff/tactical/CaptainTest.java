package fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CrewManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;

public class CaptainTest {

    Captain rogers;
    InputParser parser = new InputParser();
    String gameData;

    @BeforeEach
    void setUp() throws IOException {
        gameData = new String(this.getClass().getResourceAsStream("/captaintest/init.json").readAllBytes());
    }

    @Test
    void accelerateTest() {
        Coordinator coordinator = mock(Coordinator.class);
        rogers = new Captain(null, null, null, null, coordinator);
        List<SailorAction> sailorsActions = List.of(new OarAction(1), new OarAction(3));
        when(coordinator.nbOars()).thenReturn(4);

        when(coordinator.addOaringSailorsOnEachSide()).thenReturn(sailorsActions);

        

        assertTrue(sailorsActions.containsAll(this.rogers.accelerate(1230, 200)));

        assertEquals(List.of(), rogers.accelerate(200, 210), "Nothing to do");

    }

    @Test
    void validateActionsTest() {
        var m1 = new Sailor(1, 1, 1);
        var m2 = new Sailor(2, 2, 2);
        var m3 = new Sailor(3, 3, 3);
        var m4 = new Sailor(4, 4, 4);
        List<Sailor> marins = List.of(m1, m2, m3, m4);
        CrewManager crewManager = new CrewManager(marins);
        EquipmentsManager equipmentsManager=mock(EquipmentsManager.class);

        marins.forEach(m -> assertFalse(m.isDoneTurn(), "par défaut  doneTurn est à false"));

        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);

        rogers = new Captain(null, null, null, null, coordinator);

        rogers.validateActions(List.of(new OarAction(1), new OarAction(3)));

        assertTrue(m1.isDoneTurn(), "le marin 1 doit etre occupe");
        assertTrue(m3.isDoneTurn(), "le marin 3 doit etre occupe");
        assertFalse(m2.isDoneTurn(), "le marin 2 n'est pas occupe");
        assertFalse(m4.isDoneTurn(), "le marin 4 n'est pas occupe");

    }

    @Test
    void calculateSpeedTest() {
        Coordinator coordinator = mock(Coordinator.class);
        rogers = new Captain(null, null, null, null, coordinator);

        when(coordinator.nbOars()).thenReturn(2);
        assertEquals(165 * ((double) 1 / 2),
                rogers.calculateSpeedFromOarsAction(List.of(new OarAction(1), new Turn(5, 0.2))), " should be equal");

        assertEquals(0, rogers.calculateSpeedFromOarsAction(List.of(new Turn(5, 0.2))),
                " should be 0 since no oarsAction");

    }

    @Test
    void actionsToOrientateTest() {
        Coordinator coordinator = mock(Coordinator.class);
        Navigator navigator=mock(Navigator.class);
        when(navigator.fromAngleToDiff(anyDouble(), anyInt(), anyInt())).thenReturn(-1);
        rogers = new Captain(null, null, navigator, null, coordinator);

        when(coordinator.rudderIsPresent()).thenReturn(true);
        when(coordinator.rudderIsAccesible()).thenReturn(true);

        List<SailorAction> actionsRudder = List.of(new Turn(5, 0.2), new MoveAction(5, -1, 2));
        when(coordinator.activateRudder(anyDouble())).thenReturn(actionsRudder);

        assertEquals(actionsRudder, rogers.actionsToOrientate(0.32), "Should return the actionsRudder");

        List<SailorAction> bunchOfActions = List.of(new OarAction(1), new MoveAction(1, 0, 0));

        when(coordinator.activateOarsNotStrict(anyInt())).thenReturn(bunchOfActions);

        assertEquals(bunchOfActions, rogers.actionsToOrientate(1.5), "Should return bunchOfActions");

        assertEquals(List.of(), rogers.actionsToOrientate(0.00012), "Orientation low no need to turn");

    }

    @Test
    void speedTakingIntoAccountWindNoWindTest() throws JsonProcessingException {
        var allSailors= this.parser.fetchAllSailors(gameData);
        var prevsPos=new HashMap<Integer,IntPosition>();

        allSailors.forEach(a->prevsPos.computeIfAbsent(a.getId(), k-> new IntPosition(a.getPosition())  ));

        CrewManager crewManager = new CrewManager(allSailors);
        EquipmentsManager equipmentsManager = new EquipmentsManager(parser.fetchEquipments(gameData),
                parser.fetchBoatWidth(gameData), parser);
        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);

        WeatherAnalyst weatherAnalyst = mock(WeatherAnalyst.class);

        rogers = new Captain(null, null, null, weatherAnalyst, coordinator);

        when(weatherAnalyst.additionalSpeedExists()).thenReturn(false);

        List<SailorAction> result = rogers.adjustSpeedTakingIntoAccountWind(300, 0);
        assertFalse(result.isEmpty(), "should Send some actions");

        result.stream().filter(action->action.getType().equals(ActionType.MOVING.actionCode)).map(action->(MoveAction)action)
        .forEach(
                moving-> prevsPos.compute(moving.getSailorId(), (k,v)-> new IntPosition(v.getX()+moving.getXdistance(),v.getY()+moving.getYdistance() ) )
        );

        allSailors.stream().filter(sailor->prevsPos.keySet().contains(sailor.getId()) ).forEach(sailor->assertEquals(sailor.getPosition(),prevsPos.get(sailor.getId()) , "Should be equals"));
        
    }

    @Test
    void speedTakingIntoAccountWindTest() throws JsonProcessingException {
        var allSailors= this.parser.fetchAllSailors(gameData);


        CrewManager crewManager = new CrewManager(allSailors);
        EquipmentsManager equipmentsManager = new EquipmentsManager(parser.fetchEquipments(gameData),
                parser.fetchBoatWidth(gameData), parser);
        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);

        WeatherAnalyst weatherAnalyst = mock(WeatherAnalyst.class);

        rogers = new Captain(null, null, null, weatherAnalyst, coordinator);

        when(weatherAnalyst.additionalSpeedExists()).thenReturn(true);
        when(weatherAnalyst.potentialSpeedAcquirable()).thenReturn(200.56);

        List<SailorAction> results = rogers.adjustSpeedTakingIntoAccountWind(300, 0);
        

        assertTrue(results.stream().anyMatch(action -> action.getType().equals(ActionType.LIFTSAIL.actionCode)),
                "Il y a au moins un liftAction");
        
        

        // clear

        crewManager.resetAvailability();

        when(weatherAnalyst.currentExternalSpeed()).thenReturn(-100.20);
        when(weatherAnalyst.potentialSpeedAcquirable()).thenReturn(-200.40);

        results = rogers.adjustSpeedTakingIntoAccountWind(300, 0);
        assertTrue(results.stream().anyMatch(action -> action.getType().equals(ActionType.LOWERSAIL.actionCode)),
                "Il y a au moins un LowerAction");

        // clear

        crewManager.resetAvailability();

        when(weatherAnalyst.currentExternalSpeed()).thenReturn(250.20);
        when(weatherAnalyst.potentialSpeedAcquirable()).thenReturn(500.40);

        results = rogers.adjustSpeedTakingIntoAccountWind(300, 0);
        assertFalse(results.stream().anyMatch(action -> action.getType().equals(ActionType.LIFTSAIL.actionCode)),
                "Il N'Y A PAS de LiftAction");
        assertFalse(results.stream().anyMatch(action -> action.getType().equals(ActionType.LOWERSAIL.actionCode)),
                "et Certainement pas de  LowerAction");
        assertTrue(results.isEmpty(), "Le vent fait tout le travail pas besoin de ramer");

        // clear

        crewManager.resetAvailability();

        when(weatherAnalyst.currentExternalSpeed()).thenReturn(0.00);
        when(weatherAnalyst.potentialSpeedAcquirable()).thenReturn(-500.40);

        results = rogers.adjustSpeedTakingIntoAccountWind(300, 0);
        assertFalse(results.stream().anyMatch(action -> action.getType().equals(ActionType.LIFTSAIL.actionCode)),
                "Il N'Y A PAS de LiftAction puique le vent vous ralentirait");
        assertFalse(results.stream().anyMatch(action -> action.getType().equals(ActionType.LOWERSAIL.actionCode)),
                "et Certainement pas de  LowerAction puique currentExternalSpeed==0 donc voiles supposées baissées ");

    }

}