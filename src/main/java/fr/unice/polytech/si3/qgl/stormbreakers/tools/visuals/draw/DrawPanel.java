package fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.*;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw.drawings.DotDrawing;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw.drawings.Drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a panel where added Drawings are displayed
 */

public class DrawPanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final List<Drawing> drawings = new ArrayList<>();

    // interest Box
    private double xValMin=Double.MAX_VALUE; // Can only go down
    private double yValMin=Double.MAX_VALUE;
    private double xValMax=Double.MIN_VALUE; // Can only go up
    private double yValMax=Double.MIN_VALUE;
    private static final double MARGIN = 10;

    private Point2D zoomCornerA;
    private Point2D zoomCornerC;
    private Point pointer; // Mouse pixel position

    private Point2D pickedValue;

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
                pointer = e.getPoint();
                switch(e.getButton()) {
                    case MouseEvent.BUTTON1: // LEFT CLICK
                        if (e.isShiftDown()) {
                            restartZoomSelection();
                        } else {
                            pickValue();
                        }
                        break;
                    case MouseEvent.BUTTON2: // MIDDLE CLICK
                        if (e.isShiftDown()) {
                            cancelZoom();
                        } else {
                            unpickValue();
                        }
                        break;
                    case MouseEvent.BUTTON3: // RIGHT CLICK
                        if (e.isShiftDown()) {
                            endZoomSelection();
                        }
                        break;
                    default: break;
                }
                repaint();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                pointer = e.getPoint();
                if (!e.isShiftDown()) {
                    resetZoomSelection();
                }
                repaint();
            }
        });
    }

    // -- PICKING POS --

    private void pickValue() {
        pickedValue = valueAtPixel(pointer);
    }

    private void unpickValue() {
        pickedValue = null;
    }

    // -- ZOOMING --

    private boolean zoomInactive() {
        return zoomCornerA==null && zoomCornerC==null;
    }

    private boolean zoomSelectionStarted() {
        return zoomCornerA!=null;
    }

    private boolean zoomSelectionIncomplete() {
        return zoomSelectionStarted() && zoomCornerC==null;
    }

    private void restartZoomSelection() {
        if (zoomInactive()) // we don't want to change corners when already zoomed in
            zoomCornerA = valueAtPixel(pointer);
    }

    private void endZoomSelection() {
        if (!zoomSelectionIncomplete()) return;
        Point2D start = zoomCornerA;
        Point2D end =  valueAtPixel(pointer);
        if (start!=null) zoom(start,end);
    }

    private void cancelZoom() {
        zoomCornerA = null;
        zoomCornerC = null;
    }

    private void resetZoomSelection() {
        if (zoomInactive() || zoomSelectionIncomplete() ) cancelZoom();
    }

    private void zoom(Point2D start, Point2D end) {
        if (zoomInactive() || Utils.almostEquals(start,end,0.001)) return; // Cannot zoom when zoomed in

        // We're trying to make a framing rectangle
        Point2D center = new LineSegment2D(start,end).getMiddle();
        Vector diagVector = new Vector(start,end);
        double width = Math.abs(diagVector.getDeltaX());
        double height= Math.abs(diagVector.getDeltaY());

        zoomCornerA = center.getTranslatedBy(new Vector(-width/2,-height/2));
        zoomCornerC = center.getTranslatedBy(new Vector(width/2,height/2));
    }

    // -- QUEUING --

    /**
     * Adds a drawing to display
     * @param drawing the drawing
     */
    public void addElement(Drawing drawing){
        adaptBoundaries(drawing.getPosition(),drawing.getSize());
        this.drawings.add(drawing);
        this.repaint();
    }

    // -- DRAWING --

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
        Graphics2D g2d = (Graphics2D) g;

        // Create a backup of the original transform
        AffineTransform oldAT = g2d.getTransform();

        frameContent(g2d);

        g.setColor(Color.BLACK);
        for (Drawing drawing : drawings) {
            paintDrawing(g,drawing);
        }

        overlayLockedValue(g2d);
        overlaySelectionRectangle(g2d);

        // Restore the original transform
        g2d.setTransform(oldAT);

        overlayLockedDetails(g2d);
    }

    // -- OVERLAY --

    private void overlaySelectionRectangle(Graphics2D g2d) {
        if (zoomSelectionIncomplete()) {
            Point2D currentCursor = valueAtPixel(pointer);

            //creates a copy of the Graphics instance
            Graphics2D g2dTwo = (Graphics2D) g2d.create();

            //set the stroke of the copy, not the original
            Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            g2dTwo.setStroke(dashed);

            Path2D.Double path2D = new Path2D.Double();
            path2D.moveTo(zoomCornerA.x(),zoomCornerA.y());
            path2D.lineTo(zoomCornerA.x(),currentCursor.y());
            path2D.lineTo(currentCursor.x(),currentCursor.y());
            path2D.lineTo(currentCursor.x(),zoomCornerA.y());
            path2D.closePath();

            g2dTwo.draw(path2D);

            //gets rid of the copy
            g2dTwo.dispose();
        }
    }

    private void overlayLockedDetails(Graphics2D g2d) {
        g2d.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,28));
        g2d.setColor(Color.BLACK);
        String str = (pickedValue !=null)? pickedValue.toString():"Click to get cursor pos ..";
        g2d.drawString(str,0,getHeight());
    }

    private void overlayLockedValue(Graphics2D g2d) {
        if (pickedValue !=null) {
            DotDrawing lockedCursor = new DotDrawing(new Position(pickedValue));
            lockedCursor.setColor(new Color(255, 135, 0));
            lockedCursor.setSize(25);
            paintDrawing(g2d,lockedCursor);
        }
    }

    // -- FRAMING --

    private Point2D getTopLeftCornerValue() {
        return (zoomCornerA!=null && zoomCornerC!=null) ? zoomCornerA : new Point2D(xValMin,yValMin);
    }

    private Point2D getBottomRightCornerValue() {
        return (zoomCornerA!=null && zoomCornerC!=null) ? zoomCornerC : new Point2D(xValMax,yValMax);
    }

    private void frameContent(Graphics2D g2d) {
        // frame the interest Box
        Point2D ptA = getTopLeftCornerValue();
        Point2D ptC = getBottomRightCornerValue();
        frameSpecificBox(ptA.x(), ptA.y(), ptC.x(), ptC.y(), g2d);
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

    // -- PIXEL MAPPING --

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
        Point2D ptA = getTopLeftCornerValue();
        Point2D ptC = getBottomRightCornerValue();

        double x = map(p.x,0,getWidth(),ptA.x(),ptC.x());
        int restoredY = getHeight()-p.y; // restore Y-axis orientation
        double y = map(restoredY,0,getHeight(),ptA.y(),ptC.y());

        return new Point2D(x, y);
    }

    // -- BOUNDARIES --

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



