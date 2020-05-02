package fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.bumps;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw.Drawable;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw.drawings.Drawing;

public interface VisibleEntity extends Drawable {
    String getType();
    Shape getShape();

    @Override
    default Drawing getDrawing() {
        return getShape().getDrawing();
    }
}
