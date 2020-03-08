package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

public class Recif extends OceanEntity {

    @JsonCreator
    public Recif(
            @JsonProperty("position") Position position,
            @JsonProperty("shape") Shape shape) {
        super("reef",position, shape);
    }

    

    @Override
    public double getOrientation() {
        
        return this.position.getOrientation();
    }

}
