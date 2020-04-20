package fr.unice.polytech.si3.qgl.stormbreakers.situational;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TheHarbourCollisionTests {


    // -- THE HARBOUR --

    private Polygon harbourReef;
    private Rectangle harbourBoat;
    private List<Position> nearCrashPositions;
    private Position onBottomBorder;

    @BeforeEach
    void initHarbourBoatAndReef() throws JsonProcessingException {
        String reef = "{ \"type\": \"polygon\", \"vertices\": [ { \"x\": \"4400\", \"y\": 0 }, { \"x\": \"3000\", \"y\": \"2700\" }, { \"x\": \"-3000\", \"y\": \"3250\" }, { \"x\": \"-4500\", \"y\": \"-150\" }, { \"x\": \"-3000\", \"y\": \"-3000\" }, { \"x\": \"3000\", \"y\": \"-3200\" } ] }\n";
        String boat = "{ \"type\": \"rectangle\", \"width\": 3.0, \"height\": 6.0, \"orientation\": 0.0 }";

        ObjectMapper mapper = new ObjectMapper();

        harbourReef = mapper.readValue(reef,Polygon.class);
        harbourBoat = mapper.readValue(boat,Rectangle.class);

        nearCrashPositions = new ArrayList<>(List.of(
                new Position ( 90 , -3690 , Math.PI/2 ),
                new Position ( 85 , -3185 , Math.PI/2 ),
                new Position ( 79 , -3008 , Math.PI/2 )
        ));

        onBottomBorder = new Position(90,-3100, Math.PI/2 );
    }

    @Test
    void testCollisionBetweenBoatAndHarbourPolygonBottom() {
        LineSegment2D harbourPolygonBottom = new LineSegment2D(new Point2D(-3000,-3000),new Point2D(3000,-3200));
        for (Position pos : nearCrashPositions) {
            harbourBoat.setAnchor(pos);
            assertFalse(harbourBoat.collidesWith(harbourPolygonBottom));
        }
        harbourBoat.setAnchor(onBottomBorder);
        assertTrue(harbourBoat.collidesWith(harbourPolygonBottom));
    }

    @Test void testCollisionBetweenShipAndHarbourPolygon() {
        for (Position pos : nearCrashPositions) {
            harbourBoat.setAnchor(pos);

            assertFalse(harbourBoat.collidesWith(harbourReef));
            assertFalse(harbourReef.collidesWith(harbourBoat));
        }

        harbourBoat.setAnchor(onBottomBorder);
        assertTrue(harbourBoat.collidesWith(harbourReef));
        assertTrue(harbourReef.collidesWith(harbourBoat));
    }

    @Test void testCollisionBetweenBoatPathAndHarbourPolygon() {
        List<Integer> shouldDetect = new ArrayList<>(List.of(
                2
        ));

        for (int i=1; i<nearCrashPositions.size(); i++) {
            Position pos1 = nearCrashPositions.get(i-1);
            Position pos2 = nearCrashPositions.get(i);
            if (! pos1.getPoint2D().equals(pos2.getPoint2D())) {
                LineSegment2D pathStep = new LineSegment2D(pos1,pos2);
                assertEquals(shouldDetect.contains(i), harbourReef.collidesWith(pathStep) );
            }
        }
    }

}
