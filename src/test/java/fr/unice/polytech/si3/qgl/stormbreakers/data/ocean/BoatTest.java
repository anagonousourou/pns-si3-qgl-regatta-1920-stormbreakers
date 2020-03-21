package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.ObservableData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;

import java.io.IOException;

public class BoatTest {
    private Boat boat;
    private InputParser parser;
    private String boatJsonRound;
    private Position boatJsonPos;
    private Position boatInitPosition;

    @BeforeEach
    void setUp() throws IOException {
        boatJsonRound = new String(this.getClass().getResourceAsStream("/shiptest/ship.json").readAllBytes());
        boatJsonPos = new Position(80,100,30);
        parser=new InputParser();
        boatInitPosition = new Position(400,50.6);
        boat=new Boat(boatInitPosition, 6, 3, 50,parser);
    }

    @Test
    void positionTest(){
        assertEquals(new Position(400,50.6),boat.getPosition() );

        boat.setPosition(new Position(500,63.9));
        assertEquals(new Position(500,63.9),boat.getPosition() );
    }

    @Test
    void deckTest(){
        assertEquals(3, boat.getDeckwidth(), "is 3");
        assertEquals(6, boat.getDecklength(), "is 6");
    }

    @Test
    void lifeTest(){
        assertEquals(50, boat.getLife());

        boat.setLife(30);

        assertEquals(30, boat.getLife());
    }

    @Test
    void testOnPropertyChange() {
        ObservableData observableData = new ObservableData();
        observableData.addPropertyChangeListener(boat);
        observableData.setValue(boatJsonRound);

        assertEquals(boatJsonPos,boat.getPosition());
        assertNotEquals(boatInitPosition,boat.getShape().getAnchor());
        assertEquals(boatJsonPos,boat.getShape().getAnchor());

    }



    
}