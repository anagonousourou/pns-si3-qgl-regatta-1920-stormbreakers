package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.StreamManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;

public class Graph {
    private Set<Sommet> nodes = new HashSet<>();
    private StreamManager streamManager;
    private WeatherAnalyst weatherAnalyst;

    public Graph(StreamManager streamManager, WeatherAnalyst weatherAnalyst) {
        this.weatherAnalyst = weatherAnalyst;
        this.streamManager = streamManager;
    }

    public Sommet addNode(Sommet nodeA) {
        if (nodes.add(nodeA)) {
            return nodeA;
        } else {

            return this.findVertexFor(nodeA.getPoint());
        }

    }

    private static void calculateMinimumDistance(Sommet evaluationNode, int edgeWeigh, Sommet sourceNode) {
        int sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Sommet> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    public void createSquaring(double xmin, double ymin, double xmax, double ymax, double ecart) {
        this.nodes.clear();
        for (double x = xmin; x <= xmax; x = x + ecart) {
            for (double y = ymin; y <= ymax; y = y + ecart) {
                if (!streamManager.pointIsInsideRecif(new Point2D(x, y))) {
                    this.addNode(new Sommet(x, y));
                }

            }

        }

    }

    // NEW
    public List<Sommet> reducePath(Sommet sommet) {
        // ?? morceau de chemin trop court pour etre atteint
        List<Sommet> result = new ArrayList<>(sommet.getShortestPath());
        result.add(sommet);
        int i = 1;
        while (i < result.size() - 1) {
            if (!this.streamManager.thereIsObstacleBetween(result.get(i - 1).getPoint(),
                    result.get(i + 1).getPoint())) {

                result.remove(i);
                i--;
            }
            i++;
        }
        System.out.println(result);
        Logger.getInstance().log(result.toString());
        return result;
    }

    public void createLinkBetweenVertices(double ecart) {

        for (Sommet node : nodes) {

            nodes.stream().filter(n -> !n.equals(node)).forEach(n -> {
                double distance = n.getPoint().distanceTo(node.getPoint());
                
                if (distance <= ecart * Math.sqrt(2) && !this.streamManager.thereIsRecifsBetween(n.getPoint(), node.getPoint())) {
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

    }

    // NEW
    public void calculateShortestPathFromSource(Sommet depart, Sommet destination) {
        Sommet currentNode = null;
        depart.setDistance(0);

        Set<Sommet> settledNodes = new HashSet<>();
        Set<Sommet> unsettledNodes = new HashSet<>();

        unsettledNodes.add(depart);

        while (!unsettledNodes.isEmpty() && !settledNodes.contains(destination)) {

            currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Entry<Sommet, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Sommet adjacentNode = adjacencyPair.getKey();
                int edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }

    }

    // NEW
    public Sommet getLowestDistanceNode(Set<Sommet> unsettledNodes) {
        var optResult = unsettledNodes.stream().min((a, b) -> Double.compare(a.getDistance(), b.getDistance()));
        if (optResult.isPresent()) {
            return optResult.get();
        }
        Logger.getInstance().log(unsettledNodes.toString());
        Logger.getInstance().log("returning null :) fichu");
        return null;
    }

    public int nbOfNodes() {
        return this.nodes.size();
    }

    @Override
    public String toString() {
        return String.format("%s(nodes:%s)", this.getClass().getSimpleName(), this.nodes.toString());
    }

    public void setNodes(Set<Sommet> nodes) {
        this.nodes = new HashSet<>(nodes);
    }

    public boolean hasAsVertex(IPoint cp) {
        return nodes.stream().anyMatch(sommet -> Utils.almostEquals(sommet.getPoint(), cp));
    }

    // NEW
    public Sommet addNodeAndLink(Sommet vertex, double ecart) {
        final Sommet ourSommet = this.addNode(vertex);
        nodes.stream().filter(node -> node != ourSommet).forEach(n -> {
            double distance = n.getPoint().distanceTo(ourSommet.getPoint());
            if (distance <= ecart * Math.sqrt(2)) {
                double cout = distance - streamManager.speedProvided(ourSommet.getPoint(), n.getPoint())
                        - this.weatherAnalyst.speedProvided(ourSommet.getPoint(), n.getPoint());
                double coutNodeSommet = distance - streamManager.speedProvided(n.getPoint(), ourSommet.getPoint())
                        - this.weatherAnalyst.speedProvided(n.getPoint(), ourSommet.getPoint());
                if (cout < 0) {
                    ourSommet.addDestination(n, 0);

                } else {

                    ourSommet.addDestination(n, (int) cout);

                }

                if (coutNodeSommet < 0) {
                    n.addDestination(ourSommet, 0);
                } else {

                    n.addDestination(ourSommet, (int) coutNodeSommet);
                }

            }
        });

        return ourSommet;
    }

    public void clearShortestPaths() {
        for (Sommet sommet : nodes) {
            sommet.clearShortestPath();
        }
    }

    public Sommet findVertexFor(IPoint cp) {

        var optResult = nodes.stream().filter(sommet -> Utils.almostEquals(sommet.getPoint(), cp)).findAny();
        if (optResult.isPresent()) {
            return optResult.get();
        }
        return null;
    }
}