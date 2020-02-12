package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

public class Circle extends Shape {
    private double radius;

    @JsonCreator
    public Circle(@JsonProperty("radius") double radius) {
        super("circle");
        this.radius = radius;
    }

    @JsonProperty("radius")
    public double getRadius() {
        return radius;
    }

    @Override
    public boolean isPtInside(Point2D pt) {
        return distFromCenter(pt) <= radius;
    }

    private double distFromCenter(Point2D pt) {
        return new Point2D(0, 0).getDistanceTo(pt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Circle))
            return false;
        Circle other = (Circle) obj;
        return other.radius == radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(radius);
    }

    @Override
    public String toString() {
        return getType() + ": (" + radius + ")";
    }

    @Override
    public String toLogs() {
        return "C" + radius;
    }
}
