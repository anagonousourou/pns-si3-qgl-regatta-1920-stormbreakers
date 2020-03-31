package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.util.function.UnaryOperator;

public abstract class Drawing {

    private final Position position;
    private final double size;
    private Color color;

    Drawing(Position position, double size, Color color) {
        this.position = position;
        this.size = size;
        this.color = color;
    }

    Drawing(Position position, double size) {
        this(position,size,Color.black);
    }

    public Position getPosition() {
        return position;
    }

    public double getSize() {
        return size;
    }

    Color getColor() {
        return color;
    }

    public void draw(Graphics g, UnaryOperator<Point2D> mapPoint) {
        Point2D mappedPoint = mapPoint.apply(getPosition().getPoint2D());
        g.setColor(getColor());
        int x1= (int) mappedPoint.x();
        int y1= (int) mappedPoint.y();
        int crossSize = 4;
        g.drawLine(x1-crossSize, y1, x1+crossSize, y1);
        g.drawLine(x1, y1-crossSize, x1, y1+crossSize);
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
