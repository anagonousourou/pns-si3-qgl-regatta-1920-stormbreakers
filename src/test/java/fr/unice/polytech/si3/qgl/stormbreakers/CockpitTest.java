package fr.unice.polytech.si3.qgl.stormbreakers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CockpitTest {

    Cockpit cockpit;
    String inputInit1, inputInit2, inputInit3, inputInit4;

    @BeforeEach
    void setUp() throws IOException {
        this.inputInit1 = new String(this.getClass().getResourceAsStream("/init1.json").readAllBytes());
        this.inputInit2 = new String(this.getClass().getResourceAsStream("/init2.json").readAllBytes());
        this.inputInit3 = new String(this.getClass().getResourceAsStream("/init3.json").readAllBytes());
        this.cockpit = new Cockpit();
    }
    

    @Disabled
    @Test
    void nextRoundTest() {
        this.cockpit.initGame(inputInit1);
        String result=this.cockpit.nextRound("{}");
        assertTrue(result.contains("0"));
        assertTrue(result.contains("1"));
    }

    @Disabled
    @Test
    void nextRoundTestComplex(){
        this.cockpit.initGame(inputInit2);
        String result=this.cockpit.nextRound("{}");
        assertTrue(result.contains("2"),"Le seul rameur à droite  rame ");
        assertTrue(result.contains("1")||result.contains("2")||result.contains("3"),
        "Un des rameurs à gauche rame" );
        assertFalse(result.contains("1") && result.contains("2"),
        "Un seul des rameurs à gauche rame" );
    }
    @Disabled
    @Test
    void nextRoundTestMoreComplex(){
        this.cockpit.initGame(inputInit3);
        String result=this.cockpit.nextRound("{}");
        assertTrue(result.contains("0"),"Le seul rameur à gauche rame ");
        assertTrue(result.contains("1")||result.contains("2")||result.contains("3"),
        "Un des rameurs à droite rame" );
        assertFalse(result.contains("1") && result.contains("2"),
        "Un seul des rameurs à droite rame" );
        assertFalse(result.contains("1") && result.contains("3"),
        "Un seul des rameurs à droite rame" );
        assertFalse(result.contains("2") && result.contains("3"),
        "Un seul des rameurs à droite rame" );
    }

    
}