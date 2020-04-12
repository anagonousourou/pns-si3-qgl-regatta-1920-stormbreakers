package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

import java.awt.*;

public class PosDrawing extends Drawing {

    private static final int DOT_RADIUS = 4;

    public PosDrawing(Position position) {
        super(position,DOT_RADIUS);
    }

    public PosDrawing(double x, double y) {
        this(new Position(x,y));
    }

    @Override
    public void draw(Graphics g) {
        new DotDrawing(getPosition()).draw(g);
        new Arrow(getPosition(),getPosition().getOrientation()).draw(g);
    }

}
