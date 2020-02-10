package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Cartesian;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Polar;

import java.util.Objects;

public class Position implements Logable {
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
    public double getX() {
        return x;
    }

    @JsonProperty("y")
    public double getY() {
        return y;
    }

    public double thetaTo(Position other) {
        return Math.atan2(other.getY(), other.getX()) - Math.atan2(this.getY(), this.getX());
    }

    public Point2D getPoint2D() {
        return new Point2D(getX(),getY());
    }

    public double distanceTo(Position pos) {
        return Math.sqrt((pos.x - this.x) * (pos.x - this.x) + (pos.y - this.y) * (pos.y - this.y));
    }

    public double getOrientation() {
        return this.orientation;
    }

    public Position getRotatedBy(double theta) {
        Polar oldPolar = new Cartesian(x, y).toPolar();
        Cartesian rotatedCartesian = new Polar(oldPolar.getR(), oldPolar.getTheta() + theta).toCartesian();
        return new Position(rotatedCartesian.getX(), rotatedCartesian.getY(), oldPolar.getTheta() + theta);
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return  other.x==x
                && other.y==y
                && other.orientation==orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y,orientation);
    }

    @Override
    public String toString() {
        return "Position ( " + this.x + " , " + this.y + " , " + this.orientation +  " )";
    }

    @Override
    public String toLogs() {
        return "P(" + this.x + "|" + this.y + "|" + this.orientation +  ")";
    }
}
