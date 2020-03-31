package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.util.function.UnaryOperator;

public class DotDrawing extends Drawing {

    private static final int CROSS_SIZE = 4;

    public DotDrawing(double x, double y) {
        this(new Position(x,y));
    }

    public DotDrawing(Position position, Color color) {
        super(position, CROSS_SIZE , color);
    }

    public DotDrawing(Position position) {
        super(position,CROSS_SIZE);
    }

    @Override
    public void draw(Graphics g, UnaryOperator<Point2D> mapPoint) {
        Point2D mappedPoint = mapPoint.apply(getPosition().getPoint2D());
        int x1= (int) mappedPoint.x();
        int y1= (int) mappedPoint.y();
        int size = (int) getSize();
        g.setColor(getColor());
        g.drawLine(x1-size, y1, x1+size, y1);
        g.drawLine(x1, y1-size, x1, y1+size);
    }

}
