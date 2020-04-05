package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings;


import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.awt.*;
import java.util.function.UnaryOperator;

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
    public void draw(Graphics g, UnaryOperator<Point2D> mapPoint) {
        Point2D mappedPoint1 = mapPoint.apply(this.firstPoint);
        Point2D mappedPoint2 = mapPoint.apply(this.lastPoint);
        int x1= (int) mappedPoint1.x();
        int y1= (int) mappedPoint1.y();
        int x2= (int) mappedPoint2.x();
        int y2 =(int) mappedPoint2.y();
        g.setColor(getColor());
        g.drawLine(x1, y1, x2, y2);
    }

}
