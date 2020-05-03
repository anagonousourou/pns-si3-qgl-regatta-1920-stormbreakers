package fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

import java.awt.*;
import java.awt.geom.Line2D;

public class Arrow extends Drawing {
    private static final double ARROW_SIZE = 100;
    private final double orientation;

    public Arrow(Position position, double orientation) {
        super(position,ARROW_SIZE);
        this.orientation = orientation;
    }

    @Override
    public void draw(Graphics g) {
        Vector dir = Vector.createUnitVector(this.orientation);

        Vector startToEnd = dir.scaleVector(getSize());
        Vector headRight = dir.scaleVector(-1).getRotatedBy(-0.25*Math.PI).scaleVector(getSize()/10);
        Vector headLeft = dir.scaleVector(-1).getRotatedBy(0.25*Math.PI).scaleVector(getSize()/10);

        Point2D anchor = getPosition().getPoint2D();

        Point2D start = anchor;
        Point2D end = anchor.getTranslatedBy(startToEnd);
        Point2D left = anchor.getTranslatedBy(startToEnd).getTranslatedBy(headLeft);
        Point2D right = anchor.getTranslatedBy(startToEnd).getTranslatedBy(headRight);

        // Draw
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(new Line2D.Double(start.x(),start.y(),end.x(),end.y()));
        g2.draw(new Line2D.Double(end.x(),end.y(),left.x(),left.y()));
        g2.draw(new Line2D.Double(end.x(),end.y(),right.x(),right.y()));
    }
}
