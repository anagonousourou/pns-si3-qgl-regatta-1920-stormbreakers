package fr.unice.polytech.si3.qgl.stormbreakers.processing.navire;

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
import org.mockito.Mockito;

import fr.unice.polytech.si3.qgl.stormbreakers.data.game.GameState;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;

public class MoteurTest {
    private Moteur moteur,moteur2;
    private String inputInit1, inputInit2, inputInit3, inputInit4;
    private InputParser p;
    private Checkpoint c1;
    private static final double EPSILON=Math.pow(10, -10);
    @BeforeEach
    void setUp() throws IOException {
        p = new InputParser();
        this.inputInit1 = new String(this.getClass().getResourceAsStream("/init1.json").readAllBytes());
        this.inputInit2 = new String(this.getClass().getResourceAsStream("/init2.json").readAllBytes());
        this.inputInit3 = new String(this.getClass().getResourceAsStream("/init3.json").readAllBytes());
        this.inputInit4 = new String(this.getClass().getResourceAsStream("/init4.json").readAllBytes());
        
        moteur = new Moteur(new GameState(p.fetchInitGameState(inputInit1)), new Captain());
        moteur2=new Moteur(new GameState(p.fetchInitGameState(inputInit2)), new Captain());
        c1=mock(Checkpoint.class);
    }

    @Test
    void actionsTest() {
        var r = this.moteur.actions();
        assertNotNull(r);

    }

    @Test
    void actionsTestInput2() {
        this.moteur = new Moteur(new GameState(p.fetchInitGameState(inputInit2)), new Captain());
        var r = this.moteur.actions();
        assertNotNull(r);
    }

    @Test
    void actionsTestInput3() {
        Captain rogers = mock(Captain.class);
        this.moteur = new Moteur(new GameState(p.fetchInitGameState(inputInit3)), rogers);
        this.moteur.actions();
        verify(rogers, times(0)).minRepartition(anyList(), anyList(), anyInt(), anyList(), anyList());
        verify(rogers, times(1)).toActivate(anyList(), anyList(), anyList(), anyList());
    }

    @Test
    void actionsTestInput4() {
        Captain rogers = mock(Captain.class);
        this.moteur = new Moteur(new GameState(p.fetchInitGameState(inputInit4)), rogers);
        this.moteur.actions();
        verify(rogers, times(1)).minRepartition(anyList(), anyList(), anyInt(), anyList(), anyList());
    }

    @Test
    void possibleOrientationsTest() {
        var result = this.moteur.possibleOrientations();
        assertEquals(3, result.size());
        assertTrue(result.contains(0.0));
    }

    @Test
    void possibleOrientationsTestInput4() {
        this.moteur = new Moteur(new GameState(p.fetchInitGameState(inputInit4)), new Captain());
        var result = this.moteur.possibleOrientations();
        assertEquals(7, result.size());
        assertTrue(result.contains(0.0));
    }

    @Test
    void testOrientationNeededWhenAngleBoat0() {
        //bateau et checkpoint confondu
        Mockito.when(c1.getPosition()).thenReturn(new Position(0,0));
        assertEquals(0,this.moteur.orientationNeeded(c1));
        //checkpoint au nord du bateau
        Mockito.when(c1.getPosition()).thenReturn(new Position(1,0));
        assertEquals(0,this.moteur.orientationNeeded(c1));
        //checkpoint a droite du bateau
        Mockito.when(c1.getPosition()).thenReturn(new Position(0,-1));
        assertEquals(-Math.PI/2,this.moteur.orientationNeeded(c1));
        //checkpoint a pi/4 par rapport a bateau
        Mockito.when(c1.getPosition()).thenReturn(new Position(1,1));
        assertTrue(almostEqual(Math.PI/4,this.moteur.orientationNeeded(c1),EPSILON));
    }
    
    
    @Test
    void testOrientationNeededWhenAngleBoatPiBy2() {
        //checkpoint a 0,0  bateau 0,0 orienté pi/2
        Mockito.when(c1.getPosition()).thenReturn(new Position(0,0));
        assertEquals(0,this.moteur2.orientationNeeded(c1));
        //checkpoint a 1,0  bateau 0,0 orienté pi/2
        Mockito.when(c1.getPosition()).thenReturn(new Position(1,0));
        assertTrue(almostEqual(-Math.PI/2,this.moteur2.orientationNeeded(c1),EPSILON));
        //checkpoint a 1,1  bateau 0,0 orienté pi/2
        Mockito.when(c1.getPosition()).thenReturn(new Position(1,1));
        assertTrue(almostEqual(-Math.PI/4,this.moteur2.orientationNeeded(c1),EPSILON));
    }

    private boolean almostEqual(Double expected, Double result, Double epsilon) {
        return Math.abs(result-expected)<epsilon;
    }
}