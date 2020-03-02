package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

public class Checkpoint implements Logable {
    private Position position;
    private Shape shape;

    @JsonCreator
    public Checkpoint(@JsonProperty("position") Position pos, @JsonProperty("shape") Shape shape) {
        this.position = pos;
        this.shape = shape;
    }

    @JsonProperty("position")
    public Position getPosition() {
        return position;
    }

    @JsonProperty("shape")
    public Shape getShape() {
        return shape;
    }

    // LATER turn this to a interface method
    public boolean isPtInside(IPoint pt) {
        return this.isPtInside(pt.x(), pt.y());
    }

    private boolean isPtInside(double x, double y) {
        // On se replace par rapport au centre de la forme
        Point2D pt = new Point2D(x - position.x(), y - position.y());
        double orientation = position.getOrientation();
        // On compense l'orientation du checkpoint
        if (orientation != 0)
            pt = pt.getRotatedBy(-orientation);
        return shape.isPtInside(pt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Checkpoint))
            return false;
        Checkpoint other = (Checkpoint) obj;
        return position.equals(other.position) && shape.equals(other.shape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, shape);
    }

    @Override
    public String toString() {
        return position.toString() + " " + shape.toString();
    }

    @Override
    public String toLogs() {
        return "{" + position.toLogs() + "," + shape.toLogs() + "}";
    }
}
