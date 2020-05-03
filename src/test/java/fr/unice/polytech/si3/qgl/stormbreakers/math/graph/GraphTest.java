package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Reef;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.StreamManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;

public class GraphTest {
    private Vertex v1;
    private Vertex v2;
    private Vertex v3;
    private Vertex v4;
    private Vertex v5;
    private Vertex v6;
    private Vertex v7;
    private Vertex v8;
    private Vertex v9;
    private Vertex v10;
    private Vertex v11;
    private Vertex v12;
    private Vertex v13;
    private Graph graph;
    private Reef reef1 = new Reef(new Position(300, 300), new Rectangle(400, 400, 0.0));
    private StreamManager streamManager;
    private WeatherAnalyst weatherAnalyst;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void getLowestDistanceNodeTest() {
        v1 = new Vertex(0,0);
        v1= spy(v1);
        v2 = new Vertex(300,300);
        v2= spy(v2);
        v3 = new Vertex(500,500);
        v3= spy(v3);
        v4 = new Vertex(700,700);
        v4= spy(v4);
        graph = new Graph(null, null);
        
        Vertex destination= new Vertex(600,600);
        when(v1.getDistance()).thenReturn(200);
        when(v2.getDistance()).thenReturn(20);
        when(v3.getDistance()).thenReturn(2000);
        when(v4.getDistance()).thenReturn(100);


        assertEquals(v4, graph.getLowestDistanceNode(Set.of(v1, v2, v3, v4),destination), "On doit trouver s4");
        assertEquals(null, graph.getLowestDistanceNode(Set.of(),destination), "On doit trouver null");

    }

    @Test
    public void createSquaringTest() {
        Boat boat=mock(Boat.class);
        when(boat.securityMargin()).thenReturn(6.0);
        streamManager = new StreamManager(null, boat);
        streamManager.setBoatsAndReefs(List.of(reef1));
        graph = new Graph(streamManager, null);

        graph.createSquaring(0.0, 0.0, 600, 600, 200);

        assertEquals(12, graph.nbOfNodes(), "Doit y avoir 12");

    }

    @Test
    public void createLinkBetweenVerticesTest() {
        v1 = new Vertex(0, 0);
        v2 = new Vertex(200, 0);
        v3 = new Vertex(400, 0);
        v4 = new Vertex(600, 0);
        v5 = new Vertex(0, 200);
        v6 = new Vertex(600, 200);
        v7 = new Vertex(0, 400);
        v8 = new Vertex(600, 400);
        v9 = new Vertex(0, 600);
        v10 = new Vertex(200, 600);
        v11 = new Vertex(400, 600);
        v12 = new Vertex(600, 600);
        v13 = new Vertex(200, 200);

        streamManager = mock(StreamManager.class);
        when(streamManager.speedProvided(any(), any())).thenReturn(0.0);
        weatherAnalyst = mock(WeatherAnalyst.class);
        when(weatherAnalyst.speedProvided(any(), any())).thenReturn(0.0);

        graph = new Graph(streamManager, weatherAnalyst);
        graph.setNodes(Set.of(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13));
        graph.createLinkBetweenVertices(200);
        assertEquals(3, v1.getAdjacentNodes().size());
        assertEquals(2, v12.getAdjacentNodes().size());
        assertEquals(5, v13.getAdjacentNodes().size());
    }

