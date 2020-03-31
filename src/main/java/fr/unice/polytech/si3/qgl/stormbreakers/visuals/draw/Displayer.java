package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.Drawing;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Displayer {

    private DrawableManager drawableManager;

    private List<Checkpoint> checkpoints;
    private List<Recif> reefs;
    private List<Courant> streams;
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

    public void disp() {
        graphDrawableShapes(checkpoints,reefs,streams,shipPos);
        this.drawableManager.trace();
    }

    // -------------

    private void graphDrawableShapes(List<Checkpoint> checkpoints, List<Recif> reefs, List<Courant> streams, List<Position> shipPos) {
        graphColoredShapes(new ArrayList<>(checkpoints), Color.GREEN);
        graphColoredShapes(new ArrayList<>(reefs),Color.RED);
        graphColoredShapes(new ArrayList<>(streams),Color.BLUE);
        graphColoredShapes(new ArrayList<>(shipPos),Color.BLACK);

        graphColoredShapes(new ArrayList<>(special),new Color(170,0,255));

        graphColoredShapes(List.of(shipShape),Color.ORANGE);
    }

    private void graphColoredShapes(List<Drawable> drawables, Color color) {
        drawables.forEach(elt -> drawableManager.addElement(elt,color));
    }

    // -----------
    public void setSpecial(List<Drawable> special) {
        this.special = special;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints){
        this.checkpoints= new ArrayList<>(checkpoints);
    }

    public void setStreams(List<Courant> streams) {
        if(streams==null)return;
        this.streams = new ArrayList<>(streams);
    }

    public void setReefs(List<Recif> reefs){
        if(reefs==null)return;
        this.reefs = new ArrayList<>(reefs);
    }

    public void setShipPositions(List<Position> shipPositions){
        if(shipPositions==null)return;
        this.shipPos = shipPositions;
    }

    public void setShipShape(Shape boatShape) {
        if(boatShape==null)return;
        shipShape = boatShape;
    }

    public void addDrawing(Drawing drawing) {
        special.add(wrapDrawing(drawing));
    }

    private Drawable wrapDrawing(Drawing drawing) {
        return new Drawable() {
            @Override
            public Drawing getDrawing() {
                return drawing;
            }
        };
    }
}