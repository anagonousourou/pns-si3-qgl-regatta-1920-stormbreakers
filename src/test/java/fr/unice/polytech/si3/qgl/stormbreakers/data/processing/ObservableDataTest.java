package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;
import fr.unice.polytech.si3.qgl.stormbreakers.io.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.io.json.JsonInputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.StreamManager;

public class ObservableDataTest {

    ObservableData observableData = new ObservableData();
    private StreamManager streamManager;
    private String round;
    private String round2;
    InputParser parser = new JsonInputParser();

    @BeforeEach
    void setUp() throws IOException {
        round = new String(this.getClass().getResourceAsStream("/observabletest/round1.json").readAllBytes());
        round2 = new String(this.getClass().getResourceAsStream("/observabletest/round2.json").readAllBytes());
    }

    @Test
    void gettersSettersTest() {
        observableData.setValue("Bienvenue");
        assertEquals("Bienvenue", observableData.getValue());
    }

    @Test
    void observableBehaviorTest() {
        Wind wind = new Wind(parser);
        EquipmentsManager equipmentManager = new EquipmentsManager(List.of(), 3, parser);
        Boat boat = new Boat(new Position(45, 23.6), 6, 6, 100, parser, new Rectangle(5, 5, 0));

        observableData.addPropertyChangeListener(wind);
        observableData.addPropertyChangeListener(equipmentManager);
        observableData.addPropertyChangeListener(boat);

        assertFalse(equipmentManager.nbOars() != 0);
        assertTrue(equipmentManager.allLeftOars().isEmpty());
        assertEquals(100, boat.getLife(), "100 comme indique dans le constructeur");
        assertEquals(0.0, wind.getOrientation(), "0.0 par defaut");
        assertEquals(0.0, wind.getStrength(), "0.0 par defaut");

        observableData.setValue(round);

        assertTrue(equipmentManager.nbOars() != 0);
        assertFalse(equipmentManager.allLeftOars().isEmpty(), "Il y a maintenant des rames ha ha");
        assertFalse(equipmentManager.allRightOars().isEmpty());
        assertEquals(60, boat.getLife(), "60 comme indiqué dans le round");

        assertEquals(0.78539816339744, wind.getOrientation(), "comme indiqué dans le round");
        assertEquals(200.0, wind.getStrength(), "comme indiqué dans le round");

    }

    @Test
    
    void streamManagerTest() {
        streamManager = new StreamManager(parser, null);
        observableData.addPropertyChangeListener(streamManager);
        observableData.setValue(round2);

        assertEquals(1, streamManager.getStreams().size());
        assertEquals(11, streamManager.getRecifs().size());
        assertEquals(13, streamManager.getObstacles().size());

        

    }

}