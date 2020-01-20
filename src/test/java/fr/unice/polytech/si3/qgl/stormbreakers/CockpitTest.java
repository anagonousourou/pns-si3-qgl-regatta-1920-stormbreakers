package fr.unice.polytech.si3.qgl.stormbreakers;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

class CockpitTest {

    Cockpit cockpit;
    String inputInit1, inputInit2, inputInit3, inputInit4;

    @BeforeEach
    void setUp() throws IOException {
        this.inputInit1 = new String(this.getClass().getResourceAsStream("/init1.json").readAllBytes());
        this.inputInit3 = new String(this.getClass().getResourceAsStream("/init3.json").readAllBytes());
        this.cockpit = new Cockpit();
    }
    

    @Test
    void nextRoundTest() {
        this.cockpit.initGame(inputInit1);
        assertEquals("[{\"sailorId\":0,\"type\":\"OAR\"},{\"sailorId\":1,\"type\":\"OAR\"}]", this.cockpit.nextRound("{}"));
    }

    @Test
    void actionsTest2() {
        cockpit.initGame(inputInit3);
        List<SailorAction> myActions = cockpit.actions();
        assertEquals(2, myActions.size());
    }
}