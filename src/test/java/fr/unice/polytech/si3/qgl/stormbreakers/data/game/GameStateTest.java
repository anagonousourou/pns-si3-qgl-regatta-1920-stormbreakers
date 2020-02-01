package fr.unice.polytech.si3.qgl.stormbreakers.data.game;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Moving;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.RegattaGoal;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        GameState gState = new GameState(initGameSample);
        assertEquals(initGameSample.getShip().getPosition(),gState.getPositionBateau());
        assertEquals(initGameSample.getShip().getLife(),gState.getVieBateau());
        assertEquals(initGameSample.getSailors(),gState.getOrgaMarins());
        assertEquals(initGameSample.getShip().getEquipments(),gState.getEquipmentState());

        assertEquals(0.0,gState.getWind().getOrientation());
        assertEquals(0.0, gState.getWind().getStrength());

        assertEquals(goal.getCheckpoints().get(0), gState.getNextCheckpoint());
    }


    @Test
    void testActualiserTour() {
        gameState.updateTurn(nextRoundSample);
        assertEquals(nextRoundSample.getShip().getPosition(),gameState.getPositionBateau());
        assertEquals(nextRoundSample.getShip().getLife(),gameState.getVieBateau());
        assertEquals(nextRoundSample.getShip().getEquipments(),gameState.getEquipmentState());
        assertEquals(nextRoundSample.getWind(),gameState.getWind());
    }

    @Test
    void testActualiserCheckpointsWhenOut() {
        Checkpoint mockedCheckpoint = mock(Checkpoint.class);
        when(mockedCheckpoint.isPosInside(anyDouble(),anyDouble())).thenReturn(false);

        List<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(mockedCheckpoint);
        checkpoints.addAll(goal.getCheckpoints());

        GameState gState = new GameState(gameState.getPositionBateau(),gameState.getVieBateau(),gameState.getOrgaMarins(),
                gameState.getEquipmentState(), checkpoints);

        gState.actualiserCheckpoints();
        assertEquals(mockedCheckpoint,gState.getNextCheckpoint());
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

        gState.actualiserCheckpoints();
        assertEquals(goal.getCheckpoints().get(0),gState.getNextCheckpoint());
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