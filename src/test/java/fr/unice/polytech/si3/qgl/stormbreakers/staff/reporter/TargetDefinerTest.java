package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
  private Courant courant1 = new Courant(new Position(500.0, 0.0, 0.0), new Rectangle(300, 600, 0.0), 40.0);
  private Courant courant2 = new Courant(new Position(900.0, 900.0, -0.52), new Rectangle(300, 600, 0.0), 80.0);
  private Courant courant3 = new Courant(new Position(500.0, 0.0, 0.0), new Rectangle(400, 600, 0.0), 40.0);
  private Courant courant4 = new Courant(new Position(500.0, 500.0, 0.78539), new Rectangle(400, 400, 0.0), 80.0);

  private Checkpoint cp1 = new Checkpoint(new Position(1500, 300), new Circle(50));
  private Checkpoint cp2 = new Checkpoint(new Position(300, 1500), new Circle(50));

  private Checkpoint cp3 = new Checkpoint(new Position(0, 0), new Circle(50));
  private Checkpoint cp4 = new Checkpoint(new Position(300, 300), new Circle(50));
  private Checkpoint cp5 = new Checkpoint(new Position(800, 800), new Circle(50));
  private Checkpoint cp6 = new Checkpoint(new Position(500, 100), new Circle(50));

  private TargetDefiner targetDefiner;
  private Navigator navigator;

  @BeforeEach
  void setUp() {
    navigator = new Navigator();
  }

  @Test
  void streamOnTrajectoryTest() {
    checkpointsManager = new CheckpointsManager(List.of(cp1, cp2, cp3));

    boat = new Boat(Position.create(0.0, 0.0, 0.0), 2, 2, 100, null);
    streamManager = new StreamManager(parser, boat);

    streamManager.setCourants(List.of(courant1, courant2));
    targetDefiner = new TargetDefiner(checkpointsManager, streamManager, boat, navigator);

    assertTrue(targetDefiner.thereIsStreamOnTrajectory());

    // clear

    boat = new Boat(Position.create(300.0, 300.0, 0.0), 2, 2, 100, null);
    streamManager = new StreamManager(parser, boat);
    streamManager.setCourants(List.of(courant1, courant2));
    targetDefiner = new TargetDefiner(checkpointsManager, streamManager, boat, navigator);

    assertFalse(targetDefiner.thereIsStreamOnTrajectory());
  }

  @Test
  void nextStreamOnTrajectoryTest() {

    checkpointsManager = new CheckpointsManager(List.of(cp1, cp2, cp3));

    boat = mock(Boat.class);
    streamManager = new StreamManager(parser, boat);
    navigator = new Navigator();
    streamManager.setCourants(List.of(courant1, courant2));
    targetDefiner = new TargetDefiner(checkpointsManager, streamManager, boat, navigator);

    when(boat.getPosition()).thenReturn(Position.create(0, 0));
    assertEquals(courant1, targetDefiner.nextStreamOnTrajectory());
  }

  @Test
  void caseInsideAStreamPerpendiculaireTest() {
    checkpointsManager = new CheckpointsManager(List.of(cp4, cp1, cp2, cp3));

    boat = new Boat(Position.create(300.0, 100.0, 0.0), 2, 2, 100, null);

    streamManager = new StreamManager(parser, boat);
    navigator = new Navigator();
    streamManager.setCourants(List.of(courant1, courant2));
    targetDefiner = new TargetDefiner(checkpointsManager, streamManager, boat, navigator);

    var result = targetDefiner.caseInsideAStream();
    assertEquals(Math.PI / 2, result.getOrientation(), 1e-3);
    assertEquals(240, result.getDistance(), 1e-3);
  }

  @Test
  void caseInsideAHelpingStreamTest() {
    checkpointsManager = new CheckpointsManager(List.of(cp1, cp2, cp3));

    boat = new Boat(new Position(300.0, 100.0, 0.0), 2, 2, 100, null);
    streamManager = new StreamManager(parser, boat);
    navigator = new Navigator();
    streamManager.setCourants(List.of(courant1, courant2));
    targetDefiner = new TargetDefiner(checkpointsManager, streamManager, boat, navigator);

    var result = targetDefiner.caseInsideAStream();
    assertEquals(0, result.getOrientation(), 1e-3);
  }

  @Test
  void caseInsideAStreamTest() {
    Boat boat = new Boat(new Position(100, 60), 2, 5, 100, parser);

    checkpointsManager = mock(CheckpointsManager.class);
    var cp = new Checkpoint(new Position(120, 60), new Circle(5));
    when(checkpointsManager.nextCheckpoint()).thenReturn(cp);

    streamManager = new StreamManager(parser, boat);

    Courant courant = new Courant(new Position(60, 60), new Rectangle(40, 100, 0.0), 150);
    streamManager.setCourants(List.of(courant));
    navigator = new Navigator();
    targetDefiner = new TargetDefiner(checkpointsManager, streamManager, boat, navigator);

    var result = targetDefiner.caseInsideAStream();

    assertTrue(result.getDistance() < 0);
    assertEquals(0.0, result.getOrientation(), 1e-3, "Déja dans le bon sens pas besoin de pivoter");

  }

  @Test

  void caseInsideAStreamOnEdgeTest() {
    Boat boat = new Boat(new Position(110, 60), 2, 5, 100, parser);

    checkpointsManager = mock(CheckpointsManager.class);
    var cp = new Checkpoint(new Position(120, 60), new Circle(5));
    when(checkpointsManager.nextCheckpoint()).thenReturn(cp);

    streamManager = new StreamManager(parser, boat);

    Courant courant = new Courant(new Position(60, 60), new Rectangle(40, 100, 0.0), 150);
    streamManager.setCourants(List.of(courant));
    navigator = new Navigator();
    targetDefiner = new TargetDefiner(checkpointsManager, streamManager, boat, navigator);

    var result = targetDefiner.caseInsideAStream();

    assertTrue(result.getDistance() < 0);
    assertEquals(0.0, result.getOrientation(), 1e-3, "Déja dans le bon sens pas besoin de pivoter");

  }

  @Test
  void defineNextTargetTest() {
    checkpointsManager = mock(CheckpointsManager.class);
    boat = new Boat(new Position(0, 0), 5, 3, 3, parser);
    when(checkpointsManager.nextCheckpoint()).thenReturn(cp4);
    streamManager = new StreamManager(parser, boat);
    streamManager.setCourants(List.of(courant3, courant4));
    streamManager.setObstacles(List.of(courant3, courant4));
    targetDefiner = new TargetDefiner(checkpointsManager, streamManager, boat, navigator);

    var reponse = targetDefiner.defineNextTarget();

    assertNotNull(reponse);

    when(checkpointsManager.nextCheckpoint()).thenReturn(cp6);

    reponse = targetDefiner.defineNextTarget();

    assertNotNull(reponse);

    when(checkpointsManager.nextCheckpoint()).thenReturn(cp5);

    reponse = targetDefiner.defineNextTarget();

    assertNotNull(reponse);

    boat.setPosition(new Position(1000, 100));

    when(checkpointsManager.nextCheckpoint()).thenReturn(cp4);

    reponse = targetDefiner.defineNextTarget();

    assertNotNull(reponse);

    // boat inside stream and checkpoint inside, stream have favorable direction

    boat.setPosition(new Position(300, 100));

    when(checkpointsManager.nextCheckpoint()).thenReturn(cp6);

    reponse = targetDefiner.defineNextTarget();

    assertNotNull(reponse);

    // boat inside stream and checkpoint inside, stream have defavorable direction

    boat.setPosition(new Position(700, 100));

    when(checkpointsManager.nextCheckpoint()).thenReturn(cp6);

    reponse = targetDefiner.defineNextTarget();

    assertNotNull(reponse);

    // boat inside stream checkpoint outside, stream have defavorable orientation

    boat.setPosition(new Position(600, 100));

    when(checkpointsManager.nextCheckpoint()).thenReturn(cp4);

    reponse = targetDefiner.defineNextTarget();

    assertNotNull(reponse);

    // no stream reefs on the trajectory

    boat.setPosition(new Position(0, 0));

    when(checkpointsManager.nextCheckpoint()).thenReturn(cp5);
    streamManager.setObstacles(List.of());
    streamManager.setCourants(List.of());
    reponse = targetDefiner.defineNextTarget();

    assertNotNull(reponse);

  }

}