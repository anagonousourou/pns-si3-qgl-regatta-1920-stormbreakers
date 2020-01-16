package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public abstract class OceanEntity {
    private Position position;
    private Shape shape;

    OceanEntity(Position position, Shape shape) {
        this.position = position;
        this.shape = shape;
    }
}
