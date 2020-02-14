package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;

public class EquipmentManagerTest {
    EquipmentManager equipmentManager;
    Equipment r1 = new Oar(1, 0);
    Equipment r2 = new Oar(1, 2);
    Equipment r3 = new Oar(2, 0);
    Equipment r4 = new Oar(2, 2);
    Equipment r5 = new Oar(3, 0);
    Equipment r6 = new Oar(3, 2);
    Equipment r7 = new Sail(3, 1);
    Sail r8 = new Sail(3, 0);

    @BeforeEach
    void setUp() {

        equipmentManager = new EquipmentManager(List.of(r1, r2, r3, r4, r5, r6,r7,r8), 3);
        r8.setOpenned(true);
    }

    @Test
    void testRudderIsPresent() {
        assertFalse(this.equipmentManager.rudderIsPresent(), "Pas de Gouvernail");
    }

    @Test
    void testAllLeftOars(){
        List<Oar> leftOars= equipmentManager.allLeftOars();
        assertTrue(leftOars.contains(r1));
        assertTrue(leftOars.contains(r3));
        assertTrue(leftOars.contains(r5));
        assertEquals(3,leftOars.size());
    }
    @Test
    void testAllRightOars(){
        List<Oar> rightOars= equipmentManager.allRightOars();
        assertTrue(rightOars.contains(r2));
        assertTrue(rightOars.contains(r4));
        assertTrue(rightOars.contains(r6));
        assertEquals(3,rightOars.size());
    }
    
    @Test
    void testNbOars(){
        assertEquals(6,this.equipmentManager.nbOars(), "6 rames en tout");
    }

    @Test
    void testOarPresentAt(){
        assertTrue(this.equipmentManager.oarPresentAt( new IntPosition(3,2) ));
        assertFalse(this.equipmentManager.oarPresentAt( new IntPosition(0,0) ));
    }
    
    @Test
    void testRudderPosition(){
        assertThrows(NullPointerException.class, ()->{
            this.equipmentManager.rudderPosition();
        });
    }
    
    @Test
    void sailsTest() {
    	assertTrue(this.equipmentManager.sails(true).get(0).equals(r8));
    	assertTrue(this.equipmentManager.sails(false).get(0).equals(r7));
    }

}