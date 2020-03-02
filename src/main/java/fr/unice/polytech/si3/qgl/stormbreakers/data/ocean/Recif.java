package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;

public class Recif extends OceanEntity {

    @JsonCreator
    Recif(
            @JsonProperty("position") Position position,
            @JsonProperty("shape") Shape shape) {
        super("reef",position, shape);
    }

    @Override
    public boolean intersectsWith(LineSegment2D lineSegment2D) {
        // LATER Auto-generated method stub
        return false;
    }

}
