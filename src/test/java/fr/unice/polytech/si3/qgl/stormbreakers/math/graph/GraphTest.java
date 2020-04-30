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
    private Vertex s1;
    private Vertex s2;
    private Vertex s3;
    private Vertex s4;
    private Vertex s5;
    private Vertex s6;
    private Vertex s7;
    private Vertex s8;
    private Vertex s9;
    private Vertex s10;
    private Vertex s11;
    private Vertex s12;
    private Vertex s13;
    private Graph graph;
    private Reef reef1 = new Reef(new Position(300, 300), new Rectangle(400, 400, 0.0));
    private StreamManager streamManager;
    private WeatherAnalyst weatherAnalyst;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void getLowestDistanceNodeTest() {
        s1 = new Vertex(0,0);
        s1= spy(s1);
        s2 = new Vertex(300,300);
        s2= spy(s2);
        s3 = new Vertex(500,500);
        s3= spy(s3);
        s4 = new Vertex(700,700);
        s4= spy(s4);
        graph = new Graph(null, null);
        
        Vertex destination= new Vertex(600,600);
        when(s1.getDistance()).thenReturn(200);
        when(s2.getDistance()).thenReturn(20);
        when(s3.getDistance()).thenReturn(2000);
        when(s4.getDistance()).thenReturn(100);


        assertEquals(s4, graph.getLowestDistanceNode(Set.of(s1, s2, s3, s4),destination), "On doit trouver s4");
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
        s1 = new Vertex(0, 0);
        s2 = new Vertex(200, 0);
        s3 = new Vertex(400, 0);
        s4 = new Vertex(600, 0);
        s5 = new Vertex(0, 200);
        s6 = new Vertex(600, 200);
        s7 = new Vertex(0, 400);
        s8 = new Vertex(600, 400);
        s9 = new Vertex(0, 600);
        s10 = new Vertex(200, 600);
        s11 = new Vertex(400, 600);
        s12 = new Vertex(600, 600);
        s13 = new Vertex(200, 200);

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
        s1 = new Vertex(0, 0);
        s2 = new Vertex(200, 0);
        s3 = new Vertex(400, 0);
        s4 = new Vertex(600, 0);
        s5 = new Vertex(0, 200);
        s6 = new Vertex(600, 200);
        s7 = new Vertex(0, 400);
        s8 = new Vertex(600, 400);
        s9 = new Vertex(0, 600);
        s10 = new Vertex(200, 600);
        s11 = new Vertex(400, 600);
        s12 = new Vertex(600, 600);

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

        s1 = new Vertex(0, 0);
        s2 = new Vertex(200, 0);
        s3 = new Vertex(400, 0);
        s4 = new Vertex(600, 0);
        s5 = new Vertex(0, 200);
        s6 = new Vertex(600, 200);
        s7 = new Vertex(0, 400);
        s8 = new Vertex(600, 400);
        s9 = new Vertex(0, 600);
        s10 = new Vertex(200, 600);
        s11 = new Vertex(400, 600);
        s12 = new Vertex(600, 600);
        Vertex sommet = mock(Vertex.class);
        streamManager = mock(StreamManager.class);
        when(sommet.getShortestPath()).thenReturn(List.of(s1, s5, s7, s9, s10, s11));

        graph = new Graph(streamManager, null);
        var result = graph.reducePath(sommet);

        assertEquals(2, result.size(), "Chemin reduit à deux");

        assertTrue(result.containsAll(List.of(s1, sommet)));

    }

    @Test
    public void hasAsVertexTest() {
        s1 = new Vertex(0, 0);
        s2 = new Vertex(200, 0);
        s3 = new Vertex(400, 0);
        s4 = new Vertex(600, 0);
        s5 = new Vertex(0, 200);
        s6 = new Vertex(600, 200);
        s7 = new Vertex(0, 400);
        s8 = new Vertex(600, 400);
        s9 = new Vertex(0, 600);
        s10 = new Vertex(200, 600);
        s11 = new Vertex(400, 600);
        s12 = new Vertex(600, 600);

        graph = new Graph(null, null);
        graph.setNodes(Set.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12));

        assertTrue(graph.hasAsVertex(new Point2D(0, 0)));
        assertFalse(graph.hasAsVertex(new Point2D(100, 0)));
        assertFalse(graph.hasAsVertex(new Point2D(199, 599)));
    }

    @Test
    public void findVertexForTest() {
        s1 = new Vertex(0, 0);
        s2 = new Vertex(200, 0);
        s3 = new Vertex(400, 0);
        s4 = new Vertex(600, 0);
        s5 = new Vertex(0, 200);
        s6 = new Vertex(600, 200);
        s7 = new Vertex(0, 400);
        s8 = new Vertex(600, 400);
        s9 = new Vertex(0, 600);
        s10 = new Vertex(200, 600);
        s11 = new Vertex(400, 600);
        s12 = new Vertex(600, 600);

        graph = new Graph(null, null);
        graph.setNodes(Set.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12));

        assertEquals(s12, graph.findVertexFor(new Point2D(600, 600)));
        assertEquals(null, graph.findVertexFor(new Point2D(600, 500)));
        assertEquals(null, graph.findVertexFor(new Point2D(599.9999, 599.9999)));

    }

    @Test
    public void addNodeAndLinkTest() {
        // we setUp a graph and we link its vertices
        s1 = new Vertex(0, 0);
        s2 = new Vertex(200, 0);
        s3 = new Vertex(400, 0);
        s4 = new Vertex(600, 0);
        s5 = new Vertex(0, 200);
        s6 = new Vertex(600, 200);
        s7 = new Vertex(0, 400);
        s8 = new Vertex(600, 400);
        s9 = new Vertex(0, 600);
        s10 = new Vertex(200, 600);
        s11 = new Vertex(400, 600);
        s12 = new Vertex(600, 600);
        s13 = new Vertex(200, 200);

        streamManager = mock(StreamManager.class);
        when(streamManager.speedProvided(any(), any())).thenReturn(0.0);
        weatherAnalyst = mock(WeatherAnalyst.class);
        when(weatherAnalyst.speedProvided(any(), any())).thenReturn(0.0);

        graph = new Graph(streamManager, weatherAnalyst);
        graph.setNodes(Set.of(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13));
        graph.createLinkBetweenVertices(200);

        // we add a new Vertex
        var vertex1 = new Vertex(300, 300);
        graph.addNodeAndLink(vertex1, 200);

        assertEquals(1, vertex1.getAdjacentNodes().size(), "Un seul voisin");

        assertEquals(6, s13.getAdjacentNodes().size(), "Un  voisin s'ajoute");

        // we add a new Vertex again
        var vertex2 = new Vertex(900, 800);

        graph.addNodeAndLink(vertex1, 200);

        assertEquals(0, vertex2.getAdjacentNodes().size(), "Pas de voisin");

        assertEquals(2, s12.getAdjacentNodes().size());
    }

    @Test
    public void clearShortestPathsTest() {
        s1 = new Vertex(0, 0);
        s2 = new Vertex(200, 0);
        s3 = new Vertex(400, 0);
        s4 = new Vertex(600, 0);
        s5 = new Vertex(0, 200);
        s6 = new Vertex(600, 200);
        s7 = new Vertex(0, 400);
        s8 = new Vertex(600, 400);
        s9 = new Vertex(0, 600);
        s10 = new Vertex(200, 600);
        s11 = new Vertex(400, 600);
        s12 = new Vertex(600, 600);

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