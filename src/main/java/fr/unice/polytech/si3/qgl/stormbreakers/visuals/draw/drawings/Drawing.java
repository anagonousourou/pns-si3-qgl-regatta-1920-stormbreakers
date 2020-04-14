package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

import java.awt.*;

public abstract class Drawing {

    private final Position position;
    private double size;
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

    public void draw(Graphics g) {
        g.setColor(getColor());
        new DotDrawing(getPosition(),getColor()).draw(g);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setSize(double newSize) {
        this.size = newSize;
    }
}
