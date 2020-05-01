package fr.unice.polytech.si3.qgl.stormbreakers.visuals.bumps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Reef;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.Displayer;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.Drawable;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VisuBump {

    public static final String RAW_PATH = "/raw.visuals";

    public static void main(String[] args) {
        Displayer displayer = new Displayer();

        /**
         * The bumpParser is here to parse the output.bump resource file
         * to change to another week replace its' content with a bump from a previous run
         */

        BumpParser bumpParser;

        try {
            String bump = new String(VisuBump.class.getResourceAsStream(RAW_PATH + "/output.bump").readAllBytes());
            bumpParser = new BumpParser(new Scanner(bump));
        } catch (IOException e) {
            System.out.println("ERROR");
            e.printStackTrace();
            return;
        }

        displayer.setCheckpoints(bumpParser.getCheckpoints());
        displayer.setReefs(bumpParser.getReefs());
        displayer.setStreams(bumpParser.getStreams());
        displayer.setShipShape(bumpParser.getBoatShape());
        displayer.setShipPositions(bumpParser.getRoundPos());

        //special(displayer,bumpParser);

        List<Reef> reefs = bumpParser.getReefs();
        addNumbersFor(new ArrayList<>(reefs),displayer);
        showWrappingShapes(reefs, null, 30, displayer);

        try {
            System.out.println(bumpParser.getJsonData());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        displayer.disp();
    }

    /**
     * Shows for given shapes (by id) in the given list their wrapping shape
     * @param indexes if null shows all wrapping shapes
     *        {@code new int[] {2,4,6}} will show shapes of indexes 2,4,6
     */
    private static void showWrappingShapes(List<Reef> reefs, int[] indexes, double margin, Displayer displayer) {
        if (indexes != null) {
            for (int idx : indexes) {
                showWrappingShape(reefs.get(idx), margin, displayer);
            }
        }
        else {
            for (Reef recif : reefs) {
                showWrappingShape(recif, margin, displayer);
            }
        }
    }

    /**
     * Shows for a given shape it's wrapping shape
     */
    private static void showWrappingShape(Reef recif, double margin, Displayer displayer) {
        displayer.addDrawing(recif.getShape().wrappingShape(margin).getDrawing());
    }

    /**
     * Adds on top of each drawable given a number
     * which represents it's index in parsing order
     */
    private static void addNumbersFor(List<Drawable> drawables, Displayer displayer) {
        drawables.stream().forEach(cp ->
                {
                    String label = "#" + drawables.indexOf(cp);
                    displayer.addDrawing(new LabelDrawing(label,cp.getDrawing().getPosition()));
                }
        );
    }

    /**
     * Here add elements to draw which are not originally part of the map
     * either with {@code displayer.setSpecial(List<Drawable>)}
     * or with {@code displayer.addDrawing(Drawing)}
     */
    private static void special(Displayer displayer, BumpParser bumpParser) {

        drawOceanEntitiesFromJson(displayer);
    }

    /**
     * Draws entities stored in the special.json resource file
     */
    private static void drawOceanEntitiesFromJson(Displayer displayer) {
        try {
            String specialStr = new String(VisuBump.class.getResourceAsStream(RAW_PATH + "/special.json").readAllBytes());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
            List<Reef> specials = mapper.readValue(specialStr, mapper.getTypeFactory().constructCollectionType(List.class, OceanEntity.class));
            displayer.setSpecial(new ArrayList<>(specials));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
