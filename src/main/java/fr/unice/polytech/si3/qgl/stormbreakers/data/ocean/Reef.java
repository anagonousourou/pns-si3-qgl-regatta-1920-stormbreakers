package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;

public class Reef extends OceanEntity {

    @JsonCreator
    public Reef(@JsonProperty("position") Position position, @JsonProperty("shape") Shape shape) {
        super("reef", position, shape);
    }

    @Override
    public double getOrientation() {

        return this.position.getOrientation();
    }

    @Override
    public OceanEntityType getEnumType() {
        return OceanEntityType.RECIF;
    }

    @Override
    public String toString() {
        return String.format("%s(x= %f,y= %f, shape:%s)", this.getClass().getSimpleName(),this.position.x(),this.position.y(),this.shape.toString() );
    }

}
