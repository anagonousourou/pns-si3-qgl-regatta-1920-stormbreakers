package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.Drawing;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.PosDrawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
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

    /**
     * Places the center at canvas pos x y
     * @param x origin x
     * @param y origin y
     */
    private void setOrigin(double x, double y) {
        this.originX = x;
        this.originY = y;
    }

    // -- Queuing elements to draw --

    public void drawPos(Position position) {
        drawings.add(new PosDrawing(position.x(),position.y()));
    }

    public void drawElement(Drawing drawing){
        reframeIfNeeded(drawing.getPosition(),drawing.getSize());
        this.drawings.add(drawing);
        this.repaint();
    }

    // -- Drawing elements --

    public void paintDrawing(Graphics g, Drawing drawing) {
        drawing.draw(g);
    }

    @Override
    public void paint(Graphics g){
        g.clearRect(0,0,getWidth(),getHeight());
        Graphics2D g2d = (Graphics2D) g;
        // Create a backup of the original transform
        AffineTransform oldAT = g2d.getTransform();

        //setView(g2d, xValMin, yValMin, xValMax, yValMax);

        // frame the interest Box
        // origin: xMin,yMin
        // width: xMax-xMin
        // height: yMax-yMin
        double xRatio = getWidth()/(xValMax-xValMin);
        double yRatio = getHeight()/(yValMax-yValMin);
        g2d.translate(0,getHeight()); // Origin: top left -> bottom left
        g2d.scale(xRatio,yRatio); // for box to fit
        g2d.scale(1,-1); // y-axis: facing down -> facing up
        g2d.translate(-xValMin,-yValMin); // Origin: bottom left -> (xMin,yMin))



        super.paint(g);

        setBackground(Color.WHITE); // default
        drawAxes(g,new Point2D(0,0),Color.RED);
        g.setColor(Color.BLACK);

        for (Drawing drawing : drawings) {
            paintDrawing(g,drawing);
        }
    }

    private void drawAxes(Graphics g, Point2D center, Color color) {
        g.setColor(color);
        Point2D graphPoint = center;
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

    public Point2D valueAtPixel(Point p){
        int x = (int) map(p.x,0,getWidth(),xValMin,xValMax);
        int y = (int) map(getHeight()-p.y,0,getHeight(),yValMin,yValMax); // Inverted Y

        return new Point2D(x, y);
    }

    // -- Adapt Map Boundaries --

    private void reframeIfNeeded(Position p, double additionnalDelta) {
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



