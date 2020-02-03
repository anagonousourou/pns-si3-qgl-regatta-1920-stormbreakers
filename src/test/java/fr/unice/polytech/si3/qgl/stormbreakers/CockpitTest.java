package fr.unice.polytech.si3.qgl.stormbreakers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CockpitTest {

    private Cockpit cockpit;
    private String inputInit1;
    private String inputRound11;

    @BeforeEach
    void setUp() throws IOException {
        this.inputInit1 = new String(this.getClass().getResourceAsStream("/init1.json").readAllBytes());
        this.inputRound11 = new String(this.getClass().getResourceAsStream("/round1_1.json").readAllBytes());

        this.cockpit = new Cockpit();
    }

    @Test
    void nextRoundTest() {
        this.cockpit.initGame(inputInit1);
        String result = this.cockpit.nextRound(this.inputRound11);
        assertTrue(result.contains("0"));
        assertTrue(result.contains("1"));
    }

}