package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Graph {
    
    private Map<Vertex, List<Vertex>> adjVertices;
         
    public void addVertex(IPoint point) {
        adjVertices.putIfAbsent(new Vertex(point), new ArrayList<>());
    }

    public void removeVertex(IPoint point) {
        Vertex v = new Vertex(point);
        adjVertices.values().stream().forEach(e -> e.remove(v));
        adjVertices.remove(new Vertex(point));
    }

    public void addEdge(IPoint label1, IPoint label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        adjVertices.get(v1).add(v2);
        adjVertices.get(v2).add(v1);
    }

    public void removeEdge(IPoint label1, IPoint label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        List<Vertex> eV1 = adjVertices.get(v1);
        List<Vertex> eV2 = adjVertices.get(v2);
        if (eV1 != null)
            eV1.remove(v2);
        if (eV2 != null)
            eV2.remove(v1);
    }
    
}