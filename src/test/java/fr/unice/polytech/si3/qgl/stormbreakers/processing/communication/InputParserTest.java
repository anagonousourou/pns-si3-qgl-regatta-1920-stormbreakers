package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.NextRound;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.RegattaGoal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;

class InputParserTest {

        private InputParser parser;
        private String initGameExample;
        private String nextRoundExample;

        private String init2Checkpoints;

        @BeforeEach
        void SetUp() {
                parser = new InputParser();
                initGameExample = "{\"goal\": {\"mode\": \"REGATTA\",\"checkpoints\": [{\"position\": {\"x\": 1000,\"y\": 0,\"orientation\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 50}}]},\"shipCount\": 1,\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 2,\"length\": 1},\"entities\": [{\"x\": 0,\"y\": 0,\"type\": \"oar\"},{\"x\": 1,\"y\": 0,\"type\": \"oar\"}],\"shape\": {\"type\": \"rectangle\",\"width\": 2,\"height\": 3,\"orientation\": 0}},\"sailors\": [{\"x\": 0,\"y\": 0,\"id\": 0,\"name\": \"Edward Teach\"},{\"x\": 0,\"y\": 1,\"id\": 1,\"name\": \"Tom Pouce\"}]}";
                nextRoundExample = "{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 10.654,\"y\": 3,\"orientation\": 2.05},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 2,\"length\": 1},\"entities\": [{\"x\": 0,\"y\": 0,\"type\": \"oar\"},{\"x\": 1,\"y\": 0,\"type\": \"oar\"}]},\"visibleEntities\": []}";

                init2Checkpoints = "{\"goal\": {\"mode\": \"REGATTA\",\"checkpoints\": [{\"position\": {\"x\": 1000,\"y\": 0,\"orientation\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 50}},{\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 50}}]},\"shipCount\": 1,\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 2,\"length\": 1},\"entities\": [{\"x\": 0,\"y\": 0,\"type\": \"oar\"},{\"x\": 1,\"y\": 0,\"type\": \"oar\"}],\"shape\": {\"type\": \"rectangle\",\"width\": 2,\"height\": 3,\"orientation\": 0}},\"sailors\": [{\"x\": 0,\"y\": 0,\"id\": 0,\"name\": \"Edward Teach\"},{\"x\": 0,\"y\": 1,\"id\": 1,\"name\": \"Tom Pouce\"}]}";
        }

        @Test
        void testFetchInitGameState() {
                InitGame init = parser.fetchInitGameState(initGameExample);

                RegattaGoal goal = (RegattaGoal) init.getGoal();
                assertAll("goal", () -> assertEquals("REGATTA", init.getGoal().getMode()),
                                () -> assertEquals(1, goal.getCheckpoints().size()));

                Bateau ship = init.getShip();

                assertAll("ship", () -> assertEquals(100, ship.getLife()),
                                () -> assertEquals("rectangle", ship.getShape().getType()),
                                () -> assertAll("Position", () -> assertEquals(0, ship.getPosition().getX()),
                                                () -> assertEquals(0, ship.getPosition().getY()),
                                                () -> assertEquals(0, ship.getPosition().getOrientation())));

                assertAll("personal ship", () -> assertEquals("Les copaings d'abord!", ship.getName()),
                                () -> assertEquals(1, ship.getDeck().getLength()),
                                () -> assertEquals(2, ship.getDeck().getWidth()),
                                () -> assertEquals(2, ship.getEquipments().size()));

                assertEquals(2, init.getSailors().size());
                assertEquals(1, init.getShipCount());

        }

        @Test
        void testFetchNextRoundState() {
                NextRound next = parser.fetchNextRoundState(nextRoundExample);

                assertEquals(100, next.getShip().getLife());
                Position pos = next.getShip().getPosition();
                assertAll("Position",
                                // TODO: 23/01/2020 override equals in Position
                                () -> assertEquals(10.654, pos.getX()), () -> assertEquals(3, pos.getY()),
                                () -> assertEquals(2.05, pos.getOrientation()));

                assertEquals(0, next.getVisibleEntities().size());

        }

        @Test
        void testFetchInitWhen2Checkpoints() {
                InitGame init = parser.fetchInitGameState(init2Checkpoints);

                RegattaGoal goal = (RegattaGoal) init.getGoal();
                assertAll("goal", () -> assertEquals("REGATTA", init.getGoal().getMode()),
                                () -> assertEquals(2, goal.getCheckpoints().size()));

                List<Checkpoint> checkpoints = goal.getCheckpoints();
                assertAll("checkpoint 1", () -> assertEquals(1000, checkpoints.get(0).getPosition().getX()),
                                () -> assertEquals(0, checkpoints.get(0).getPosition().getY()),
                                () -> assertEquals(0, checkpoints.get(0).getPosition().getOrientation()));

                assertAll("checkpoint 2", () -> assertEquals(0, checkpoints.get(1).getPosition().getX()),
                                () -> assertEquals(0, checkpoints.get(1).getPosition().getY()),
                                () -> assertEquals(0, checkpoints.get(1).getPosition().getOrientation()));

        }
}