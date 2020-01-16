package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public class AutreBateau extends OceanEntity {
    private int life;

    AutreBateau(Position position, Shape shape, int life) {
        super(position,shape);
        this.life=life;
    }

}
