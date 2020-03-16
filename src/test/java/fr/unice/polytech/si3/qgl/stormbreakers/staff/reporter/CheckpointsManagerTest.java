package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.*;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

public class CheckpointsManagerTest {

    CheckpointsManager checkpointsManager;

    Checkpoint c1=new Checkpoint(new Position(5,5), new Circle(5) );

    Checkpoint c2=new Checkpoint(new Position(10,10), new Circle(5) );

    Shape boatShape;
    Boat boat;

    @BeforeEach
    void setUp(){
        List<Checkpoint> checkpoints=List.of(c1,c2);
        checkpointsManager=new CheckpointsManager(checkpoints);

        List<Point2D> vertices = new ArrayList<>();
        vertices.add(new Point2D(2.0,0.0));
        vertices.add(new Point2D(1.0,1.0));
        vertices.add(new Point2D(-1.0,0.75));
        vertices.add(new Point2D(-1.0,-0.75));
        vertices.add(new Point2D(1.0,-1.0));
        boatShape = new Polygon(0.0,vertices);

        boat = new Boat(new Position(0,0),4,3,100,null,boatShape);
    }


    @Test
    void updateCheckpointTest(){
        assertEquals(c1,checkpointsManager.nextCheckpoint());

        // TODO: 12/03/2020 Redo updateCheckpoint test

        boatShape.setAnchor(new Position(-5,5));
        checkpointsManager.updateCheckpoint(boat); // Still same checkpoint
        assertEquals(c1,checkpointsManager.nextCheckpoint());

        boatShape.setAnchor(new Position(-1.5,5));
        checkpointsManager.updateCheckpoint(boat); // c1 validated
        assertEquals(c2,checkpointsManager.nextCheckpoint());

        boatShape.setAnchor(new Position(4,6,Math.toRadians(34)));
        checkpointsManager.updateCheckpoint(boat);  // Still same checkpoint
        assertEquals(c2,checkpointsManager.nextCheckpoint());

        boatShape.setAnchor(new Position(4.16,6.16,Math.toRadians(34)));
        checkpointsManager.updateCheckpoint(boat); // No more checkpoints

        assertNull(checkpointsManager.nextCheckpoint());
    }

    @Test
    void boatCollidesWithNextCheckpoint() {
        boatShape.setAnchor(new Position(-5,5));
        assertFalse(new CheckpointsManager(List.of(c1)).boatCollidesWithNextCheckpoint(boat));

        boatShape.setAnchor(new Position(-1.5,5));
        assertTrue(new CheckpointsManager(List.of(c1)).boatCollidesWithNextCheckpoint(boat));
        assertFalse(new CheckpointsManager(List.of(c2)).boatCollidesWithNextCheckpoint(boat));

        boatShape.setAnchor(new Position(4,6,Math.toRadians(34)));
        assertTrue(new CheckpointsManager(List.of(c1)).boatCollidesWithNextCheckpoint(boat));
        assertFalse(new CheckpointsManager(List.of(c2)).boatCollidesWithNextCheckpoint(boat));

        boatShape.setAnchor(new Position(4.16,6.16,Math.toRadians(34)));
        assertTrue(new CheckpointsManager(List.of(c1)).boatCollidesWithNextCheckpoint(boat));
        assertTrue(new CheckpointsManager(List.of(c2)).boatCollidesWithNextCheckpoint(boat));

    }
}