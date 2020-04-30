package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.RectangularSurface;
import fr.unice.polytech.si3.qgl.stormbreakers.math.graph.Graph;
import fr.unice.polytech.si3.qgl.stormbreakers.math.graph.Vertex;

public class Cartographer {

    private final Boat boat;
    private final Graph graph;
    private final CheckpointsManager checkpointsManager;
    private RectangularSurface virtualMap;
    private static final double MAP_MARGIN_Y = 2000;
    private static final double MAP_MARGIN_X = 6000;
    private static final double GAP = 50.0;

    public Cartographer(CheckpointsManager checkpointsManager, Graph graph, Boat boat) {
        this.boat = boat;
        this.graph = graph;
        this.checkpointsManager = checkpointsManager;

    }

    public IPoint nextPoint() {
        Checkpoint cp = this.checkpointsManager.nextCheckpoint();

        return caseBuildMap(cp); // new graph

    }

    IPoint caseBuildMap(Checkpoint cp) {
        long t = System.currentTimeMillis();
        double gap = GAP;
        double height = Math.abs((boat.x() - cp.x())) + MAP_MARGIN_X;
        double width = Math.abs((boat.y() - cp.y())) + MAP_MARGIN_Y;

        double d = boat.distanceTo(cp);
        System.out.println("distanceToCp: " + d);

        if(d >= 9000){
            gap=400;
        }
        else if (d >= 8000) {
            gap = 350;
        } else if (d >= 6000) {
            gap = 300;
        } else if (d >= 3000) {
            gap = 200;
        } else if (d >= 2000) {
            gap = 150;
        } else if (d > 1000) {
            gap = 100;
        }

        System.out.println("Ecart choisi: " + gap);

        IPoint center = IPoint.centerPoints(boat, cp);

        this.virtualMap = new RectangularSurface(center.x(), center.y(), 0.0, new Rectangle(width, height, 0.0));

        graph.createSquaring(virtualMap.minX(), virtualMap.minY(), virtualMap.maxX(), virtualMap.maxY(), gap);

        System.out.println("createSquaring: " + (System.currentTimeMillis() - t));

        var start = new Vertex(boat);
        start = graph.addNode(start);

        var destination = new Vertex(cp);
        destination = graph.addNode(destination);

        // en principe inutile
        graph.clearShortestPaths();

        graph.calculateShortestPathFromSource(start, destination, gap);

        List<Vertex> path = graph.reducePath(destination);

        System.out.println("Djisktra: " + (System.currentTimeMillis() - t));
        System.out.println("Path computed: " + path);
        if (path.size() > 1) {
            return path.get(1).getPoint();
        } else {
            Logger.getInstance().log("Oh no path found :bad");
            return cp;
        }
    }

    boolean virtualMapExists() {
        return this.virtualMap != null;
    }

}