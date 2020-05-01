package fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Rudder;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.ParsingException;
import fr.unice.polytech.si3.qgl.stormbreakers.io.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.io.json.JsonInputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CheckpointsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CrewManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.TargetDefiner;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.TupleDistanceOrientation;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;

public class CaptainTest {

    Captain rogers;
    InputParser parser = new JsonInputParser();
    String gameData;

    @BeforeEach
    void setUp() throws IOException {
        gameData = new String(this.getClass().getResourceAsStream("/captaintest/init.json").readAllBytes());
    }

    @Test
    void accelerateTest() {
        Coordinator coordinator = mock(Coordinator.class);
        TargetDefiner targetDefiner = new TargetDefiner(null, null, null, null);
        rogers = new Captain(null, null, null, null, coordinator, targetDefiner);
        List<SailorAction> sailorsActions = List.of(new OarAction(1), new OarAction(3));
        when(coordinator.nbOars()).thenReturn(4);

        when(coordinator.addOaringSailorsOnEachSide()).thenReturn(sailorsActions);

        assertTrue(sailorsActions.containsAll(this.rogers.accelerate(1230, 200)));

        assertEquals(List.of(), rogers.accelerate(200, 210), "Nothing to do");
        assertEquals(List.of(), rogers.accelerate(0.0001, 0), "Nothing to do");
        assertEquals(List.of(), rogers.accelerate(100, 120), "Nothing to do");

    }

    @Test
    void validateActionsTest() {
        var m1 = new Sailor(1, 1, 1);
        var m2 = new Sailor(2, 2, 2);
        var m3 = new Sailor(3, 3, 3);
        var m4 = new Sailor(4, 4, 4);
        List<Sailor> sailors = List.of(m1, m2, m3, m4);
        CrewManager crewManager = new CrewManager(sailors);
        EquipmentsManager equipmentsManager = mock(EquipmentsManager.class);

        sailors.forEach(m -> assertFalse(m.isDoneTurn(), "par défaut  doneTurn est à false"));

        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);

        rogers = new Captain(null, null, null, null, coordinator, null);

        rogers.validateActions(List.of(new OarAction(1), new OarAction(3)));

