package fr.unice.polytech.si3.qgl.stormbreakers.data.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.RegattaGoal;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;

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
        assertEquals(initGameSample.getShip().getPosition(), gState.getPositionBateau());
        assertEquals(initGameSample.getShip().getLife(), gState.getVieBateau());
        assertEquals(initGameSample.getSailors(), gState.getOrgaMarins());
        assertEquals(initGameSample.getShip().getEquipments(), gState.getEquipmentState());

        assertEquals(0.0, gState.getWind().getOrientation());
        assertEquals(0.0, gState.getWind().getStrength());

        assertEquals(goal.getCheckpoints().get(0), gState.getNextCheckpoint());
    }

    @Test
    void testActualiserTour() {
        gameState.updateTurn(nextRoundSample);
        //seul la position nous intéresse pour le moment
        assertEquals(nextRoundSample.getShip().getPosition(), gameState.getPositionBateau());
    }

    @Test
    void testActualiserCheckpointsWhenOut() {
        Checkpoint mockedCheckpoint = mock(Checkpoint.class);
        when(mockedCheckpoint.isPtInside(any(Point2D.class))).thenReturn(false);

        List<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(mockedCheckpoint);
        checkpoints.addAll(goal.getCheckpoints());

        GameState gState = new GameState(gameState.getShip(), gameState.getOrgaMarins(), checkpoints);

        gState.actualiserCheckpoints();
        assertEquals(mockedCheckpoint, gState.getNextCheckpoint());
    }

    @Test
    void testActualiserCheckpointsWhenIn() {
        Checkpoint mockedCheckpoint = mock(Checkpoint.class);
        when(mockedCheckpoint.isPtInside(any(Point2D.class))).thenReturn(true);

        List<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(mockedCheckpoint);
        checkpoints.addAll(goal.getCheckpoints());

        GameState gState = new GameState(gameState.getShip(), gameState.getOrgaMarins(), checkpoints);

        gState.actualiserCheckpoints();
        assertEquals(goal.getCheckpoints().get(0), gState.getNextCheckpoint());
    }

    @Test
    void testActualiserDeplacementsXY() {
        List<MoveAction> deplacements = new ArrayList<>();
        deplacements.add(new MoveAction(0, 1, 1));
        gameState.actualiserDeplacements(deplacements);

        Marin premierMarin = gameState.getOrgaMarins().get(0);
        assertEquals(0, premierMarin.getId());
        assertEquals(1, premierMarin.getX());
        assertEquals(1, premierMarin.getY());
    }

}