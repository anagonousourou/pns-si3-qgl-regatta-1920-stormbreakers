package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.StreamManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;

public class GraphTest {
    private Sommet s1;
    private Sommet s2;
    private Sommet s3;
    private Sommet s4;
    private Sommet s5;
    private Sommet s6;
    private Sommet s7;
    private Sommet s8;
    private Sommet s9;
    private Sommet s10;
    private Sommet s11;
    private Sommet s12;
    private Sommet s13;
    private Graph graph;
    private Recif reef1 = new Recif(new Position(300, 300), new Rectangle(400, 400, 0.0));
    private StreamManager streamManager;
    private WeatherAnalyst weatherAnalyst;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void getLowestDistanceNodeTest() {
        s1 = mock(Sommet.class);
        s2 = mock(Sommet.class);
        s3 = mock(Sommet.class);
        s4 = mock(Sommet.class);
        graph = new Graph(null, null);
        when(s1.getDistance()).thenReturn(200);
        when(s2.getDistance()).thenReturn(20);
        when(s3.getDistance()).thenReturn(2000);
        when(s4.getDistance()).thenReturn(100);

        assertEquals(s2, graph.getLowestDistanceNode(Set.of(s1, s2, s3, s4)), "On doit trouver s2");
        assertEquals(null, graph.getLowestDistanceNode(Set.of()), "On doit trouver null");

    }

    @Test
    public void createSquaringTest() {
        streamManager = new StreamManager(null, null);
        streamManager.setBoatsAndReefs(List.of(reef1));
        graph = new Graph(streamManager, null);

        graph.createSquaring(0.0, 0.0, 600, 600, 200);

        assertEquals(12, graph.nbOfNodes(), "Doit y avoir 12");

    }

