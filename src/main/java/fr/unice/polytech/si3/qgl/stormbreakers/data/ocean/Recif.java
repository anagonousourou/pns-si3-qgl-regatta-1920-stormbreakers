package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public class Recif extends OceanEntity {

    Recif(Position position, Shape shape) {
        super("reef",position, shape);
    }

}
