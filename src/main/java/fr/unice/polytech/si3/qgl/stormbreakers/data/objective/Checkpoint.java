package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.io.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Surface;

public class Checkpoint implements Logable, Surface {
    private Position position;
    private Shape shape;

    @JsonCreator
    public Checkpoint(@JsonProperty("position") Position pos, @JsonProperty("shape") Shape shape) {
        this.position = pos;
        this.shape = shape;
        shape.setAnchor(pos);
    }

    @JsonProperty("position")
    public Position getPosition() {
        return position;
    }

    @JsonProperty("shape")
    public Shape getShape() {
        return shape;
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
        return String.format("%s(pos: %s, shape:%s)", this.getClass().getSimpleName(), position.toString(),
                shape.toString());
    }

    @Override
    public String toLogs() {
        return "CP: {" + position.toLogs() + "," + shape.toLogs() + "}";
    }

    @Override
    public double x() {
        return this.position.x();
    }

    @Override
    public double y() {
        return this.position.y();
    }

    @Override
    public double getOrientation() {
        return this.position.getOrientation();
    }
}
