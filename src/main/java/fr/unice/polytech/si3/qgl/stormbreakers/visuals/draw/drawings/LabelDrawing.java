package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class LabelDrawing extends Drawing {

    private static final double STRING_RADIUS = 0;
    private final String label;

    public LabelDrawing(double number, Position position) {
        super(position, STRING_RADIUS);
        this.label = Double.toString(number);
    }

    public LabelDrawing(String label, Position position) {
        super(position, STRING_RADIUS);
        this.label = label;
    }

    public LabelDrawing(String label, double x, double y) {
        this(label, new Position(x,y));
    }

    @Override
    public void draw(Graphics g) {
        IPoint canvasPos = getPosition().getPoint2D();

        // Drawn
        Graphics2D g2 = (Graphics2D) g;

        FontMetrics fontMetrics = g2.getFontMetrics();
        Rectangle2D textBounds = fontMetrics.getStringBounds(label, g2);

        //Drawn from bottom left corner
        float labelX = (float) (canvasPos.x()-(textBounds.getWidth()/2)); // we center text around X
        float labelY = (float) (canvasPos.y()); // Label is just above given (X,Y)
        g2.drawString(label, labelX, labelY);
    }

}
