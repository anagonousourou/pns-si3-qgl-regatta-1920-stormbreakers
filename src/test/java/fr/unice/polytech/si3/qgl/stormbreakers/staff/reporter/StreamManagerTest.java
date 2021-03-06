package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Reef;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Stream;
import fr.unice.polytech.si3.qgl.stormbreakers.io.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.io.json.JsonInputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;

public class StreamManagerTest {

    private StreamManager manager;

    private InputParser parser = new JsonInputParser();
    private Stream stream1 = new Stream(new Position(500.0, 0.0, 0.0), new Rectangle(300, 600, 0.0), 40.0);
    private Stream stream2 = new Stream(new Position(900.0, 900.0, -0.52), new Rectangle(300, 600, 0.0), 80.0);
    private Stream stream3 = new Stream(new Position(500.0, 0.0, 0.0), new Rectangle(400, 600, 0.0), 40.0);
    private Stream stream4 = new Stream(new Position(500.0, 500.0, 0.78539), new Rectangle(400, 400, 0.0), 80.0);
    private Reef reefTriangle = new Reef(new Position(-200, 300.0, 0),
            new Polygon(0.0, List.of(new Point2D(100, 100), new Point2D(0, -100), new Point2D(-100, 100))));

    private Reef reefCircle = new Reef(new Position(300, 600), new Circle(200));
    private Reef reefSquare = new Reef(new Position(500, 200), new Rectangle(200, 200, 0));

    private Stream streamSquare = new Stream(new Position(500, 200), new Rectangle(200, 200, 0), 100);

    Position pointA = new Position(200, 200);
    Position pointB = new Position(500, 400);
    Position pointC = new Position(800, 200);
    Position pointD = new Position(-300, 300);
    Position pointE = new Position(200, 900);
    Position pointF = new Position(-100, 600);
    Position pointG = new Position(-200, 700);
    Position pointH = new Position(200, 300);
    Position pointI = new Position(800, 800);

    @Test
    void insideStreamTest() {
        Boat boat = mock(Boat.class);
        List<Stream> streams = List.of(stream1, stream2);
        manager = new StreamManager(parser, boat);
        manager.setStreams(streams);

        when(boat.x()).thenReturn(300.0);
        when(boat.y()).thenReturn(300.0);
        when(boat.getOrientation()).thenReturn(0.0);

        assertFalse(manager.insideOpenStream());

        when(boat.x()).thenReturn(300.0);
        when(boat.y()).thenReturn(100.0);

        assertTrue(manager.insideOpenStream());

        /*when(boat.x()).thenReturn(500.0);
        when(boat.y()).thenReturn(0.0);

        assertTrue(manager.insideOpenStream());*/
    }

    @Test
    void streamAroundBoatTest() {
        Boat boat = mock(Boat.class);
        List<Stream> streams = List.of(stream1, stream2);
        manager = new StreamManager(parser, boat);
        manager.setStreams(streams);

        when(boat.x()).thenReturn(300.0);
        when(boat.y()).thenReturn(300.0);

        assertEquals(null, manager.streamAroundBoat());

        when(boat.x()).thenReturn(800.0);
        when(boat.y()).thenReturn(100.0);

        assertEquals(stream1, manager.streamAroundBoat());

        when(boat.x()).thenReturn(500.0);
        when(boat.y()).thenReturn(0.0);

        assertEquals(stream1, manager.streamAroundBoat());

    }

    @Test
    void thereIsStreamBetweenTest() {
        Boat boat = mock(Boat.class);
        List<Stream> streams = List.of(stream1, stream2);
        manager = new StreamManager(parser, boat);
        manager.setStreams(streams);

        when(boat.getPosition()).thenReturn(new Position(0.0, 0.0));

        assertFalse(manager.thereIsStreamBetween(new Position(300, 300)));
        assertTrue(manager.thereIsStreamBetween(new Position(900, 100)));
        assertTrue(manager.thereIsStreamBetween(new Position(500, 100)));
        assertFalse(manager.thereIsStreamBetween(new Position(-300, 100)));

    }

    @Test
    void firstStreamBetweenTest() {

        Boat boat = mock(Boat.class);
        List<Stream> courants = List.of(stream1, stream2);
        manager = new StreamManager(parser, boat);
        manager.setStreams(courants);

        when(boat.x()).thenReturn(700.0);
        when(boat.y()).thenReturn(-300.0);

        assertEquals(stream1, manager.firstStreamBetween(Position.create(700, 1500)));

        assertEquals(null, manager.firstStreamBetween(Position.create(1800, 300)));

        when(boat.x()).thenReturn(700.0);
        when(boat.y()).thenReturn(1300.0);

        assertEquals(stream2, manager.firstStreamBetween(Position.create(700, 600)));

    }

    @Test
    void thereIsObstacleBetweenTest() {

        StreamManager streamManager = new StreamManager(parser, null);
        streamManager.setObstacles(List.of(reefSquare, reefCircle, reefTriangle));

        assertTrue(streamManager.thereIsObstacleBetween(pointA, pointC));
        // assertTrue(streamManager.thereIsObstacleBetween(pointA, pointE));
        assertTrue(streamManager.thereIsObstacleBetween(pointA, pointD));
        assertFalse(streamManager.thereIsObstacleBetween(pointA, pointB));

    }

