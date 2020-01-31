package fr.unice.polytech.si3.qgl.stormbreakers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Rame;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

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
    

    @Test
    void nextRoundTest() {
        this.cockpit.initGame(inputInit1);
        String result=this.cockpit.nextRound("{}");
        assertTrue(result.contains("0"));
        assertTrue(result.contains("1"));
    }

    
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

    @Test
    void ramesAccessiblesTest(){
        var m1=new Marin(1,2,2,"Peter");
        List<Marin> marins=List.of(
            m1,
            new Marin(3,1,1,"Pan"),
            new Marin(2,5,3,"Robin")
            
        );
        var r1=new Rame(0,0);
        var r2=new Rame(10, 4);
        List<Equipment> equipments =List.of(r1,r2);

        var result=this.cockpit.ramesAccessibles(equipments, marins);
        assertNotEquals(null, result, "Pas null");
        assertTrue(result.get(m1).contains(r1));
        assertFalse(result.get(m1).contains(r2));


    }
}