package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.util.function.UnaryOperator;

public class PosDrawing extends Drawing {

    private static final int DOT_RADIUS = 4;

    public PosDrawing(Position position) {
        super(position,DOT_RADIUS);
    }

    public PosDrawing(double x, double y) {
        this(new Position(x,y));
    }

    @Override
    public void draw(Graphics g, UnaryOperator<Point2D> mapPoint) {
        new DotDrawing(getPosition()).draw(g,mapPoint);
        new Arrow(getPosition(),getPosition().getOrientation()).draw(g,mapPoint);
    }

}
