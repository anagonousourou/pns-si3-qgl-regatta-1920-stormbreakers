package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;

public class Vertex {
	private IPoint point;
	private List<Vertex> shortestPath = new ArrayList<>(30);
	private int distance = Integer.MAX_VALUE;
	private Map<Vertex, Integer> adjacentNodes = new HashMap<>();
	boolean computedAdj= false;

	public Vertex(IPoint pt) {
		point = new Point2D(pt);
	}

	public Vertex(double x, double y) {
		point = new Point2D(x, y);
	}

	public IPoint getPoint() {
		return point;
	}

	public String toString() {
		return String.format("%s(x: %f,y: %f,distance: %d)", this.getClass().getSimpleName(), this.point.x(),
				this.point.y(), distance);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Vertex)) {
			return false;
		}

		Vertex s = (Vertex) other;

		return Utils.almostEquals(s.getPoint(), this.getPoint());

	}

	public void addDestination(Vertex destination, int distance) {
		adjacentNodes.putIfAbsent(destination, distance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(point);
	}

	public double getX() {
		return point.x();
	}

	public double getY() {
		return point.y();
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public List<Vertex> getShortestPath() {
		return this.shortestPath;
	}

	public void setShortestPath(List<Vertex> shortestPath2) {
		this.shortestPath = shortestPath2;
	}

	public Map<Vertex, Integer> getAdjacentNodes() {
		return this.adjacentNodes;
	}

	public void clearShortestPath() {
		this.shortestPath.clear();
	}
}
