package fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw;

import fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw.drawings.Drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class takes drawable objects to extract their drawings
 * and add them to be displayed on a DrawPanel showed by this class
 */

public class DrawableManager extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = -8888346433477453536L;
    private final DrawPanel drawPanel;


    public DrawableManager() {
        super("Graph display");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height-50);
        this.drawPanel = new DrawPanel();
    }

    /**
     * Add a drawable element to display
     * on the draw panel
     * @param drawable the element to display
     * @param color the display color
     */
    public void addElement(Drawable drawable, Color color) {
        Drawing drawing = drawable.getDrawing();
        drawing.setColor(color);
        drawPanel.addElement(drawing);
    }

    public void addElement(Drawable drawable) {
        drawPanel.addElement(drawable.getDrawing());
    }

    /**
     * Shows the DrawPanel where every element is drawn
     */
    public void trace(){
        this.getContentPane().add(this.drawPanel);
        this.setVisible(true);
        //this.setResizable(false); // NO RESIZING

        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                drawPanel.repaint();
           }
        });
    }

}
