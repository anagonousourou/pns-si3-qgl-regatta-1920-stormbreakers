package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Stream;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Reef;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.Drawing;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the different map (Drawable) elements
 * and adds them to be displayed in a drawPanel
 */

public class Displayer {

    private final DrawableManager drawableManager;

    private List<Checkpoint> checkpoints;
    private List<Reef> reefs;
    private List<Stream> streams;
    private List<Position> shipPos;
    private Shape shipShape;

    private List<Drawable> special;

    public Displayer(){
        checkpoints = new ArrayList<>();
        reefs = new ArrayList<>();
        streams = new ArrayList<>();
        shipPos = new ArrayList<>();

        special = new ArrayList<>();

        drawableManager = new DrawableManager();
    }

    public DrawableManager getDrawableManager(){
        return drawableManager;
    }

    /**
     * Tells the inner Drawable Manager to display added Drawables
     */
    public void disp() {
        graphDrawableShapes(checkpoints,reefs,streams,shipPos);
        this.drawableManager.trace();
    }

    // -------------

    /**
     * Adds for display every map element with their corresponding color
     */
    private void graphDrawableShapes(List<Checkpoint> checkpoints, List<Reef> reefs, List<Stream> streams, List<Position> shipPos) {
        graphColoredShapes(new ArrayList<>(checkpoints), Color.GREEN);
        graphColoredShapes(new ArrayList<>(reefs),Color.RED);
        graphColoredShapes(new ArrayList<>(streams),Color.BLUE);
        graphColoredShapes(new ArrayList<>(shipPos),Color.BLACK);

        graphColoredShapes(new ArrayList<>(special),new Color(170,0,255));

        graphColoredShapes(List.of(shipShape),Color.ORANGE);
    }

    /**
     * Adds drawables for display with the specified color
     * @param drawables list of drawables
     * @param color display color
     */
    private void graphColoredShapes(List<Drawable> drawables, Color color) {
        drawables.forEach(elt -> drawableManager.addElement(elt,color));
    }

    // -----------

    /**
     * Adds a list of drawables as "special" elements
     */
    public void setSpecial(List<Drawable> special) {
        this.special = special;
    }

    /**
     * Adds a list of drawables as "checkpoints" elements
     */
    public void setCheckpoints(List<Checkpoint> checkpoints){
        this.checkpoints= new ArrayList<>(checkpoints);
    }

    /**
     * Adds a list of drawables as "streams" elements
     */
    public void setStreams(List<Stream> streams) {
        if(streams==null)return;
        this.streams = new ArrayList<>(streams);
    }

    /**
     * Adds a list of drawables as "reefs" elements
     */
    public void setReefs(List<Reef> reefs){
        if(reefs==null)return;
        this.reefs = new ArrayList<>(reefs);
    }

    /**
     * Adds a list of positions representing the different ship steps
     */
    public void setShipPositions(List<Position> shipPositions){
        if(shipPositions==null)return;
        this.shipPos = shipPositions;
    }

    /**
     * Sets the ship shape
     */
    public void setShipShape(Shape boatShape) {
        if(boatShape==null)return;
        shipShape = boatShape;
    }

    /**
     * Adds any drawing as a "special" element
     * by wrapping it as a Drawable
     */
    public void addDrawing(Drawing drawing) {
        special.add(wrapDrawing(drawing));
    }

    private Drawable wrapDrawing(Drawing drawing) {
        return () -> drawing;
    }
}