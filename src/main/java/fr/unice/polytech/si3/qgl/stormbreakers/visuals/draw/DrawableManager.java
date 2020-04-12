package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw;

import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.Drawing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DrawableManager extends JFrame {

    private DrawPanel drawPanel;


    public DrawableManager() {
        super("Graph disp");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height-50);
        this.drawPanel = new DrawPanel();
    }

    public void addElement(Drawable drawable, Color color) {
        Drawing drawing = drawable.getDrawing();
        drawing.setColor(color);
        drawPanel.drawElement(drawing);
    }

    public void addElement(Drawable drawable) {
        drawPanel.drawElement(drawable.getDrawing());
    }

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
