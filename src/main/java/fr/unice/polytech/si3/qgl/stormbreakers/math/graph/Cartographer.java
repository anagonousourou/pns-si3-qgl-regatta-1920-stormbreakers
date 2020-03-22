package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.Collections;
import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.math.RectangularSurface;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CheckpointsManager;

public class Cartographer {

    private Boat boat;
    private Graph graph;
    private CheckpointsManager checkpointsManager;
    private RectangularSurface virtualMap;

    public Cartographer(CheckpointsManager checkpointsManager, Graph graph, Boat boat) {
        this.boat = boat;
        this.graph = graph;
        this.checkpointsManager = checkpointsManager;

    }

    public IPoint nextPoint() {
        Checkpoint cp = this.checkpointsManager.nextCheckpoint();

        return caseBuildMap(cp); // nouveau graph

    }

    IPoint caseBuildMap(Checkpoint cp) {
        double height = Math.abs((boat.x() - cp.x())) + 200;
        double width = Math.abs((boat.y() - cp.y())) + 200;

        IPoint center = IPoint.centerPoints(boat, cp);

        this.virtualMap = new RectangularSurface(center.x(), center.y(), 0.0, new Rectangle(width, height, 0.0));

        graph.createSquaring(virtualMap.minX(), virtualMap.minY(), virtualMap.maxX(), virtualMap.maxY(), 50);

        var depart = new Sommet(boat);
        depart = graph.addNode(depart);

        var destination = new Sommet(cp);
        destination = graph.addNode(destination);
        graph.createLinkBetweenVertices(50);

        // en principe inutile
        graph.clearShortestPaths();

        graph.calculateShortestPathFromSource(destination);
        
        List<Sommet> path = graph.reducePath(depart);
        Collections.reverse(path);
        if (path.size() > 1) {
            return path.get(1).getPoint();
        } else {

            return cp;
        }
    }

    /*
     * IPoint caseUseExistingMap(Checkpoint cp) { List<Sommet> path; Sommet depart;
     * Sommet destination; if (!graph.hasAsVertex(cp)) {// le checkpoint était pas
     * pris en compte dans le graph destination = graph.addNodeAndLink(new
     * Sommet(cp), 50); depart = graph.addNodeAndLink(new Sommet(boat), 50);
     * 
     * // reduce it // return the second point } else { // on rajoute la nouvelle
     * position du bateau au graphe depart = graph.addNodeAndLink(new Sommet(boat),
     * 50); destination = graph.findVertexFor(cp); } graph.clearShortestPaths();
     * 
     * graph.calculateShortestPathFromSource(destination);
     * System.out.println(depart.getDistance());
     * System.out.println(depart.getShortestPath()); path =
     * graph.reducePath(depart); Collections.reverse(path); if (path.size() > 1) {
     * return path.get(1).getPoint(); } else { return cp; }
     * 
     * }
     */

    boolean virtualMapExists() {
        return this.virtualMap != null;
    }

}