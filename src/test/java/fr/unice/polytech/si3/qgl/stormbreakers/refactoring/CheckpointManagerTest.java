package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

public class CheckpointManagerTest {

    CheckpointManager checkpointManager;
    Checkpoint c1=new Checkpoint(new Position(147.68, 15.69),
    new Rectangle(100, 50, 0.36) );

    Checkpoint c2=new Checkpoint(new Position(75,23.05), new Circle(20) );
    @BeforeEach
    void setUp(){
        List<Checkpoint> checkpoints=List.of(c1,c2);
        
        checkpointManager=new CheckpointManager(checkpoints);
    }


    @Test
    void checkpointTest(){
        assertEquals(c1,checkpointManager.nextCheckpoint());

        checkpointManager.updateCheckpoint(new Position(75,23));

        assertEquals(c1,checkpointManager.nextCheckpoint());

        checkpointManager.updateCheckpoint(new Position(147,23));

        assertEquals(c2,checkpointManager.nextCheckpoint());

        checkpointManager.updateCheckpoint(new Position(90,30));

        assertEquals(null,checkpointManager.nextCheckpoint());
        

    }
    
}