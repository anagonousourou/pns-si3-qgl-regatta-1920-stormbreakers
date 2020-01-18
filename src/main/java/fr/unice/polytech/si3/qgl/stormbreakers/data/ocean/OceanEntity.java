package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public abstract class OceanEntity {
    private String type;
    private Position position;
    private Shape shape;

    OceanEntity(String type, Position position, Shape shape) {
        this.type = type;
        this.position = position;
        this.shape = shape;
    }
}
