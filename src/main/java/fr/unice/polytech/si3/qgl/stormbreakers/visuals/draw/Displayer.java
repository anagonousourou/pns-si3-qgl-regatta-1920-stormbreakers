package fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Stream;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Reef;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.DotDrawing;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.Drawing;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.LabelDrawing;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.PosDrawing;

import java.awt.*;
import java.io.IOException;
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
    public static final Color SPECIAL_COLOR = new Color(170,0,255);

    public Displayer(){
        checkpoints = new ArrayList<>();
        reefs = new ArrayList<>();
        streams = new ArrayList<>();
        shipPos = new ArrayList<>();

        special = new ArrayList<>();

        drawableManager = new DrawableManager();
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
        graphShapes(new ArrayList<>(checkpoints), Color.GREEN);
        graphShapes(new ArrayList<>(reefs), new Color(255, 206, 21));
        graphShapes(new ArrayList<>(streams),Color.BLUE);
        graphShapes(new ArrayList<>(shipPos),Color.BLACK);

        graphColoredShapes(new ArrayList<>(special));

        graphShapes(List.of(shipShape),Color.ORANGE);
    }

    /**
     * Adds drawables for display with their own color
     * @param drawables list of drawables
     */
    private void graphColoredShapes(ArrayList<Drawable> drawables) {
        drawables.forEach(drawableManager::addElement);
    }

    /**
     * Adds drawables for display with the specified color
     * @param drawables list of drawables
     * @param color display color
     */
    private void graphShapes(List<Drawable> drawables, Color color) {
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

    // Basic drawing methods

    /**
     * Adds any drawing as a "special" element
     * by wrapping it as a Drawable
     */
    public void addDrawing(Drawing drawing) {
        special.add(wrapDrawing(drawing));
    }

    /**
     * Adds a position to display
     * @param position the position
     */
    public void addPosition(Position position) {
        this.addDrawing(new PosDrawing(position));
    }

    /**
     * Adds a position to display
     * @param position the dot's position
     */
    public void addDot(Position position, Color color) {
        this.addDrawing(new DotDrawing(position, color));
    }

    private Drawable wrapDrawing(Drawing drawing) {
        return () -> drawing;
    }

    // Advanced drawing methods

    /**
     * Shows for given shapes (by id) in the given list their wrapping shape
     * @param indexes if null shows all wrapping shapes
     *        {@code new int[] {2,4,6}} will show shapes of indexes 2,4,6
     */
    public void showWrappingShapes(List<Reef> reefs, int[] indexes, double margin) {
        if (indexes != null) {
            for (int idx : indexes) {
                if (0<idx && idx<reefs.size()) showWrappingShape(reefs.get(idx), margin);
            }
        }
        else {
            for (Reef recif : reefs) {
                showWrappingShape(recif, margin);
            }
        }
    }

    /**
     * Shows for a given shape it's wrapping shape
     */
    public void showWrappingShape(Reef recif, double margin) {
        Shape wrappingShape = recif.getShape().wrappingShape(margin);
        Drawing drawing = wrappingShape.getDrawing();
        drawing.setColor(Color.RED);
        this.addDrawing(drawing);
    }

    /**
     * Adds on top of each drawable a label with a number
     * which represents it's index in the given list
     */
    public void showIndexingFor(List<Drawable> drawables) {
        drawables.stream().forEach(cp ->
                {
                    String label = "#" + drawables.indexOf(cp);
                    this.addDrawing(new LabelDrawing(label,cp.getDrawing().getPosition()));
                }
        );
    }

    /**
     * Draws entities stored in the given json resource file
     */
    private void drawOceanEntitiesFromJson(String jsonRessourcePath) {
        try {
            String specialStr = new String(Displayer.class.getResourceAsStream(jsonRessourcePath).readAllBytes());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
            List<Reef> specials = mapper.readValue(specialStr, mapper.getTypeFactory().constructCollectionType(List.class, OceanEntity.class));
            this.setSpecial(new ArrayList<>(specials));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}