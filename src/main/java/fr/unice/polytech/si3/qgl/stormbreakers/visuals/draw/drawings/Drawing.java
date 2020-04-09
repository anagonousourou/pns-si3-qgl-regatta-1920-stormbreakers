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
        g.setColor(getColor());
        new DotDrawing(getPosition(),getColor()).draw(g,mapPoint);
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
