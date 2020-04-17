package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static java.time.Duration.ofMillis;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ElementsConstructorTest {

    private ElementsConstructor constructor;
    private String game;
    private String gameArchipel;
    private String gameHarbour;
    private String roundHarbour;
    private String roundHarbour2;
    private String round1;
    private String roundArchipel;

    @BeforeEach
    public void setUp() throws IOException {
        gameHarbour = new String(this.getClass().getResourceAsStream("/elementstest/init_harbour.json").readAllBytes());
        game = new String(this.getClass().getResourceAsStream("/elementstest/init.json").readAllBytes());
        gameArchipel = new String(
                this.getClass().getResourceAsStream("/elementstest/init_archipel.json").readAllBytes());
        round1 = new String(this.getClass().getResourceAsStream("/elementstest/round1.json").readAllBytes());
        roundArchipel = new String(
                this.getClass().getResourceAsStream("/elementstest/round_archipel.json").readAllBytes());
        constructor = new ElementsConstructor(gameArchipel);
        roundHarbour = new String(
                this.getClass().getResourceAsStream("/elementstest/round_harbour.json").readAllBytes());
                roundHarbour2 = new String(
                this.getClass().getResourceAsStream("/elementstest/round_harbour2.json").readAllBytes());
    }

    @Test
    public void actionsTest() {

        assertDoesNotThrow(() -> System.out.println(constructor.sendActions(roundArchipel)));
    }

    @Test
    public void harbourTest() {

        constructor = new ElementsConstructor(gameHarbour);
        assertDoesNotThrow(() -> System.out.println(constructor.sendActions(roundHarbour)));
    }

    @Test
    public void harbour2Test() {

        constructor = new ElementsConstructor(gameHarbour);
        assertDoesNotThrow(() -> System.out.println(constructor.sendActions(roundHarbour2)));
    }



}