        assertTrue(m1.isDoneTurn(), "le marin 1 doit etre occupe");
        assertTrue(m3.isDoneTurn(), "le marin 3 doit etre occupe");
        assertFalse(m2.isDoneTurn(), "le marin 2 n'est pas occupe");
        assertFalse(m4.isDoneTurn(), "le marin 4 n'est pas occupe");

    }

    @Test
    void calculateSpeedTest() {
        Coordinator coordinator = mock(Coordinator.class);
        rogers = new Captain(null, null, null, null, coordinator, null);

        when(coordinator.nbOars()).thenReturn(2);
        assertEquals(165 * ((double) 1 / 2),
                rogers.calculateSpeedFromOarsAction(List.of(new OarAction(1), new Turn(5, 0.2))), " should be equal");

        assertEquals(0, rogers.calculateSpeedFromOarsAction(List.of(new Turn(5, 0.2))),
                " should be 0 since no oarsAction");

    }

    @Test
    void actionsToOrientateTest1OarsOnly() {
        List<Sailor> sailors = List.of(new Sailor(0, new IntPosition(0, 0)), new Sailor(1, new IntPosition(0, 1)),
                new Sailor(2, new IntPosition(1, 0)), new Sailor(3, new IntPosition(1, 1)));
        CrewManager crewManager = new CrewManager(sailors);

        List<Equipment> equipments = List.of(new Oar(0, 0), new Oar(1, 0), new Oar(0, 1), new Oar(1, 1));
        EquipmentsManager equipmentsManager = new EquipmentsManager(equipments, 2);
        Navigator navigator = new Navigator();
        List<SailorAction> actions;

        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);
        rogers = new Captain(null, null, navigator, null, coordinator, null);

        actions = List.of(sailors.get(0).howToMoveTo(equipments.get(2).getPosition()), new OarAction(0),
                sailors.get(1).howToMoveTo(equipments.get(3).getPosition()), new OarAction(1));

        assertEquals(List.of(), rogers.actionsToOrientate(0, 100));
        // The orientation wanted is a possible OarsConfig
        assertEquals(actions.toString(), rogers.actionsToOrientate(Math.PI / 2, 100).toString());
    }

    @Test
    void actionsToOrientateTest2() {
        List<Sailor> sailors = List.of(

                new Sailor(0, new IntPosition(0, 0)), new Sailor(1, new IntPosition(0, 1)),
                new Sailor(2, new IntPosition(1, 0)), new Sailor(3, new IntPosition(1, 1))

        );
        CrewManager crewManager = new CrewManager(sailors);

        List<Equipment> equipments = List.of(new Oar(0, 0), new Oar(1, 0), new Oar(0, 1), new Oar(1, 1));
        EquipmentsManager equipmentsManager = new EquipmentsManager(equipments, 2);
        Navigator navigator = new Navigator();
        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);
        rogers = new Captain(null, null, navigator, null, coordinator, null);

        List<SailorAction> actions = List.of(sailors.get(0).howToMoveTo(equipments.get(0).getPosition()),
                new OarAction(0));
        // The orientation wanted is a possible OarsConfig
        assertEquals(actions.toString(), rogers.actionsToOrientate(-Math.PI / 4, 100).toString());
    }

    @Test
    void actionsToOrientateTestWithRudder() {
        List<Sailor> sailors = List.of(new Sailor(0, new IntPosition(0, 0)), new Sailor(1, new IntPosition(0, 1)),
                new Sailor(2, new IntPosition(1, 0)), new Sailor(3, new IntPosition(1, 1)));
        CrewManager crewManager = new CrewManager(sailors);
        List<Equipment> equipments = List.of(new Oar(0, 0), new Oar(1, 0), new Oar(0, 1), new Oar(1, 1),
                new Rudder(2, 2));
        EquipmentsManager equipmentsManager = new EquipmentsManager(equipments, 3);
        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);
        Navigator navigator = new Navigator();
        rogers = new Captain(null, null, navigator, null, coordinator, null);

        List<SailorAction> actualActions = rogers.actionsToOrientate(-Math.PI / 5, 100.0);

        assertEquals(2, actualActions.size(), "L'angle est suffisamment petit pour le gouvernail");

        var optTurnAction = actualActions.stream().filter(action -> action.getType().equals(ActionType.TURN.actionCode))
                .findFirst();
        if (optTurnAction.isPresent()) {
            var turnAction = (Turn) optTurnAction.get();

            assertEquals(-Math.PI / 5, turnAction.getRotation(), 1e-3, "doit etre -pi/5 ");
        }

    }

    @Test
    void actionsToOrientateTestWithRudderLowSpeed() {
        // vitesse requise faible (nulle) et rudder present et accessible
        List<Sailor> sailors = List.of(new Sailor(0, new IntPosition(0, 0)), new Sailor(1, new IntPosition(0, 1)),
                new Sailor(2, new IntPosition(1, 0)), new Sailor(3, new IntPosition(1, 1)));
        CrewManager crewManager = new CrewManager(sailors);
        List<Equipment> equipments = List.of(new Oar(0, 0), new Oar(1, 0), new Oar(0, 1), new Oar(1, 1),
                new Rudder(2, 2));
        EquipmentsManager equipmentsManager = new EquipmentsManager(equipments, 3);
        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);
        Navigator navigator = new Navigator();
        rogers = new Captain(null, null, navigator, null, coordinator, null);

        List<SailorAction> actualActions = rogers.actionsToOrientate(Math.PI / 2, 0.1);

        assertEquals(2, actualActions.size(), "La vitesse est nulle donc on ramene dans -PI/4 à Pi/4");

        var optTurnAction = actualActions.stream().filter(action -> action.getType().equals(ActionType.TURN.actionCode))
                .findFirst();
        if (optTurnAction.isPresent()) {
            var turnAction = (Turn) optTurnAction.get();

            assertEquals(Math.PI / 4, turnAction.getRotation(), 1e-3, "doit etre pi/4 ");
        }

    }

    @Test
    void actionsToOrientateTest4() {
        List<Sailor> sailors = List.of(new Sailor(0, new IntPosition(0, 0)), new Sailor(1, new IntPosition(0, 1)),
                new Sailor(2, new IntPosition(1, 0)), new Sailor(3, new IntPosition(1, 1)));
        CrewManager crewManager = new CrewManager(sailors);
        List<Equipment> equipments = List.of(new Oar(0, 0), new Oar(1, 0), new Oar(0, 1), new Oar(1, 1),
                new Rudder(0, 2));
        EquipmentsManager equipmentsManager = new EquipmentsManager(equipments, 2);
        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);
        Navigator navigator = new Navigator();
        rogers = new Captain(null, null, navigator, null, coordinator, null);

        List<SailorAction> actualActions = rogers.actionsToOrientate(Math.PI, 180);

        assertEquals(6, actualActions.size());
        var optTurnAction = actualActions.stream().filter(action -> action.getType().equals(ActionType.TURN.actionCode))
                .findFirst();
        if (optTurnAction.isPresent()) {
            var turnAction = (Turn) optTurnAction.get();

            assertEquals(Math.PI / 4, turnAction.getRotation(), 1e-3, "doit etre pi/4 ");
        }
    }

    @Test
    void orientateWithRudderTest() {
        List<Sailor> sailors = List.of(new Sailor(0, new IntPosition(0, 0)), new Sailor(1, new IntPosition(0, 1)),
                new Sailor(2, new IntPosition(1, 0)), new Sailor(3, new IntPosition(1, 1)));
        CrewManager crewManager = new CrewManager(sailors);
        List<Equipment> equipments = List.of(new Rudder(0, 0));
        EquipmentsManager equipmentsManager = new EquipmentsManager(equipments, 2);
        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);
        Navigator navigator = new Navigator();
        rogers = new Captain(null, null, navigator, null, coordinator, null);

        List<SailorAction> actions = rogers.orientateWithRudder(-Math.PI, 100);

        assertEquals(2, actions.size(), "Pas de rames seulement une TurnAction et la MoveAction correspondante");
        var optTurnAction = actions.stream().filter(action -> action.getType().equals(ActionType.TURN.actionCode))
                .findFirst();
        if (optTurnAction.isPresent()) {
            var turnAction = (Turn) optTurnAction.get();

            assertEquals(-Math.PI / 4, turnAction.getRotation(), 1e-3, "doit etre -pi/4 ");
        }
    }

    @Test
    void actionsToOrientateTest5() {
        List<Sailor> sailors = List.of(new Sailor(0, new IntPosition(0, 0)), new Sailor(1, new IntPosition(0, 1)),
                new Sailor(2, new IntPosition(1, 0)), new Sailor(3, new IntPosition(1, 1)));
        CrewManager crewManager = new CrewManager(sailors);
        List<Equipment> equipments = List.of(

                new Oar(0, 0), new Oar(1, 0), new Oar(0, 1), new Oar(1, 1), new Rudder(2, 2));
        EquipmentsManager equipmentsManager = new EquipmentsManager(equipments, 2);
        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);
        Navigator navigator = new Navigator();
        rogers = new Captain(null, null, navigator, null, coordinator, null);

        List<SailorAction> actualActions = rogers.actionsToOrientate(Math.PI / 10, 0.0);
        assertEquals(2, actualActions.size());
    }

    @Test
    void speedTakingIntoAccountWindNoWindTest() throws ParsingException {
        var allSailors = this.parser.fetchAllSailors(gameData);
        var prevsPos = new HashMap<Integer, IntPosition>();

        allSailors.forEach(a -> prevsPos.computeIfAbsent(a.getId(), k -> new IntPosition(a.getPosition())));

        CrewManager crewManager = new CrewManager(allSailors);
        EquipmentsManager equipmentsManager = new EquipmentsManager(parser.fetchEquipments(gameData),
                parser.fetchBoatWidth(gameData), parser);
        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);

        WeatherAnalyst weatherAnalyst = mock(WeatherAnalyst.class);

        rogers = new Captain(null, null, null, weatherAnalyst, coordinator, null);

        when(weatherAnalyst.speedFromWindExists()).thenReturn(false);

        List<SailorAction> result = rogers.adjustSpeedTakingIntoAccountWind(300, 0);
        assertFalse(result.isEmpty(), "should Send some actions");

        result.stream().filter(action -> action.getType().equals(ActionType.MOVING.actionCode))
                .map(action -> (MoveAction) action).forEach(moving -> prevsPos.compute(moving.getSailorId(),
                        (k, v) -> new IntPosition(v.x() + moving.getXdistance(), v.y() + moving.getYdistance())));

        allSailors.stream().filter(sailor -> prevsPos.keySet().contains(sailor.getId())).forEach(
                sailor -> assertEquals(sailor.getPosition(), prevsPos.get(sailor.getId()), "Should be equals"));

    }

    @Test
    void speedTakingIntoAccountWindTest() throws ParsingException {
        var allSailors = this.parser.fetchAllSailors(gameData);

        CrewManager crewManager = new CrewManager(allSailors);
        EquipmentsManager equipmentsManager = new EquipmentsManager(parser.fetchEquipments(gameData),
                parser.fetchBoatWidth(gameData), parser);
        Coordinator coordinator = new Coordinator(crewManager, equipmentsManager);

        WeatherAnalyst weatherAnalyst = mock(WeatherAnalyst.class);

        rogers = new Captain(null, null, null, weatherAnalyst, coordinator, null);

        when(weatherAnalyst.speedFromWindExists()).thenReturn(true);
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