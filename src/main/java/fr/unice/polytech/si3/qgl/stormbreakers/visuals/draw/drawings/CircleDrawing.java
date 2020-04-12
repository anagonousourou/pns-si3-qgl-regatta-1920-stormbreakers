package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CircleDrawing extends Drawing {

    public CircleDrawing(Circle circle) {
        this(circle.getAnchor(),circle.getRadius());
    }

    public CircleDrawing(Position position, double radius) {
        super(position,radius);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        // Actual coordinates
        double radius = getSize();
        Point2D center = getPosition().getPoint2D();
        Point2D bottomLeft = center.getTranslatedBy(-radius,-radius);
        Point2D topLeft = bottomLeft.getTranslatedBy(0,2*radius);
        Point2D topRight = topLeft.getTranslatedBy(2*radius,0);

        // Graph coordinates
        double ovalXDiameter = topLeft.distanceTo(topRight);
        double ovalYDiameter = topLeft.distanceTo(bottomLeft);

        // Drawn from "pixel top left corner" ("real bottom-left" when y-axis reversed)
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(new Ellipse2D.Double(bottomLeft.x(),bottomLeft.y(),ovalXDiameter,ovalYDiameter));
    }

}