    @Test
    public void calculateShortestPathFromSourceTest() {
        v1 = new Vertex(0, 0);
        v2 = new Vertex(200, 0);
        v3 = new Vertex(400, 0);
        v4 = new Vertex(600, 0);
        v5 = new Vertex(0, 200);
        v6 = new Vertex(600, 200);
        v7 = new Vertex(0, 400);
        v8 = new Vertex(600, 400);
        v9 = new Vertex(0, 600);
        v10 = new Vertex(200, 600);
        v11 = new Vertex(400, 600);
        v12 = new Vertex(600, 600);

        streamManager = mock(StreamManager.class);
        when(streamManager.speedProvided(any(), any())).thenReturn(0.0);
        weatherAnalyst = mock(WeatherAnalyst.class);
        when(weatherAnalyst.speedProvided(any(), any())).thenReturn(0.0);

        graph = new Graph(streamManager, weatherAnalyst);

        graph.setNodes(Set.of(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12));
        // graph.createLinkBetweenVertices(200);

        graph.calculateShortestPathFromSource(v12, v1, 200);

        assertEquals(5, v1.getShortestPath().size());

        // assertEquals(2, s10.getShortestPath().size());
    }

    @Test
    public void reducePathTest() {

        v1 = new Vertex(0, 0);
        v2 = new Vertex(200, 0);
        v3 = new Vertex(400, 0);
        v4 = new Vertex(600, 0);
        v5 = new Vertex(0, 200);
        v6 = new Vertex(600, 200);
        v7 = new Vertex(0, 400);
        v8 = new Vertex(600, 400);
        v9 = new Vertex(0, 600);
        v10 = new Vertex(200, 600);
        v11 = new Vertex(400, 600);
        v12 = new Vertex(600, 600);
        Vertex sommet = mock(Vertex.class);
        streamManager = mock(StreamManager.class);
        when(sommet.getShortestPath()).thenReturn(List.of(v1, v5, v7, v9, v10, v11));

        graph = new Graph(streamManager, null);
        var result = graph.reducePath(sommet);

        assertEquals(2, result.size(), "Chemin reduit à deux");

        assertTrue(result.containsAll(List.of(v1, sommet)));

    }

    @Test
    public void hasAsVertexTest() {
        v1 = new Vertex(0, 0);
        v2 = new Vertex(200, 0);
        v3 = new Vertex(400, 0);
        v4 = new Vertex(600, 0);
        v5 = new Vertex(0, 200);
        v6 = new Vertex(600, 200);
        v7 = new Vertex(0, 400);
        v8 = new Vertex(600, 400);
        v9 = new Vertex(0, 600);
        v10 = new Vertex(200, 600);
        v11 = new Vertex(400, 600);
        v12 = new Vertex(600, 600);

        graph = new Graph(null, null);
        graph.setNodes(Set.of(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12));

        assertTrue(graph.hasAsVertex(new Point2D(0, 0)));
        assertFalse(graph.hasAsVertex(new Point2D(100, 0)));
        assertFalse(graph.hasAsVertex(new Point2D(199, 599)));
    }

    @Test
    public void findVertexForTest() {
        v1 = new Vertex(0, 0);
        v2 = new Vertex(200, 0);
        v3 = new Vertex(400, 0);
        v4 = new Vertex(600, 0);
        v5 = new Vertex(0, 200);
        v6 = new Vertex(600, 200);
        v7 = new Vertex(0, 400);
        v8 = new Vertex(600, 400);
        v9 = new Vertex(0, 600);
        v10 = new Vertex(200, 600);
        v11 = new Vertex(400, 600);
        v12 = new Vertex(600, 600);

        graph = new Graph(null, null);
        graph.setNodes(Set.of(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12));

        assertEquals(v12, graph.findVertexFor(new Point2D(600, 600)));
        assertEquals(null, graph.findVertexFor(new Point2D(600, 500)));
        assertEquals(null, graph.findVertexFor(new Point2D(599.9999, 599.9999)));

    }