    @Test
    void pointIsInsideStreamTest() {
        StreamManager streamManager = new StreamManager(parser, null);
        streamManager.setObstacles(List.of(reefSquare, reefCircle, reefTriangle));
        streamManager.setStreams(List.of(streamSquare));
        assertFalse(streamManager.pointIsInsideStream(pointA));
        assertFalse(streamManager.pointIsInsideStream(pointB));
        assertFalse(streamManager.pointIsInsideStream(pointC));
        assertFalse(streamManager.pointIsInsideStream(pointE));
        assertTrue(streamManager.pointIsInsideStream(new Position(500, 200)));
        assertTrue(streamManager.pointIsInsideStream(new Position(510, 190)));
    }

    @Test
    void streamAroundPointTest() {
        StreamManager streamManager = new StreamManager(parser, null);

        streamManager.setStreams(List.of(stream3, stream4));
        assertTrue(streamManager.streamAroundPoint(new Position(300, 300)).isEmpty());

        var optc3 = streamManager.streamAroundPoint(new Position(500, 100));
        assertTrue(optc3.isPresent());
        assertEquals(stream3, optc3.get(), "Le point est dans le courant 3");

        var optc4 = streamManager.streamAroundPoint(new Position(500, 300));
        assertTrue(optc4.isPresent());
        assertEquals(stream4, optc4.get(), "Le point est dans le courant 4");

    }

    @Test
    void firstObstacleBetweenTest() {
        StreamManager streamManager = new StreamManager(parser, null);
        streamManager.setObstacles(List.of(reefSquare, reefCircle, reefTriangle));

        assertEquals(reefTriangle, streamManager.firstObstacleBetween(pointD, pointC));
        assertEquals(reefSquare, streamManager.firstObstacleBetween(pointC, pointD));
        assertEquals(reefSquare, streamManager.firstObstacleBetween(pointA, pointC));

    }

    @Test
    void trajectoryLeaveStreamAndReachPointTest() {
        StreamManager streamManager = new StreamManager(parser, null);

        streamManager.setStreams(List.of(stream3, stream4));

        var trajectory = streamManager.trajectoryLeaveStreamAndReachPoint(new Position(600, 600), pointH);

        assertEquals(3, trajectory.size());

        assertFalse(streamManager.pointIsInsideStream(trajectory.get(1)));

        trajectory = streamManager.trajectoryLeaveStreamAndReachPoint(new Position(600, 600), pointI);

        assertEquals(2, trajectory.size());
    }

    @Test
    void trajectoryBoatAndCheckpointInsideStreamTest() {
        StreamManager streamManager = new StreamManager(parser, null);

        streamManager.setStreams(List.of(stream3, stream4));

        var trajectory = streamManager.trajectoryBoatAndCheckpointInsideStream(new Position(400, 400),
                new Position(600, 600));

        assertEquals(2, trajectory.size());

    }

    @Test
    public void speedProvidedLimitsTest() {

        StreamManager streamManager = new StreamManager(parser, null);

        streamManager.setStreams(List.of(stream3, stream4));

        assertEquals(0.0, streamManager.speedProvidedLimits(new Point2D(300, 200), new Point2D(350, 200)), 1e-3);

        assertTrue(streamManager.speedProvidedLimits(new Point2D(300, 200), new Point2D(350, 100)) > 1e-3);

    }

    @Test
    public void speedProvidedTest() {
        StreamManager streamManager = new StreamManager(parser, null);

        streamManager.setStreams(List.of(stream3, stream4));
        // segment completement dans le courant4
        assertEquals(stream4.getStrength(),
                streamManager.speedProvided(new Position(400, 400), new Position(600, 600)), 1e-3);

        // segment perpendiculiare au courant4
        assertEquals(0.0, streamManager.speedProvided(new Position(600, 400), new Position(400, 600)), 1e-3);

        assertEquals(stream4.getStrength() * Math.cos(stream4.getPosition().getOrientation()),
                streamManager.speedProvided(new Position(500, 500), new Position(500, 600)), 1e-3);

        assertEquals(stream4.getStrength(),
                streamManager.speedProvided(new Position(500, 500), new Position(700, 700)), 1e-3);

        assertEquals(-stream4.getStrength(),
                streamManager.speedProvided(new Position(700, 700), new Position(500, 500)), 1e-3);

        assertEquals(stream4.getStrength() * Math.cos(stream4.getPosition().getOrientation()),
                streamManager.speedProvided(new Position(500, 700), new Position(499.64, 782.16)), 1.0);

        assertEquals(0.0, streamManager.speedProvided(new Position(300, 200), new Position(400, 200)), 1e-3);

        // le segment se trouve à l'extérieur avec un bord sur le courant3
        assertEquals(0.0, streamManager.speedProvided(new Position(100, 100), new Position(200, 100)), 1e-3);

        // le segment se trouve à l'intérieur du courant3
        assertEquals(stream3.getStrength(),
                streamManager.speedProvided(new Position(300, 199), new Position(400, 199)), 1e-3);

        assertEquals(stream3.getStrength() * Math.cos(Math.PI/4),
                streamManager.speedProvided(new Position(700, 100), new Position(800, 200)), 1);

    }
}