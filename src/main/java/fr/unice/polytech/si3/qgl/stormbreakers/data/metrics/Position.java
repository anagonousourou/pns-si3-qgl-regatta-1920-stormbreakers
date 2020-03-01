package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

public class Position implements Logable,IPoint {
    private double x;
    private double y;
    private double orientation;

    @JsonCreator
    public Position(@JsonProperty("x") double x, @JsonProperty("y") double y,
            @JsonProperty("orientation") double orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public Position(double x, double y) {
        this(x, y, 0);
    }

    @JsonProperty("x")
    public double x() {
        return x;
    }

    @JsonProperty("y")
    public double y() {
        return y;
    }

    

    public double distanceTo(IPoint other){
        return Math.sqrt((other.x() - this.x) * (other.x() - this.x) + (other.y() - this.y) * (other.y() - this.y));
    }
    public double distanceTo(Position pos) {
        return Math.sqrt((pos.x - this.x) * (pos.x - this.x) + (pos.y - this.y) * (pos.y - this.y));
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    public double getOrientation() {
        return this.orientation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Position))
            return false;
        Position other = (Position) obj;
        return other.x == x && other.y == y && other.orientation == orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, orientation);
    }

    @Override
    public String toString() {
        return "Position ( " + this.x + " , " + this.y + " , " + this.orientation + " )";
    }

    @Override
    public String toLogs() {
        return "P(" + this.x + "|" + this.y + "|" + this.orientation + ")";
    }

	public Point2D getPoint2D() {
		return new Point2D(this.x, this.y);
	}
}
