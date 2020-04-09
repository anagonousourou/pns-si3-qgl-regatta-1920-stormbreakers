package fr.unice.polytech.si3.qgl.stormbreakers.visuals.bumps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.Displayer;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class VisuBump {

    private static final String RAW_PATH = "/raw.visuals";

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

        displayer.disp();
    }


    /**
     * Here add elements to draw which are not originally part of the map
     * either with {@codedisplayer.setSpecial(List<Drawable>)}
     * or with {@codedisplayer.addDrawing(Drawing)}
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
            // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
            List<Recif> specials = mapper.readValue(specialStr, mapper.getTypeFactory().constructCollectionType(List.class, OceanEntity.class));
            displayer.setSpecial(new ArrayList<>(specials));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
