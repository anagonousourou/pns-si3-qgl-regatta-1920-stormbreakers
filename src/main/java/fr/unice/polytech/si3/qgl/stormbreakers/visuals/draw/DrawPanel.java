package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.RectanglePositioned;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.DotDrawing;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.Drawing;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.PosDrawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a panel where added Drawings are displayed
 */

public class DrawPanel extends JPanel {
    private final List<Drawing> drawings =new ArrayList<>();

    // interest Box
    private double xValMin=Double.MAX_VALUE; // Can only go down
    private double yValMin=Double.MAX_VALUE;
    private double xValMax=Double.MIN_VALUE; // Can only go up
    private double yValMax=Double.MIN_VALUE;
    private static final double MARGIN = 10;

    Point2D zoomCornerA;
    Point2D zoomCornerC;
    Point pointer;

    private Point2D lockedValue;

    public DrawPanel() {
        super();

        addMouseListener(new MouseAdapter() {
            /**
             * On left click updates cursorValue
             * On right click resets cursorValue
             * On any click repaints
             * @param e MouseEvent
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                switch(e.getButton()) {
                    case MouseEvent.BUTTON1: // LEFT CLICK
                        if (e.isShiftDown()) {
                            pointer = e.getPoint();
                        } else {
                            lockedValue = valueAtPixel(e.getPoint());
                        }
                        break;
                    case MouseEvent.BUTTON2: // MIDDLE CLICK
                        lockedValue = null;
                        if (e.isShiftDown()) {
                            pointer = null;
                            zoomCornerA = null;
                            zoomCornerC = null;
                        }
                        break;
                    case MouseEvent.BUTTON3: // RIGHT CLICK
                        if (e.isShiftDown()) {
                            Point2D start = valueAtPixel(e.getPoint());
                            Point2D end = valueAtPixel(pointer);
                            if (pointer!=null) zoom(start,end);
                        }
                        break;
                }
                repaint();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }
        });
    }

    private void zoom(Point2D start, Point2D end) {
        // We're trying to make a framing rectangle
        Point2D center = new LineSegment2D(start,end).getMiddle();
        Vector diagVector = new Vector(start,end);
        double width = Math.abs(diagVector.getDeltaX());
        double height= Math.abs(diagVector.getDeltaY());

        zoomCornerA = center.getTranslatedBy(new Vector(-width/2,-height/2));
        zoomCornerC = center.getTranslatedBy(new Vector(width/2,height/2));
    }

    // -- Drawing Panel config --

    // -- Queuing elements to draw --

    /**
     * Adds a position to display
     * @param position the position
     */
    public void drawPos(Position position) {
        drawings.add(new PosDrawing(position.x(),position.y()));
    }

    /**
     * Adds a drawing to display
     * @param drawing the drawing
     */
    public void drawElement(Drawing drawing){
        adaptBoundaries(drawing.getPosition(),drawing.getSize());
        this.drawings.add(drawing);
        this.repaint();
    }

    // -- Drawing elements --

    /**
     * Paints a drawing on the panel
     */
    public void paintDrawing(Graphics g, Drawing drawing) {
        drawing.draw(g);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        setBackground(Color.WHITE); // default
        //g.clearRect(0,0,getWidth(),getHeight());

        Graphics2D g2d = (Graphics2D) g;
        // Create a backup of the original transform
        AffineTransform oldAT = g2d.getTransform();

        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        // frame the interest Box
        if (zoomCornerA!=null && zoomCornerC!=null) {
            frameSpecificBox(zoomCornerA.x(), zoomCornerA.y(), zoomCornerC.x(), zoomCornerC.y(), g2d);
        } else {
            frameSpecificBox(xValMin, yValMin, xValMax, yValMax, g2d);
        }

        drawAxes(g2d,new Point2D(0,0),Color.RED);
        g.setColor(Color.BLACK);

        for (Drawing drawing : drawings) {
            paintDrawing(g,drawing);
        }

        if (lockedValue!=null) {
            DotDrawing lockedCursor = new DotDrawing(new Position(lockedValue));
            lockedCursor.setColor(new Color(255, 135, 0));
            lockedCursor.setSize(25);
            paintDrawing(g,lockedCursor);
        }


        // Restore the original transform
        g2d.setTransform(oldAT);


        g2d.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,28));
        g2d.setColor(Color.BLACK);
        String str = (lockedValue!=null)?lockedValue.toString():"Click to get cursor pos ..";
        g2d.drawString(str,0,getHeight());

    }

    private void frameSpecificBox(double minX, double minY, double maxX, double maxY, Graphics2D g2d) {
        // origin: xMin,yMin
        // width: xMax-xMin
        // height: yMax-yMin
        double xRatio = getWidth()/(maxX-minX);
        double yRatio = getHeight()/(maxY-minY);
        g2d.translate(0,getHeight()); // Origin: top left -> bottom left
        g2d.scale(xRatio,yRatio); // for box to fit
        g2d.scale(1,-1); // y-axis: facing down -> facing up
        g2d.translate(-minX,-minY); // Origin: bottom left -> (xMin,yMin))
    }

    /**
     * Draws axis on the panel
     * @param center the anchor point / origin
     * @param color the drawing color
     */
    private void drawAxes(Graphics2D g2d, Point2D center, Color color) {
        g2d.setColor(color);
        g2d.draw(new Line2D.Double(0.0,center.y(),getWidth(),center.y())); // (Ox)
        g2d.draw(new Line2D.Double(center.x(),0.0,center.x(),getHeight())); // (Oy)
    }

    // -- Position <-> Pixel --

    /**
     * Maps a given value from [oldMin,oldMax] to [min,max]
     * @return the mapped value
     */
    private double map(double value, double oldMin, double oldMax, double min, double max) {
        // Y = (X-A)/(B-A) * (D-C) + C
        return (value-oldMin)/(oldMax-oldMin) * (max-min) + min;
    }

    /**
     * Gets the value point corresponding to a given pixel
     * from the panel
     * @return the value point
     */
    private Point2D valueAtPixel(Point p){
        double x = map(p.x,0,getWidth(),xValMin,xValMax);
        int restoredY = getHeight()-p.y; // restore Y-axis orientation
        double y = map(restoredY,0,getHeight(),yValMin,yValMax);

        return new Point2D(x, y);
    }

    // -- Adapt Map Boundaries --

    /**
     * Adapts display boundaries
     * when elements don't fit anymore
     * @param p new element's position
     * @param additionalDelta new element's width/height
     */
    private void adaptBoundaries(Position p, double additionalDelta) {
        additionalDelta += MARGIN;

        adaptXboundaries(p.x(),additionalDelta);
        adaptYboundaries(p.y(),additionalDelta);
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



