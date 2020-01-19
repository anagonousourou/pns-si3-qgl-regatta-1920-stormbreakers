package fr.unice.polytech.si3.qgl.stormbreakers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class CockpitTest {

    Cockpit cockpit;
    String inputInit;

    @BeforeEach
    void setUp() throws IOException {
        this.inputInit=new String(this.getClass().getResourceAsStream("/init.json").readAllBytes());
        this.cockpit = new Cockpit();
    }
    

    @Test
    void nextRoundTest() {
        this.cockpit.initGame(inputInit);
        assertEquals("[{\"sailorId\":0,\"type\":\"oar\"},{\"sailorId\":1,\"type\":\"oar\"}]", this.cockpit.nextRound("{}"));
    }
}