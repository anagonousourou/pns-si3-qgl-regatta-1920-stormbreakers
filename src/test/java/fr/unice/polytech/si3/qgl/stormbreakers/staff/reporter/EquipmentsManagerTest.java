package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;

public class EquipmentsManagerTest {
    EquipmentsManager equipmentsManager;
    Equipment r1 = new Oar(1, 0);
    Equipment r2 = new Oar(1, 2);
    Equipment r3 = new Oar(2, 0);
    Equipment r4 = new Oar(2, 2);
    Equipment r5 = new Oar(3, 0);
    Equipment r6 = new Oar(3, 2);
    Equipment r7 = new Sail(3, 1);
    Sail r8 = new Sail(3, 0);
    Equipment rudder=new Gouvernail(0, 0);

    @BeforeEach
    void setUp() {

        equipmentsManager = new EquipmentsManager(List.of(r1, r2, r3, r4, r5, r6,r7,r8), 3);
        r8.setOpenned(true);
    }

    @Test
    void testRudderIsPresent() {
        assertFalse(this.equipmentsManager.rudderIsPresent(), "Pas de Gouvernail");

        equipmentsManager = new EquipmentsManager(List.of(r1, r2, r3, r4, r5, r6,r7,r8,rudder), 3);
        assertTrue(this.equipmentsManager.rudderIsPresent(),"Il y a maintenant un gouvernail");
    }

    @Test
    void testAllLeftOars(){
        List<Oar> leftOars= equipmentsManager.allLeftOars();
        assertTrue(leftOars.contains(r1));
        assertTrue(leftOars.contains(r3));
        assertTrue(leftOars.contains(r5));
        assertEquals(3,leftOars.size());
    }
    @Test
    void testAllRightOars(){
        List<Oar> rightOars= equipmentsManager.allRightOars();
        assertTrue(rightOars.contains(r2));
        assertTrue(rightOars.contains(r4));
        assertTrue(rightOars.contains(r6));
        assertEquals(3,rightOars.size());
    }
    
    @Test
    void testNbOars(){
        assertEquals(6,this.equipmentsManager.nbOars(), "6 rames en tout");
    }

    @Test
    void testOarPresentAt(){
        assertTrue(this.equipmentsManager.oarPresentAt( new IntPosition(3,2) ));
        assertFalse(this.equipmentsManager.oarPresentAt( new IntPosition(0,0) ));
    }
    
    @Test
    void testRudderPosition(){
        assertThrows(NullPointerException.class, ()->{
            this.equipmentsManager.rudderPosition();
        });
    }
    
    @Test
    void sailsTest() {
    	assertTrue(this.equipmentsManager.sails(true).get(0).equals(r8));
    	assertTrue(this.equipmentsManager.sails(false).get(0).equals(r7));
    }

    @Test
    void unusedLeftOarsTest() {
        List<Equipment> equipments = new ArrayList<>();
        equipments.addAll(List.of(r1, r2, r3, r4, r5, r6, r7, r8));

        Oar usedLeftOar = new Oar(4,0); usedLeftOar.setUsed(true);
        equipments.add(usedLeftOar);

        EquipmentsManager equipmentManager2 = new EquipmentsManager(equipments, 3);

        List<Oar> unusedLeftOars = equipmentManager2.unusedLeftOars();
        assertEquals(3,unusedLeftOars.size());
        assertFalse(unusedLeftOars.contains(usedLeftOar));
        unusedLeftOars.forEach(oar -> assertFalse(oar.isUsed()));
    }

    @Test
    void unusedRightOarsTest() {
        List<Equipment> equipments = new ArrayList<>();
        equipments.addAll(List.of(r1, r2, r3, r4, r5, r6, r7, r8));

        Oar usedRightOar = new Oar(4,3); usedRightOar.setUsed(true);
        equipments.add(usedRightOar);

        EquipmentsManager equipmentManager2 = new EquipmentsManager(equipments, 3);

        List<Oar> unusedRightOars = equipmentManager2.unusedRightOars();
        assertEquals(3,unusedRightOars.size());
        assertFalse(unusedRightOars.contains(usedRightOar));
        unusedRightOars.forEach(oar -> assertFalse(oar.isUsed()));
    }
}