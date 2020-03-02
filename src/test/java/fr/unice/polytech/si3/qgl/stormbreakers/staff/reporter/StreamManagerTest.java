package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;

public class StreamManagerTest {

    private StreamManager manager;
    
    private InputParser parser = new InputParser();
    private Courant courant1=new Courant(new Position(500.0, 0.0,0.0), new Rectangle(300, 600, 0.0) , 40.0);
    private Courant courant2=new Courant(new Position(900.0, 900.0,-0.52), new Rectangle(300, 600, 0.0) , 80.0);

    

    @Test
    void insideStreamTest(){
        Boat boat=mock(Boat.class);
        List<Courant> courants=List.of(
            courant1,courant2
        );
        manager=new StreamManager(parser, boat);
        manager.setCourants(courants);

        when( boat.getPosition() ).thenReturn(new Position(300, 300));

        assertFalse(manager.insideStream());

        when( boat.getPosition() ).thenReturn(new Position(300, 100));

        assertTrue(manager.insideStream());

        when( boat.getPosition() ).thenReturn(new Position(500, 0.0));

        assertTrue(manager.insideStream());
    }

    @Test
    void streamAroundBoatTest(){
        Boat boat=mock(Boat.class);
        List<Courant> courants=List.of(
            courant1,courant2
        );
        manager=new StreamManager(parser, boat);
        manager.setCourants(courants);

        when( boat.getPosition() ).thenReturn(new Position(300, 300));

        assertEquals(null,manager.streamAroundBoat());

        when( boat.getPosition() ).thenReturn(new Position(800, 100));

        assertEquals(courant1,manager.streamAroundBoat());

        when( boat.getPosition() ).thenReturn(new Position(500, 0.0));

        assertEquals(courant1,manager.streamAroundBoat());
        
    }

    @Test
    void thereIsStreamBetweenTest(){
        Boat boat=mock(Boat.class);
        List<Courant> courants=List.of(
            courant1,courant2
        );
        manager=new StreamManager(parser, boat);
        manager.setCourants(courants);

        when( boat.getPosition() ).thenReturn(new Position(0.0, 0.0));

        assertFalse(manager.thereIsStreamBetween(new Position(300, 300)));
        assertTrue(manager.thereIsStreamBetween(new Position(900, 100)));
        assertTrue(manager.thereIsStreamBetween(new Position(500, 100)));
        assertFalse(manager.thereIsStreamBetween(new Position(-300, 100)));


    }
    @Test
    void firstStreamBetweenTest(){

        Boat boat=mock(Boat.class);
        List<Courant> courants=List.of(
            courant1,courant2
        );
        manager=new StreamManager(parser, boat);
        manager.setCourants(courants);

        when( boat.getPosition() ).thenReturn(new Position(700, -300.0));

        assertEquals(courant1, manager.firstStreamBetween(Position.create(700, 1500)));

        assertEquals(null, manager.firstStreamBetween(Position.create(1800, 300)));

        when( boat.getPosition() ).thenReturn(new Position(700, 1300.0));

        assertEquals(courant2, manager.firstStreamBetween(Position.create(700, 600)));

    }
}