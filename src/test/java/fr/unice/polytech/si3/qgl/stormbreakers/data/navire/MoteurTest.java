package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.game.GameState;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;

public class MoteurTest {
    Moteur moteur;
    String inputInit1, inputInit2, inputInit3, inputInit4;
    InputParser p;
    
    @BeforeEach
    void setUp() throws IOException {
        p=new InputParser();
        this.inputInit1 = new String(this.getClass().getResourceAsStream("/init1.json").readAllBytes());
        this.inputInit2 = new String(this.getClass().getResourceAsStream("/init2.json").readAllBytes());
        this.inputInit3 = new String(this.getClass().getResourceAsStream("/init3.json").readAllBytes());
        this.inputInit4 = new String(this.getClass().getResourceAsStream("/init4.json").readAllBytes());
        
        moteur=new Moteur(new GameState(p.fetchInitGameState(inputInit1)));
    }
    @Test
    void ramesAccessiblesTest(){

        var result=this.moteur.ramesAccessibles();
        assertNotNull(result, "Pas null");

    }

    @Test
    void actionsTest(){
        var r=this.moteur.actions();
        assertNotNull(r);
        
    }

    @Test
    void actionsTestInput2(){
        this.moteur=new Moteur(new GameState(p.fetchInitGameState(inputInit2)));
        var r=this.moteur.actions();
        System.out.println(r);
        assertNotNull(r);
    }

    @Test
    void actionsTestInput3(){
        this.moteur=new Moteur(new GameState(p.fetchInitGameState(inputInit3)));
        var r=this.moteur.actions();
        System.out.println(r);
        assertNotNull(r);
    }

    @Test
    void actionsTestInput4(){
        this.moteur=new Moteur(new GameState(p.fetchInitGameState(inputInit4)));
        var r=this.moteur.actions();
        System.out.println(r);
        assertNotNull(r);
    }
}