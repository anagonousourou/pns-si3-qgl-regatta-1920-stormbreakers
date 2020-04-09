package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.util.function.UnaryOperator;

public class CircleDrawing extends Drawing {

    public CircleDrawing(Circle circle) {
        this(circle.getAnchor(),circle.getRadius());
    }

    public CircleDrawing(Position position, double radius) {
        super(position,radius);
    }

    @Override
    public void draw(Graphics g, UnaryOperator<Point2D> mapPoint) {
        super.draw(g,mapPoint);

        // Actual coordinates
        double radius = getSize();
        Point2D center = getPosition().getPoint2D();
        Point2D bottomLeft = center.getTranslatedBy(-radius,-radius);
        Point2D topLeft = bottomLeft.getTranslatedBy(0,2*radius);
        Point2D topRight = topLeft.getTranslatedBy(2*radius,0);

        // Graph coordinates
        Point2D ovalTopLeft = mapPoint.apply(topLeft);
        int ovalXRadius = (int) ovalTopLeft.distanceTo(mapPoint.apply(topRight));
        int ovalYRadius = (int) ovalTopLeft.distanceTo(mapPoint.apply(bottomLeft));
        // Drawn from top left corner
        g.drawOval((int) ovalTopLeft.x(), (int) ovalTopLeft.y(), ovalXRadius , ovalYRadius);
    }

}
