package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public class Recif extends OceanEntity {
    Position position;
    Shape shape;

    Recif(Position position, Shape shape) {
        super(position, shape);
    }

}
