package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Navigator;

public class TargetDefinerTest {
    
    
    private CheckpointsManager checkpointsManager;
    
    private StreamManager streamManager;
    private Boat boat;
    
    private InputParser parser = new InputParser();
    private Courant courant1=new Courant(new Position(500.0, 0.0,0.0), new Rectangle(300, 600, 0.0) , 40.0);
    private Courant courant2=new Courant(new Position(900.0, 900.0,-0.52), new Rectangle(300, 600, 0.0) , 80.0);

    private Checkpoint cp1=new Checkpoint(Position.create(1500, 300) , new Circle(50));
    private Checkpoint cp2=new Checkpoint(Position.create(300, 1500) , new Circle(50));
    
    private Checkpoint cp3=new Checkpoint(Position.create(0, 0) , new Circle(50));
    
    private TargetDefiner targetDefiner;
    private Navigator navigator;
    
    @BeforeEach
    void setUp(){
        
    }

    @Test
    void streamOnTrajectoryTest(){
        checkpointsManager=new CheckpointsManager(List.of(cp1,cp2,cp3) );
        
        boat=mock(Boat.class);
        streamManager=new StreamManager(parser, boat);
        navigator=new Navigator();
        streamManager.setCourants(List.of(courant1,courant2) );
        targetDefiner=new TargetDefiner(checkpointsManager, streamManager, boat, navigator);

        when(boat.getPosition()).thenReturn(Position.create(0, 0));
        assertTrue(targetDefiner.thereIsStreamOnTrajectory());

        when(boat.getPosition()).thenReturn(Position.create(300, 300));
        assertFalse(targetDefiner.thereIsStreamOnTrajectory());
    }

    @Test
    void nextStreamOnTrajectoryTest() {

        checkpointsManager=new CheckpointsManager(List.of(cp1,cp2,cp3) );
        
        boat=mock(Boat.class);
        streamManager=new StreamManager(parser, boat);
        navigator=new Navigator();
        streamManager.setCourants(List.of(courant1,courant2) );
        targetDefiner=new TargetDefiner(checkpointsManager, streamManager, boat, navigator);

        when(boat.getPosition()).thenReturn(Position.create(0, 0));
        assertEquals(courant1, targetDefiner.nextStreamOnTrajectory());
      }
    
}