    @Test
    public void createLinkBetweenVerticesTest() {
        s1 = new Sommet(0, 0);
        s2 = new Sommet(200, 0);
        s3 = new Sommet(400, 0);
        s4 = new Sommet(600, 0);
        s5 = new Sommet(0, 200);
        s6 = new Sommet(600, 200);
        s7 = new Sommet(0, 400);
        s8 = new Sommet(600, 400);
        s9 = new Sommet(0, 600);
        s10 = new Sommet(200, 600);
        s11 = new Sommet(400, 600);
        s12 = new Sommet(600, 600);
        s13 = new Sommet(200, 200);

        streamManager = mock(StreamManager.class);
        when(streamManager.speedProvided(any(), any())).thenReturn(0.0);
        weatherAnalyst = mock(WeatherAnalyst.class);
        when(weatherAnalyst.speedProvided(any(), any())).thenReturn(0.0);

        graph = new Graph(streamManager, weatherAnalyst);
        graph.setNodes(Set.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13));
        graph.createLinkBetweenVertices(200);
        assertEquals(3, s1.getAdjacentNodes().size());
        assertEquals(2, s12.getAdjacentNodes().size());
        assertEquals(5, s13.getAdjacentNodes().size());
    }

    @Test
    public void calculateShortestPathFromSourceTest() {
        s1 = new Sommet(0, 0);
        s2 = new Sommet(200, 0);
        s3 = new Sommet(400, 0);
        s4 = new Sommet(600, 0);
        s5 = new Sommet(0, 200);
        s6 = new Sommet(600, 200);
        s7 = new Sommet(0, 400);
        s8 = new Sommet(600, 400);
        s9 = new Sommet(0, 600);
        s10 = new Sommet(200, 600);
        s11 = new Sommet(400, 600);
        s12 = new Sommet(600, 600);

        streamManager = mock(StreamManager.class);
        when(streamManager.speedProvided(any(), any())).thenReturn(0.0);
        weatherAnalyst = mock(WeatherAnalyst.class);
        when(weatherAnalyst.speedProvided(any(), any())).thenReturn(0.0);

        graph = new Graph(streamManager, weatherAnalyst);

        graph.setNodes(Set.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12));
        // graph.createLinkBetweenVertices(200);

        graph.calculateShortestPathFromSource(s12, s1, 200);

        assertEquals(5, s1.getShortestPath().size());

        // assertEquals(2, s10.getShortestPath().size());
    }

    @Test
    public void reducePathTest() {

        s1 = new Sommet(0, 0);
        s2 = new Sommet(200, 0);
        s3 = new Sommet(400, 0);
        s4 = new Sommet(600, 0);
        s5 = new Sommet(0, 200);
        s6 = new Sommet(600, 200);
        s7 = new Sommet(0, 400);
        s8 = new Sommet(600, 400);
        s9 = new Sommet(0, 600);
        s10 = new Sommet(200, 600);
        s11 = new Sommet(400, 600);
        s12 = new Sommet(600, 600);
        Sommet sommet = mock(Sommet.class);
        streamManager = mock(StreamManager.class);
        when(sommet.getShortestPath()).thenReturn(List.of(s1, s5, s7, s9, s10, s11));

        graph = new Graph(streamManager, null);
        var result = graph.reducePath(sommet);

        assertEquals(2, result.size(), "Chemin reduit à deux");

        assertTrue(result.containsAll(List.of(s1, sommet)));

    }

    @Test
    public void hasAsVertexTest() {
        s1 = new Sommet(0, 0);
        s2 = new Sommet(200, 0);
        s3 = new Sommet(400, 0);
        s4 = new Sommet(600, 0);
        s5 = new Sommet(0, 200);
        s6 = new Sommet(600, 200);
        s7 = new Sommet(0, 400);
        s8 = new Sommet(600, 400);
        s9 = new Sommet(0, 600);
        s10 = new Sommet(200, 600);
        s11 = new Sommet(400, 600);
        s12 = new Sommet(600, 600);

        graph = new Graph(null, null);
        graph.setNodes(Set.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12));

        assertTrue(graph.hasAsVertex(new Point2D(0, 0)));
        assertFalse(graph.hasAsVertex(new Point2D(100, 0)));
        assertFalse(graph.hasAsVertex(new Point2D(199, 599)));
    }

    @Test
    public void findVertexForTest() {
        s1 = new Sommet(0, 0);
        s2 = new Sommet(200, 0);
        s3 = new Sommet(400, 0);
        s4 = new Sommet(600, 0);
        s5 = new Sommet(0, 200);
        s6 = new Sommet(600, 200);
        s7 = new Sommet(0, 400);
        s8 = new Sommet(600, 400);
        s9 = new Sommet(0, 600);
        s10 = new Sommet(200, 600);
        s11 = new Sommet(400, 600);
        s12 = new Sommet(600, 600);

        graph = new Graph(null, null);
        graph.setNodes(Set.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12));

        assertEquals(s12, graph.findVertexFor(new Point2D(600, 600)));
        assertEquals(null, graph.findVertexFor(new Point2D(600, 500)));
        assertEquals(null, graph.findVertexFor(new Point2D(599.9999, 599.9999)));

    }

    @Test
    public void addNodeAndLinkTest() {
        // we setUp a graph and we link its vertices
        s1 = new Sommet(0, 0);
        s2 = new Sommet(200, 0);
        s3 = new Sommet(400, 0);
        s4 = new Sommet(600, 0);
        s5 = new Sommet(0, 200);
        s6 = new Sommet(600, 200);
        s7 = new Sommet(0, 400);
        s8 = new Sommet(600, 400);
        s9 = new Sommet(0, 600);
        s10 = new Sommet(200, 600);
        s11 = new Sommet(400, 600);
        s12 = new Sommet(600, 600);
        s13 = new Sommet(200, 200);

        streamManager = mock(StreamManager.class);
        when(streamManager.speedProvided(any(), any())).thenReturn(0.0);
        weatherAnalyst = mock(WeatherAnalyst.class);
        when(weatherAnalyst.speedProvided(any(), any())).thenReturn(0.0);

        graph = new Graph(streamManager, weatherAnalyst);
        graph.setNodes(Set.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13));
        graph.createLinkBetweenVertices(200);

        // we add a new Vertex
        var vertex1 = new Sommet(300, 300);
        graph.addNodeAndLink(vertex1, 200);

        assertEquals(1, vertex1.getAdjacentNodes().size(), "Un seul voisin");

        assertEquals(6, s13.getAdjacentNodes().size(), "Un  voisin s'ajoute");

        // we add a new Vertex again
        var vertex2 = new Sommet(900, 800);

        graph.addNodeAndLink(vertex1, 200);

        assertEquals(0, vertex2.getAdjacentNodes().size(), "Pas de voisin");

        assertEquals(2, s12.getAdjacentNodes().size());
    }

    @Test
    public void clearShortestPathsTest() {
        s1 = new Sommet(0, 0);
        s2 = new Sommet(200, 0);
        s3 = new Sommet(400, 0);
        s4 = new Sommet(600, 0);
        s5 = new Sommet(0, 200);
        s6 = new Sommet(600, 200);
        s7 = new Sommet(0, 400);
        s8 = new Sommet(600, 400);
        s9 = new Sommet(0, 600);
        s10 = new Sommet(200, 600);
        s11 = new Sommet(400, 600);
        s12 = new Sommet(600, 600);

        streamManager = mock(StreamManager.class);
        when(streamManager.speedProvided(any(), any())).thenReturn(0.0);
        weatherAnalyst = mock(WeatherAnalyst.class);
        when(weatherAnalyst.speedProvided(any(), any())).thenReturn(0.0);

        graph = new Graph(streamManager, weatherAnalyst);

        graph.setNodes(Set.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12));
        // graph.createLinkBetweenVertices(200);

        graph.calculateShortestPathFromSource(s12, s1, 200);

        assertEquals(5, s1.getShortestPath().size());

        // assertEquals(2, s10.getShortestPath().size());

        graph.clearShortestPaths();

        assertTrue(s1.getShortestPath().isEmpty());

        assertTrue(s10.getShortestPath().isEmpty());
    }

}