package fr.unice.polytech.si3.qgl.stormbreakers.visuals.bumps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.Displayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VisuBump {

    private static final String RAW_PATH = "/raw.visuals";

    public static void main(String[] args) {
        Displayer displayer = new Displayer();


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

        //special(affichage);

        displayer.disp();
    }

    private static void special(Displayer displayer) {
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
