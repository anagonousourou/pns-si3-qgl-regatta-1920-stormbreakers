package fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw.drawings;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class LabelDrawing extends Drawing {

    private static final double STRING_RADIUS = 0;
    private final String label;

    public LabelDrawing(int number, Position position) {
        super(position, STRING_RADIUS);
        this.label = Integer.toString(number);
    }

    public LabelDrawing(double number, Position position) {
        super(position, STRING_RADIUS);
        this.label = Double.toString(number);
    }

    public LabelDrawing(String label, Position position) {
        super(position, STRING_RADIUS);
        this.label = label;
    }

    public LabelDrawing(String label, Position position, Color color) {
        super(position, STRING_RADIUS, color);
        this.label = label;
    }

    public LabelDrawing(String label, double x, double y) {
        this(label, new Position(x,y));
    }

    @Override
    public void draw(Graphics g) {
        IPoint canvasPos = getPosition().getPoint2D();

        // Drawn
        Graphics2D g2d = (Graphics2D) g;
        Font backup = g2d.getFont();

        Font font = new Font(Font.SANS_SERIF,Font.PLAIN,100);
        g2d.setFont(font);
        Rectangle2D textBounds = font.getStringBounds(label,((Graphics2D) g).getFontRenderContext());

        //Drawn from bottom left corner
        float labelX = (float) (canvasPos.x()-(textBounds.getWidth()/2)); // we center text around X
        float labelY = (float) (canvasPos.y()); // Label is just above given (X,Y)

        g2d.setColor(getColor());

        // All these shenanigans because of re-inverted Y
        g2d.scale(1,-1); // Restore axis
        g2d.drawString(label, labelX, -labelY);
        g2d.scale(1,-1); // Re-invert axis

        g2d.setFont(backup);
    }

}