    @Test
    public void addNodeAndLinkTest() {
        // we setUp a graph and we link its vertices
        v1 = new Vertex(0, 0);
        v2 = new Vertex(200, 0);
        v3 = new Vertex(400, 0);
        v4 = new Vertex(600, 0);
        v5 = new Vertex(0, 200);
        v6 = new Vertex(600, 200);
        v7 = new Vertex(0, 400);
        v8 = new Vertex(600, 400);
        v9 = new Vertex(0, 600);
        v10 = new Vertex(200, 600);
        v11 = new Vertex(400, 600);
        v12 = new Vertex(600, 600);
        v13 = new Vertex(200, 200);

        streamManager = mock(StreamManager.class);
        when(streamManager.speedProvided(any(), any())).thenReturn(0.0);
        weatherAnalyst = mock(WeatherAnalyst.class);
        when(weatherAnalyst.speedProvided(any(), any())).thenReturn(0.0);

        graph = new Graph(streamManager, weatherAnalyst);
        graph.setNodes(Set.of(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13));
        graph.createLinkBetweenVertices(200);

        // we add a new Vertex
        var vertex1 = new Vertex(300, 300);
        graph.addNodeAndLink(vertex1, 200);

        assertEquals(1, vertex1.getAdjacentNodes().size(), "Un seul voisin");

        assertEquals(6, v13.getAdjacentNodes().size(), "Un  voisin s'ajoute");

        // we add a new Vertex again
        var vertex2 = new Vertex(900, 800);

        graph.addNodeAndLink(vertex1, 200);

        assertEquals(0, vertex2.getAdjacentNodes().size(), "Pas de voisin");

        assertEquals(2, v12.getAdjacentNodes().size());
    }

    @Test
    public void clearShortestPathsTest() {
        v1 = new Vertex(0, 0);
        v2 = new Vertex(200, 0);
        v3 = new Vertex(400, 0);
        v4 = new Vertex(600, 0);
        v5 = new Vertex(0, 200);
        v6 = new Vertex(600, 200);
        v7 = new Vertex(0, 400);
        v8 = new Vertex(600, 400);
        v9 = new Vertex(0, 600);
        v10 = new Vertex(200, 600);
        v11 = new Vertex(400, 600);
        v12 = new Vertex(600, 600);

        streamManager = mock(StreamManager.class);
        when(streamManager.speedProvided(any(), any())).thenReturn(0.0);
        weatherAnalyst = mock(WeatherAnalyst.class);
        when(weatherAnalyst.speedProvided(any(), any())).thenReturn(0.0);

        graph = new Graph(streamManager, weatherAnalyst);

        graph.setNodes(Set.of(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12));
        // graph.createLinkBetweenVertices(200);

        graph.calculateShortestPathFromSource(v12, v1, 200);

        assertEquals(5, v1.getShortestPath().size());

        // assertEquals(2, s10.getShortestPath().size());

        graph.clearShortestPaths();

        assertTrue(v1.getShortestPath().isEmpty());

        assertTrue(v10.getShortestPath().isEmpty());
    }

    @Test
    public void computeAdjacentNodesTest(){
        Reef reef=new Reef(new Position(500,300),
        new Rectangle(400,400,0.0)
        );
        Boat boat=mock(Boat.class);
        when(boat.securityMargin()).thenReturn(6.0);
        StreamManager streamManager=new StreamManager(null, boat);
        streamManager.setBoatsAndReefs(List.of(
            reef
        ));
        WeatherAnalyst weatherAnalyst=mock(WeatherAnalyst.class);
        Graph graph=new Graph(streamManager, weatherAnalyst);

        Vertex s1=new Vertex(500, 503);

        Vertex s2=new Vertex(500,550);
        Vertex s3=new Vertex(500,600);
        Vertex s4=new Vertex(400,550);
        Vertex s5=new Vertex(600,525);

        assertTrue(streamManager.pointIsInsideOrAroundReefOrBoat(s1.getPoint()));

        graph.addNode(s1);
        graph.addNode(s2);
        graph.addNode(s3);
        graph.addNode(s4);
        graph.addNode(s5);

        assertTrue(s1.getAdjacentNodes().isEmpty());
        graph.computeAdjacentNodes(s1, 50);
        assertFalse(s1.getAdjacentNodes().isEmpty());
    }


}