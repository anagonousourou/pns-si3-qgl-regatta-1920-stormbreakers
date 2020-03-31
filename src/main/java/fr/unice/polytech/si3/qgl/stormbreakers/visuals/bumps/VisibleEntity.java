package fr.unice.polytech.si3.qgl.stormbreakers.visuals.bumps;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.Drawable;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.Drawing;

public interface VisibleEntity extends Drawable {
    String CHECKPOINT_TOKEN = "Checkpoint";

    String getType();
    Shape getShape();

    @Override
    default Drawing getDrawing() {
        return getShape().getDrawing();
    }
}
