package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.ObservableData;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CheckpointsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.StreamManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;

public class CartographerTest {

    private Graph graph;
    private Boat boat;
    private Checkpoint checkpoint1;
    private StreamManager streamManager;
    private WeatherAnalyst weatherAnalyst;
    private CheckpointsManager checkpointsManager;
    private Recif reefRectangle1;
    private Recif reefTriangle;
    private Recif reefRectangle2;
    private Cartographer cartographer;
    private String round2;

    @BeforeEach
    public void setUp() throws IOException {
        reefTriangle = new Recif(new Position(700, 500),
                new Polygon(0.0, List.of(new Point2D(0, 200), new Point2D(0, 0), new Point2D(-200, 0))));
        reefRectangle1 = new Recif(new Position(350, 200), new Rectangle(200, 300, 0.0));
        reefRectangle2 = new Recif(new Position(1000, 500), new Rectangle(200, 400, 0.0));
        round2 = new String(this.getClass().getResourceAsStream("/observabletest/round2.json").readAllBytes());
    }

    @Test
    public void nextPointTest() {

        boat = new Boat(new Position(-5000, -5000), 5, 5, 5, null);
        streamManager = new StreamManager(null, boat);
        streamManager.setRecifs(List.of(reefRectangle1, reefRectangle2, reefTriangle));
        streamManager.setObstacles(List.of(reefRectangle1, reefRectangle2, reefTriangle));
        weatherAnalyst = new WeatherAnalyst(null, boat, null);
        checkpointsManager = mock(CheckpointsManager.class);

        checkpoint1 = new Checkpoint(new Position(1000, 700), new Circle(50));
        when(checkpointsManager.nextCheckpoint()).thenReturn(checkpoint1);

        graph = new Graph(streamManager, weatherAnalyst);

        
        cartographer = new Cartographer(checkpointsManager, graph, boat);
        assertFalse(cartographer.virtualMapExists());
        cartographer.nextPoint();
        graph.clearShortestPaths();
        
        assertTrue(cartographer.virtualMapExists());
        assertNotNull(cartographer.nextPoint());
    }

    @Test
    public void caseBuildMapTestNoWindNoStreams() {
        boat = new Boat(new Position(300, 500), 5, 5, 5, null);
        streamManager = new StreamManager(null, boat);
        streamManager.setRecifs(List.of(reefRectangle1, reefRectangle2, reefTriangle));
        streamManager.setObstacles(List.of(reefRectangle1, reefRectangle2, reefTriangle));
        weatherAnalyst = new WeatherAnalyst(null, boat, null);

        checkpointsManager = mock(CheckpointsManager.class);
        checkpoint1 = new Checkpoint(new Position(1000, 700), new Circle(50));

        graph = new Graph(streamManager, weatherAnalyst);
        cartographer = new Cartographer(checkpointsManager, graph, boat);

        var result = cartographer.caseBuildMap(checkpoint1);
        assertNotNull(result);
        
        assertNotEquals(checkpoint1, result, "Il doit y avoir une étape intermediaire");
    }


    @Test
    public void lotOfObstaclesTest(){
        boat = new Boat(new Position(2306.962592519181, 5190.275946479242), 5, 5, 5, null);
        InputParser parser=new InputParser();
        ObservableData observableData=new ObservableData();
        streamManager = new StreamManager(parser, boat);
        observableData.addPropertyChangeListener(streamManager);

        observableData.setValue(round2);
        
        weatherAnalyst = new WeatherAnalyst(null, boat, null);

        checkpointsManager = mock(CheckpointsManager.class);
        checkpoint1 = new Checkpoint(new Position(3600.0, 5160), new Circle(50));

        graph = new Graph(streamManager, weatherAnalyst);
        cartographer = new Cartographer(checkpointsManager, graph, boat);

        var result = cartographer.caseBuildMap(checkpoint1);
        assertNotNull(result);
        System.out.println(result);
        assertNotEquals(checkpoint1, result, "Il doit y avoir une étape intermediaire");
    }
    

}