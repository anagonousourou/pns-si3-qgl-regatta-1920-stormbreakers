package fr.unice.polytech.si3.qgl.stormbreakers.data.game;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Moving;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Goal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.RegattaGoal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Vent;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.print.DocFlavor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameStateTest {

    private GameState gameState;
    private InitGame initGameSample;
    private NextRound nextRoundSample;

    private RegattaGoal goal;

    @BeforeEach
    void setUp() {
        String initGameExample = "{\"goal\": {\"mode\": \"REGATTA\",\"checkpoints\": [{\"position\": {\"x\": 1000,\"y\": 0,\"orientation\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 50}}]},\"shipCount\": 1,\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 2,\"length\": 1},\"entities\": [{\"x\": 0,\"y\": 0,\"type\": \"oar\"},{\"x\": 1,\"y\": 0,\"type\": \"oar\"}],\"shape\": {\"type\": \"rectangle\",\"width\": 2,\"height\": 3,\"orientation\": 0}},\"sailors\": [{\"x\": 0,\"y\": 0,\"id\": 0,\"name\": \"Edward Teach\"},{\"x\": 0,\"y\": 1,\"id\": 1,\"name\": \"Tom Pouce\"}]}";
        String nextRoundExample = "{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 10.654,\"y\": 3,\"orientation\": 2.05},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 2,\"length\": 1},\"entities\": [{\"x\": 0,\"y\": 0,\"type\": \"oar\"},{\"x\": 1,\"y\": 0,\"type\": \"oar\"}]},\"visibleEntities\": []}";

        InputParser inputParser = new InputParser();
        initGameSample = inputParser.fetchInitGameState(initGameExample);
        nextRoundSample = inputParser.fetchNextRoundState(nextRoundExample);



        gameState = new GameState(initGameSample);

        goal = (RegattaGoal) initGameSample.getGoal();
    }

    @Test
    void testConstructorWithInitGame() {
        GameState gameState = new GameState(initGameSample);
        assertEquals(initGameSample.getShip().getPosition(),gameState.getPositionBateau());
        assertEquals(initGameSample.getShip().getLife(),gameState.getVieBateau());
        assertEquals(initGameSample.getSailors(),gameState.getOrgaMarins());
        assertEquals(initGameSample.getShip().getEquipments(),gameState.getEquipmentState());

        assertEquals(0.0,gameState.getWind().getOrientation());
        assertEquals(0.0, gameState.getWind().getStrength());


        assertEquals(goal.getCheckpoints().get(0), gameState.getNextCheckpoint());
    }


    @Test
    void testActualiserTour() {
        gameState.actualiserTour(nextRoundSample);
        assertEquals(nextRoundSample.getShip().getPosition(),gameState.getPositionBateau());
        assertEquals(nextRoundSample.getShip().getLife(),gameState.getVieBateau());
        assertEquals(nextRoundSample.getShip().getEquipments(),gameState.getEquipmentState());
        assertEquals(nextRoundSample.getWind(),gameState.getWind());
    }

    @Test
    void testActualiserCheckpointsWhenOut() {
        gameState.actualiserTour(nextRoundSample);
        assertEquals(gameState.getNextCheckpoint(),goal.getCheckpoints().get(0));
    }

    @Test
    void testActualiserCheckpointsWhenIn() {
        Checkpoint mockedCheckpoint = mock(Checkpoint.class);
        when(mockedCheckpoint.isPosInside(anyDouble(),anyDouble())).thenReturn(true);

        List<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(mockedCheckpoint);
        checkpoints.addAll(goal.getCheckpoints());

        GameState gState = new GameState(gameState.getPositionBateau(),gameState.getVieBateau(),gameState.getOrgaMarins(),
                gameState.getEquipmentState(), checkpoints);


        gState.actualiserTour(nextRoundSample);
        assertEquals(gameState.getNextCheckpoint(),goal.getCheckpoints().get(0));
    }

    @Test
    void testActualiserDeplacementsXY() {
        List<Moving> deplacements = new ArrayList<>();
        deplacements.add(new Moving(0,1,1));
        gameState.actualiserDeplacements(deplacements);

        Marin premierMarin = gameState.getOrgaMarins().get(0);
        assertEquals(0,premierMarin.getId());
        assertEquals(1,premierMarin.getX());
        assertEquals(1,premierMarin.getY());
    }

}