package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.game.GameState;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;

public class MoteurTest {
    Moteur moteur;
    String inputInit1, inputInit2, inputInit3, inputInit4,inputInit5;
    InputParser p;
    
    @BeforeEach
    void setUp() throws IOException {
        p=new InputParser();
        this.inputInit1 = new String(this.getClass().getResourceAsStream("/init1.json").readAllBytes());
        this.inputInit2 = new String(this.getClass().getResourceAsStream("/init2.json").readAllBytes());
        this.inputInit3 = new String(this.getClass().getResourceAsStream("/init3.json").readAllBytes());
        this.inputInit4 = new String(this.getClass().getResourceAsStream("/init4.json").readAllBytes());
        this.inputInit5 = new String(this.getClass().getResourceAsStream("/init5.json").readAllBytes());
        
        moteur=new Moteur(new GameState(p.fetchInitGameState(inputInit1)),new Captain());
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
        this.moteur=new Moteur(new GameState(p.fetchInitGameState(inputInit2)),new Captain());
        var r=this.moteur.actions();
        System.out.println(r);
        assertNotNull(r);
    }

    @Test
    void actionsTestInput3(){
        Captain rogers=mock(Captain.class);
        this.moteur=new Moteur(new GameState(p.fetchInitGameState(inputInit3)),rogers);
        this.moteur.actions();
        verify(rogers,times(0)).minRepartition(anyList(),anyList() ,anyInt(),anyList(), anyList());
        verify(rogers,times(1)).toActivate(anyList(), anyList(),anyList(), anyList());
    }

    
    @Test
    void actionsTestInput4(){
        Captain rogers=mock(Captain.class);
        this.moteur=new Moteur(new GameState(p.fetchInitGameState(inputInit4)),rogers);
        this.moteur.actions();
        verify(rogers,times(1)).minRepartition(anyList(),anyList() ,anyInt(),anyList(), anyList());
    }
    
    @Test
    void possibleOrientationsTest(){
        var result=this.moteur.possibleOrientations();
        assertEquals(3,result.size());
        assertTrue(result.contains(0.0));
    }
    @Test
    void possibleOrientationsTestInput4(){
        this.moteur=new Moteur(new GameState(p.fetchInitGameState(inputInit4)),new Captain());
        var result=this.moteur.possibleOrientations();
        assertEquals(7,result.size());
        assertTrue(result.contains(0.0));
    }
}