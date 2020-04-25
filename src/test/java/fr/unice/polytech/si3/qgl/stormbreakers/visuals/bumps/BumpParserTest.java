package fr.unice.polytech.si3.qgl.stormbreakers.visuals.bumps;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.ShapeType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class BumpParserTest {

    private BumpParser bumpParser;

    @BeforeEach
    void setUp() throws IOException {
        String bump = new String(VisuBump.class.getResourceAsStream(VisuBump.RAW_PATH + "/output.bump").readAllBytes());
        bumpParser = new BumpParser(new Scanner(bump));
    }

    @Test
    void testReefCorrectlyParsed() {
        String firstLine = "ENTITY 1346201722 reef polygon 350.0 0.0 249.99999999999997 250.0 -99.99999999999994 150.00000000000003 -150.0 -150.00000000000003 249.99999999999997 -200.00000000000003";
        String secondLine = "POSITION 1346201722 5048.0192076830845 2530.0120048019226 0.8377580409572781";

        VisibleEntity reefEntity = bumpParser.parseEntity(firstLine,secondLine);
        assertEquals(BumpParser.REEF_TOKEN,reefEntity.getType());
        Recif reef = (Recif) reefEntity;

        assertEquals(ShapeType.POLYGON,reef.getShape().getTypeEnum());
        Polygon polygonReef = (Polygon) reef.getShape();

        assertEquals(polygonReef.getOrientation(),0);
        assertEquals(new Position(5048.0192076830845,2530.0120048019226,0.8377580409572781),reef.getPosition());

        // Shape ?
    }

}