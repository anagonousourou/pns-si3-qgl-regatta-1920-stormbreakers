package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;

public class BoatTest {
    Boat boat;
    InputParser parser;

    @BeforeEach
    void setUp(){
        parser=mock(InputParser.class);
        boat=new Boat(new Position(400,50.6), 6, 3, 50,parser );
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



    
}