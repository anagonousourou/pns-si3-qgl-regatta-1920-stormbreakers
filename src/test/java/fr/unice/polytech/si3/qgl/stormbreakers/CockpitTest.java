package fr.unice.polytech.si3.qgl.stormbreakers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CockpitTest {

    private Cockpit cockpit;
    private String inputInit1;
    private String inputRound11;

	private String inputGameWEEK2B;
    private String nextRoundWEEK2B;

    @BeforeEach
    void setUp() throws IOException {
        this.inputInit1 = new String(this.getClass().getResourceAsStream("/init1.json").readAllBytes());
        this.inputRound11 = new String(this.getClass().getResourceAsStream("/round1_1.json").readAllBytes());
		this.inputGameWEEK2B = new String(this.getClass().getResourceAsStream("/WEEK2B_init.json").readAllBytes());
        this.nextRoundWEEK2B = new String(this.getClass().getResourceAsStream("/WEEK2B_nextRound.json").readAllBytes());
        this.cockpit = new Cockpit();
    }

    @Test
    void nextRoundTest() {
        this.cockpit.initGame(inputInit1);
        String result = this.cockpit.nextRound(this.inputRound11);
        assertTrue(result.contains("0"));
        assertTrue(result.contains("1"));
    }

	@Test
    void noErrorOnWEEK2B() {
        assertDoesNotThrow(() -> {
            Cockpit cockpit = new Cockpit();
            cockpit.initGame(inputGameWEEK2B);

            cockpit.nextRound(nextRoundWEEK2B);

            cockpit.getLogs();
        }  );
    }

}