package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;

public class Sommet {
	private IPoint point;
	private List<Sommet> shortestPath = new LinkedList<>();
	private int distance = Integer.MAX_VALUE;
	private Map<Sommet, Integer> adjacentNodes = new HashMap<>();

	public Sommet(IPoint pt) {
		point = new Point2D(pt);
	}

	public Sommet(double x, double y) {
		point = new Point2D(x, y);
	}

	public IPoint getPoint() {
		return point;
	}

	public String toString() {
		return String.format("%s(x: %f,y: %f,distance: %d)", this.getClass().getSimpleName(),this.point.x(),this.point.y(),distance);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Sommet)) {
			return false;
		}

		Sommet s = (Sommet) other;

		return Utils.almostEquals(s.getPoint(), this.getPoint());

	}

	public void addDestination(Sommet destination, int distance) {
		adjacentNodes.put(destination, distance);
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

	public List<Sommet> getShortestPath() {
		return this.shortestPath;
	}

	public void setShortestPath(List<Sommet> shortestPath2) {
		this.shortestPath = shortestPath2;
	}

	public Map<Sommet, Integer> getAdjacentNodes() {
		return this.adjacentNodes;
	}

	public void clearShortestPath() {
		this.shortestPath.clear();
	}
}
