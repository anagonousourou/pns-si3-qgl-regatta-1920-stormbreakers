package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.awt.geom.Line2D;

public class DotDrawing extends Drawing {

    private static final int CROSS_SIZE = 4;

    public DotDrawing(Position position, Color color) {
        super(position, CROSS_SIZE , color);
    }

    public DotDrawing(Position position) {
        super(position,CROSS_SIZE);
    }

    @Override
    public void draw(Graphics g) {
        Point2D point = getPosition().getPoint2D();
        double x1= point.x();
        double y1= point.y();
        double size = getSize();

        g.setColor(getColor());
        // Drawn
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(new Line2D.Double(x1-size,y1,x1+size,y1));
        g2.draw(new Line2D.Double(x1,y1-size,x1,y1+size));
    }

}
