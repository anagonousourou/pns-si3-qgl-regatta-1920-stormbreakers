package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.StreamManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;

public class Graph {
    private Set<Vertex> nodes = new HashSet<>();
    private StreamManager streamManager;
    private WeatherAnalyst weatherAnalyst;

    public Graph(StreamManager streamManager, WeatherAnalyst weatherAnalyst) {
        this.weatherAnalyst = weatherAnalyst;
        this.streamManager = streamManager;
    }

    public Vertex addNode(Vertex nodeA) {
        if (nodes.add(nodeA)) {
            return nodeA;
        } else {

            return this.findVertexFor(nodeA.getPoint());
        }

    }

    private static void calculateMinimumDistance(Vertex evaluationNode, int edgeWeigh, Vertex sourceNode) {
        int sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            List<Vertex> shortestPath = new ArrayList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    public void createSquaring(double xmin, double ymin, double xmax, double ymax, double ecart) {
        this.nodes.clear();

        for (double x = xmin; x <= xmax; x = x + ecart) {
            for (double y = ymin; y <= ymax; y = y + ecart) {
                if (!streamManager.pointIsInsideOrAroundReefOrBoat(new Point2D(x, y))) {
                    this.addNode(new Vertex(x, y));
                }

            }

        }
        Logger.getInstance().log("xdif :" + (xmax - xmin));
        Logger.getInstance().log("ydif :" + (ymax - ymin));
        Logger.getInstance().log("Nodes size:" + this.nodes.size());

    }

    // NEW
    public List<Vertex> reducePath(Vertex sommet) {
        // ?? morceau de chemin trop court pour etre atteint
        List<Vertex> result = new ArrayList<>(sommet.getShortestPath());
        result.add(sommet);
        return reducePath(result);
    }

    List<Vertex> reducePath(List<Vertex> result) {
        int i = 1;
        while (i < result.size() - 1) {
            if (!this.streamManager.thereIsObstacleBetween(result.get(i - 1).getPoint(),
                    result.get(i + 1).getPoint())) {
                result.remove(i);
                i--;
            }
            i++;
        }

        Logger.getInstance().log(result.toString());
        return result;
    }

    /**
     * 
     * @param ecart
     */
    public void createLinkBetweenVertices(double ecart) {
        long t = System.currentTimeMillis();
        for (Vertex node : nodes) {
            nodes.stream().filter(n -> !n.equals(node)).forEach(n -> {
                double distance = n.getPoint().distanceTo(node.getPoint());

                if (distance <= ecart * Math.sqrt(2)
                        && !this.streamManager.thereIsReefBetweenOrAround(n.getPoint(), node.getPoint())) {
                    double cout = distance - streamManager.speedProvided(node.getPoint(), n.getPoint())
                            - this.weatherAnalyst.speedProvided(node.getPoint(), n.getPoint());
                    if (cout < 0) {

                        node.addDestination(n, 0);
                    } else {

                        node.addDestination(n, (int) cout);
                    }

                }
            });

        }

        Logger.getInstance().log(System.currentTimeMillis() - t);

    }

    // NEW
    public void calculateShortestPathFromSource(Vertex depart, Vertex destination, double ecart) {
        Vertex currentNode = null;
        depart.setDistance(0);

        Set<Vertex> settledNodes = new HashSet<>(6000);
        Set<Vertex> unsettledNodes = new HashSet<>(6000);

        unsettledNodes.add(depart);

        while (!unsettledNodes.isEmpty() && !settledNodes.contains(destination)) {

            currentNode = getLowestDistanceNode(unsettledNodes, destination);
            unsettledNodes.remove(currentNode);

            if (!currentNode.computedAdj) {// si on n'a pas déja déterminé les noeuds adjacents de ce noeud
                this.computeAdjacentNodes(currentNode, ecart);
                currentNode.computedAdj = true;
            }

            for (Entry<Vertex, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Vertex adjacentNode = adjacencyPair.getKey();
                int edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);

                }
            }
            settledNodes.add(currentNode);
        }
    }

    List<Shape> determineShapesToConsider(Vertex vertex) {
        List<Shape> obstacles = new ArrayList<>(15);
        for (OceanEntity obstacle : this.streamManager.getBoatsAndReefs()) {
            if (obstacle.isInsideWrappingSurface(streamManager.boatSecurityMargin(), vertex.getPoint())) {

                obstacles.add(obstacle.getShape());
            } else {
                obstacles.add(obstacle.getShape().wrappingShape(streamManager.boatSecurityMargin()));
            }
        }

        return obstacles;

    }

    void computeAdjacentNodes(Vertex vertex, double ecart) {
        List<Shape> obstacles = determineShapesToConsider(vertex);
        for (Vertex v : this.nodes) {
            if (!v.equals(vertex)) {
                double distance = v.getPoint().distanceTo(vertex.getPoint());

                if (distance <= ecart * Math.sqrt(2) && obstacles.stream().noneMatch(
                        obstacle -> obstacle.collidesWith(new LineSegment2D(vertex.getPoint(), v.getPoint())))) {
                    double cout = distance - streamManager.speedProvided(vertex.getPoint(), v.getPoint())
                            - this.weatherAnalyst.speedProvided(vertex.getPoint(), v.getPoint());
                    if (cout < 0) {

                        vertex.addDestination(v, 0);
                    } else {

                        vertex.addDestination(v, (int) cout);
                    }

                }
            }
        }

    }

    // NEW
    public Vertex getLowestDistanceNode(Set<Vertex> unsettledNodes, Vertex destination) {
        var optResult = unsettledNodes.stream()
                .min((a, b) -> Double.compare(a.getDistance() + a.getPoint().distanceTo(destination.getPoint()),
                        b.getDistance() + b.getPoint().distanceTo(destination.getPoint())));

        if (optResult.isPresent()) {
            return optResult.get();
        }
        Logger.getInstance().log(unsettledNodes.toString());
        Logger.getInstance().log("returning null fichu :)");
        return null;
    }

    public int nbOfNodes() {
        return this.nodes.size();
    }

    @Override
    public String toString() {
        return String.format("%s(nodes:%s)", this.getClass().getSimpleName(), this.nodes.toString());
    }

    public void setNodes(Set<Vertex> nodes) {
        this.nodes = new HashSet<>(nodes);
    }

    public boolean hasAsVertex(IPoint cp) {
        return nodes.stream().anyMatch(sommet -> Utils.almostEquals(sommet.getPoint(), cp));
    }

    // NEW
    public Vertex addNodeAndLink(Vertex vertex, double gap) {
        final Vertex ourVertex = this.addNode(vertex);
        nodes.stream().filter(node -> node != ourVertex).forEach(n -> {
            double distance = n.getPoint().distanceTo(ourVertex.getPoint());
            if (distance <= gap * Math.sqrt(2)) {
                double cost = distance - streamManager.speedProvided(ourVertex.getPoint(), n.getPoint())
                        - this.weatherAnalyst.speedProvided(ourVertex.getPoint(), n.getPoint());
                double costNodeVertex = distance - streamManager.speedProvided(n.getPoint(), ourVertex.getPoint())
                        - this.weatherAnalyst.speedProvided(n.getPoint(), ourVertex.getPoint());
                if (cost < 0) {
                    ourVertex.addDestination(n, 0);

                } else {

                    ourVertex.addDestination(n, (int) cost);

                }

                if (costNodeVertex < 0) {
                    n.addDestination(ourVertex, 0);
                } else {

                    n.addDestination(ourVertex, (int) costNodeVertex);
                }

            }
        });

        return ourVertex;
    }

    public void clearShortestPaths() {
        for (Vertex vertex : nodes) {
        	vertex.clearShortestPath();
        }
    }

    public Vertex findVertexFor(IPoint cp) {

        var optResult = nodes.stream().filter(vertex -> Utils.almostEquals(vertex.getPoint(), cp)).findAny();
        if (optResult.isPresent()) {
            return optResult.get();
        }
        return null;
    }

}