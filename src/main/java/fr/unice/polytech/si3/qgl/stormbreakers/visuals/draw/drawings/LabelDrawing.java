package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.util.function.UnaryOperator;

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
    public void draw(Graphics g, UnaryOperator<Point2D> mapPoint) {
        IPoint canvasPos = mapPoint.apply(getPosition().getPoint2D());
        g.drawString(label, (int) canvasPos.x(), (int) canvasPos.y());
    }

}
