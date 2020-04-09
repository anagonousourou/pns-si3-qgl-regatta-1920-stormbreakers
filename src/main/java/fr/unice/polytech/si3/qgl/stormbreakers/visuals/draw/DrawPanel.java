package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.Drawing;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.PosDrawing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel {
    private List<Drawing> drawings =new ArrayList<>();

    private double xValMin=Double.MAX_VALUE; // Can only go down
    private double yValMin=Double.MAX_VALUE;
    private double xValMax=Double.MIN_VALUE; // Can only go up
    private double yValMax=Double.MIN_VALUE;

    private static final double MARGIN = 10;

    // Origin pos val
    private double originX = 0;
    private double originY = 0;

    public DrawPanel() {
        super();
    }

    // -- Drawing Panel config --

    void setGraphValOrigin(Vector translation) {
        setGraphValOrigin(translation.getDeltaX(),translation.getDeltaY());
    }

    void setGraphValOrigin(double dx, double dy) {
        setOrigin(dx, dy);
        repaint();
    }

    /**
     * Places the center at canvas pos x y
     * @param x origin x
     * @param y origin y
     */
    private void setOrigin(double x, double y) {
        this.originX = map(x,xValMin,xValMax,0,getWidth());
        this.originY = map(y,yValMin,yValMax,0,getHeight());
    }

    // -- Queuing elements to draw --

    public void drawPos(Position position) {
        drawings.add(new PosDrawing(position.x(),position.y()));
    }

    public void drawElement(Drawing drawing){
        zoomOutIfNeeded(drawing.getPosition(),drawing.getSize());
        this.drawings.add(drawing);
        this.repaint();
    }

    // -- Drawing elements --

    public void paintDrawing(Graphics g, Drawing drawing) {
        drawing.draw(g, this::valueToGraph);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        setOrigin(0,0);

        setBackground(Color.WHITE); // default
        drawAxes(g,new Point2D(0,0),Color.RED);
        g.setColor(Color.BLACK);

        for (Drawing drawing : drawings) {
            paintDrawing(g,drawing);
        }
    }

    private void drawAxes(Graphics g, Point2D center, Color color) {
        g.setColor(color);
        Point2D graphPoint = valueToGraph(center);
        int centerX = (int) graphPoint.x();
        int centerY = (int) graphPoint.y();

        g.drawLine(0,centerY, getWidth(),centerY);  // (Ox)
        g.drawLine(centerX,0, centerX,getHeight()); // (Oy)
    }

    // -- Position <-> Pixel --

    double map(double value, double oldMin, double oldMax, double min, double max) {
        // Y = (X-A)/(B-A) * (D-C) + C
        return (value-oldMin)/(oldMax-oldMin) * (max-min) + min;
    }

    public Point2D valueToGraph(IPoint p){
        int x = (int) map(p.x(),xValMin,xValMax,0,getWidth());
        int y = (int) map(p.y(),yValMin,yValMax,0,getHeight()); // Inverted Y

        return new Point2D(x, getHeight() - y);
    }

    // -- Adapt Map Boundaries --

    private void zoomOutIfNeeded(Position p, double additionnalDelta) {
        additionnalDelta += MARGIN;

        adaptXboundaries(p.x(),additionnalDelta);
        adaptYboundaries(p.y(),additionnalDelta);
    }

    private void adaptXboundaries(double value,double delta) {
        if(value-delta<this.xValMin)
            this.xValMin = value-delta;
        if(value+delta>this.xValMax)
            this.xValMax = value+delta;
    }

    private void adaptYboundaries(double value, double delta) {
        if(value-delta<this.yValMin)
            this.yValMin = value-delta;
        if(value+delta>this.yValMax)
            this.yValMax = value+delta;
    }

    // ------------



}



