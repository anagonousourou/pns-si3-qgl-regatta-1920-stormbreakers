package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.awt.geom.Line2D;

public class SegmentDrawing extends Drawing {

    private Point2D firstPoint;
    private Point2D lastPoint;

    public SegmentDrawing(double x1, double y1, double x2, double y2) {
        this(new Position(x1,y1),new Position(x2,y2));
    }

    public SegmentDrawing(Position startPosition, Position endPosition) {
        this(new LineSegment2D(startPosition,endPosition));
    }

    public SegmentDrawing(Position startPosition, Position endPosition,Color color) {
        this(new LineSegment2D(startPosition,endPosition),color);
    }

    public SegmentDrawing(LineSegment2D segment2D) {
        super(new Position(segment2D.getMiddle()),segment2D.length()*0.5);
        this.firstPoint = segment2D.firstPoint();
        this.lastPoint = segment2D.lastPoint();
    }

    public SegmentDrawing(LineSegment2D segment2D, Color color) {
        super(new Position(segment2D.getMiddle()),segment2D.length()*0.5,color);
        this.firstPoint = segment2D.firstPoint();
        this.lastPoint = segment2D.lastPoint();
    }

    @Override
    public void draw(Graphics g) {
        double x1 = this.firstPoint.x();
        double y1 = this.firstPoint.y();
        double x2 = this.lastPoint.x();
        double y2 = this.lastPoint.y();

        // Draw
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(getColor());
        g2.draw(new Line2D.Double(x1,y1,x2,y2));
    }

}
