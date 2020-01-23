package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

import fr.unice.polytech.si3.qgl.stormbreakers.data.game.NextRound;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputParserTest {

    private InputParser parser;
    private String nextRoundExample;

    @BeforeEach
    void SetUp() {
        parser = new InputParser();
        nextRoundExample = "{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 10.654,\"y\": 3,\"orientation\": 2.05},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 2,\"length\": 1},\"entities\": [{\"x\": 0,\"y\": 0,\"type\": \"oar\"},{\"x\": 1,\"y\": 0,\"type\": \"oar\"}]},\"visibleEntities\": []}";
    }

    @Test
    void fetchNextRoundState() {
        NextRound next = parser.fetchNextRoundState(nextRoundExample);

        assertEquals(100, next.getShip().getLife());
        Position pos = next.getShip().getPosition();
        assertAll("Position",
                // TODO: 23/01/2020 override equals in Position
                () -> assertEquals(10.654,pos.getX()),
                () -> assertEquals(3,pos.getY()),
                () -> assertEquals(2.05,pos.getOrientation())
        );

        assertEquals(0, next.getVisibleEntities().size());


    }
}