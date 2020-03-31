package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

import java.awt.*;
import java.util.function.UnaryOperator;

public class Arrow extends Drawing {
    private static final double ARROW_SIZE = 100;
    private final double orientation;

    public Arrow(Position position, double orientation) {
        super(position,ARROW_SIZE);
        this.orientation = orientation;
    }

    @Override
    public void draw(Graphics g, UnaryOperator<Point2D> mapPoint) {
        Vector dir = Vector.createUnitVector(this.orientation);

        Vector startToEnd = dir.scaleVector(getSize());
        Vector headRight = dir.scaleVector(-1).getRotatedBy(-0.25*Math.PI).scaleVector(getSize()/10);
        Vector headLeft = dir.scaleVector(-1).getRotatedBy(0.25*Math.PI).scaleVector(getSize()/10);

        Point2D anchor = getPosition().getPoint2D();

        Point2D start = mapPoint.apply(anchor);
        Point2D end = mapPoint.apply(anchor.getTranslatedBy(startToEnd));
        Point2D left = mapPoint.apply(anchor.getTranslatedBy(startToEnd).getTranslatedBy(headLeft));
        Point2D right = mapPoint.apply(anchor.getTranslatedBy(startToEnd).getTranslatedBy(headRight));

        g.drawLine((int) start.x(),(int) start.y(),(int) end.x(),(int) end.y());
        g.drawLine((int) end.x(),(int) end.y(),(int) left.x(),(int) left.y());
        g.drawLine((int) end.x(),(int) end.y(),(int) right.x(),(int) right.y());

    }
}
