package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.RectangularSurface;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CheckpointsManager;

public class Cartographer {

    private Boat boat;
    private Graph graph;
    private CheckpointsManager checkpointsManager;
    private RectangularSurface virtualMap;
    private static final double MAP_MARGIN = 1500;
    private static final double ECART = 50.0;

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
        long t = System.currentTimeMillis();
        double ecart = ECART;
        double height = Math.abs((boat.x() - cp.x())) + MAP_MARGIN;
        double width = Math.abs((boat.y() - cp.y())) + MAP_MARGIN;

        if (width >= 3000 || height >= 3000) {
            ecart = 150;
        } else if (width >= 2000 || height >= 2000) {
            ecart = 120;
        } else if (width >= 1500 || height >= 1500) {
            ecart = 100;
        } else if (width >= 1200 || height >= 1200) {
            ecart = 60;
        }

        System.out.println("Ecart choisi: "+ecart);

        IPoint center = IPoint.centerPoints(boat, cp);

        this.virtualMap = new RectangularSurface(center.x(), center.y(), 0.0, new Rectangle(width, height, 0.0));

        graph.createSquaring(virtualMap.minX(), virtualMap.minY(), virtualMap.maxX(), virtualMap.maxY(), ecart);

        System.out.println("createSquaring: " + (System.currentTimeMillis() - t));

        var depart = new Sommet(boat);
        depart = graph.addNode(depart);

        var destination = new Sommet(cp);
        destination = graph.addNode(destination);

        // en principe inutile
        graph.clearShortestPaths();

        graph.calculateShortestPathFromSource(depart, destination, ecart);

        List<Sommet> path = graph.reducePath(destination);

        System.out.println("Djisktra: " + (System.currentTimeMillis() - t));
        System.out.println("Path computed: "+path);
        if (path.size() > 1) {
            return path.get(1).getPoint();
        } else {
            Logger.getInstance().log("Oh no path found :screwed");
            return cp;
        }
    }

    

    boolean virtualMapExists() {
        return this.virtualMap != null;
    }

}