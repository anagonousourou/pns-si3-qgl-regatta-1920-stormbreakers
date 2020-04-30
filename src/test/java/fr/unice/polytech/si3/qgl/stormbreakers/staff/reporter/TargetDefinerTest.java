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

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Stream;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Reef;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;
import fr.unice.polytech.si3.qgl.stormbreakers.io.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.io.json.JsonInputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.math.graph.Graph;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Navigator;

public class TargetDefinerTest {

  private CheckpointsManager checkpointsManager;

  private StreamManager streamManager;
  private Boat boat;

  private InputParser parser = new JsonInputParser();
  private Stream courant1 = new Stream(new Position(500.0, 0.0, 0.0), new Rectangle(300, 600, 0.0), 40.0);
  private Stream courant2 = new Stream(new Position(900.0, 900.0, -0.52), new Rectangle(300, 600, 0.0), 80.0);
  private Stream courant3 = new Stream(new Position(500.0, 0.0, 0.0), new Rectangle(400, 600, 0.0), 100.0);
  private Stream courant4 = new Stream(new Position(500.0, 500.0, 0.78539), new Rectangle(400, 400, 0.0), 80.0);

  private Checkpoint cp1 = new Checkpoint(new Position(1500, 300), new Circle(50));
  private Checkpoint cp2 = new Checkpoint(new Position(300, 1500), new Circle(50));

  private Checkpoint cp3 = new Checkpoint(new Position(0, 0), new Circle(50));
  private Checkpoint cp4 = new Checkpoint(new Position(300, 300), new Circle(50));
  private Checkpoint cp5 = new Checkpoint(new Position(800, 800), new Circle(50));
  private Checkpoint cp6 = new Checkpoint(new Position(500, 100), new Circle(50));

  private TargetDefiner targetDefiner;
  private Navigator navigator;
  private Cartographer cartographer;
  private Graph graph;
  private WeatherAnalyst weatherAnalyst;
  private EquipmentsManager equipmentsManager;
  private Wind wind;

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
  void defineNextTargetTest() {
    checkpointsManager = mock(CheckpointsManager.class);
    boat = new Boat(new Position(0, 0), 5, 3, 3, parser);
    when(checkpointsManager.nextCheckpoint()).thenReturn(cp4);
    streamManager = new StreamManager(parser, boat);
    streamManager.setCourants(List.of(courant3, courant4));
    streamManager.setObstacles(List.of(courant3, courant4));
    

    wind = new Wind(null);

    equipmentsManager = mock(EquipmentsManager.class);

    when(equipmentsManager.nbOars()).thenReturn(10);

    weatherAnalyst = new WeatherAnalyst(wind, boat, equipmentsManager);
    graph = new Graph(streamManager, weatherAnalyst);
    cartographer = new Cartographer(checkpointsManager, graph, boat);
    targetDefiner = new TargetDefiner(checkpointsManager, streamManager, boat, navigator, cartographer);

    var reponse = targetDefiner.defineNextTarget();

    assertNotNull(reponse);

    //le checkpoint se trouve dans un stream et le bateau est hors du stream
    when(checkpointsManager.nextCheckpoint()).thenReturn(cp6);

    reponse = targetDefiner.defineNextTarget();

    assertNotNull(reponse);

    
    // le ep se trouve de l'autre coté du courant4
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


  @Test
  public void curveTrajectoryIsSafeTest(){
    Boat boat = mock(Boat.class);
    when(boat.x()).thenReturn(700.0);
    when(boat.y()).thenReturn(200.0);

    when(boat.getOrientation()).thenReturn(Math.PI/2);
    StreamManager streamManager=new StreamManager(parser, boat);
    Reef reef1=new Reef(new Position(600,400), new Rectangle(190*2, 100*2, 0));
    streamManager.setBoatsAndReefs(List.of(reef1));
    TargetDefiner targetDefiner=new TargetDefiner(null, streamManager, boat, navigator);

    assertFalse(targetDefiner.curveTrajectoryIsSafe(new TupleDistanceOrientation(200, Math.PI/2 )));

    assertTrue(targetDefiner.curveTrajectoryIsSafe(new TupleDistanceOrientation(200, -Math.PI/2 )));
  }

}