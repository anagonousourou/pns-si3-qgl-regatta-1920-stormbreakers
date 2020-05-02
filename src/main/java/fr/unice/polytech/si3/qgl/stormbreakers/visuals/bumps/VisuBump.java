package fr.unice.polytech.si3.qgl.stormbreakers.visuals.bumps;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Reef;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.Displayer;

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

        List<Reef> reefs = bumpParser.getReefs();
        displayer.showIndexingFor(new ArrayList<>(reefs));
        displayer.showWrappingShapes(reefs, null, 30);

        try {
            System.out.println(bumpParser.getJsonData());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        displayer.disp();
    }

}
