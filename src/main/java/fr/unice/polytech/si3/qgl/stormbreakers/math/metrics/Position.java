package fr.unice.polytech.si3.qgl.stormbreakers.math.metrics;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.io.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.Drawable;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.Drawing;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.PosDrawing;

public class Position implements Logable, IPoint , Drawable {
    private final double x;
    private final double y;
    private final double orientation;

    @JsonCreator
    public Position(@JsonProperty("x") double x, @JsonProperty("y") double y,
            @JsonProperty("orientation") double orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public Position(Position toCopy) {
       this(toCopy.x,toCopy.y,toCopy.orientation);
    }

    public Position(IPoint point2D) {
        this(point2D.x(),point2D.y());
    }

    public static Position create(double x, double y) {
        return new Position(x, y);
    }

    public static Position create(double x, double y, double orientation) {
        return new Position(x, y, orientation);
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

    @JsonProperty("orientation")
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
        return Utils.almostEquals(other.x, x) && Utils.almostEquals(other.y, y)
                && Utils.almostEquals(other.orientation, orientation);
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


    // Implements drawable
    // DRAWING
    @Override
    public Drawing getDrawing() {
        return new PosDrawing(this);
    }

}
