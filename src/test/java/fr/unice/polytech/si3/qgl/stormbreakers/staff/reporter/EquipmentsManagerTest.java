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
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Rudder;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Watch;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;

public class EquipmentsManagerTest {
    EquipmentsManager equipmentsManager;
    Equipment o1 = new Oar(1, 0);
    Equipment o2 = new Oar(1, 2);
    Equipment o3 = new Oar(2, 0);
    Equipment o4 = new Oar(2, 2);
    Equipment o5 = new Oar(3, 0);
    Equipment o6 = new Oar(3, 2);
    Equipment o7 = new Sail(3, 1);
    Sail s1 = new Sail(3, 0);
    Equipment rudder=new Rudder(0, 0);
    Watch watch = new Watch(0, 1);

    @BeforeEach
    void setUp() {

        equipmentsManager = new EquipmentsManager(List.of(o1, o2, o3, o4, o5, o6,o7,s1), 3);
        s1.setOpenned(true);
    }

    @Test
    void testRudderIsPresent() {
        assertFalse(this.equipmentsManager.rudderIsPresent(), "Pas de Gouvernail");

        equipmentsManager = new EquipmentsManager(List.of(o1, o2, o3, o4, o5, o6,o7,s1,rudder), 3);
        assertTrue(this.equipmentsManager.rudderIsPresent(),"Il y a maintenant un gouvernail");
    }
    
    @Test
    void testWatchIsPresent() {
        assertFalse(this.equipmentsManager.watchIsPresent(), "Pas de vigie");

        equipmentsManager = new EquipmentsManager(List.of(o1, o2, o3, o4, o5, o6,o7,s1, watch), 3);
        assertTrue(this.equipmentsManager.watchIsPresent(),"Il y a maintenant la vigie.");
    }

    @Test
    void testAllLeftOars(){
        List<Oar> leftOars= equipmentsManager.allLeftOars();
        assertTrue(leftOars.contains(o1));
        assertTrue(leftOars.contains(o3));
        assertTrue(leftOars.contains(o5));
        assertEquals(3,leftOars.size());
    }
    @Test
    void testAllRightOars(){
        List<Oar> rightOars= equipmentsManager.allRightOars();
        assertTrue(rightOars.contains(o2));
        assertTrue(rightOars.contains(o4));
        assertTrue(rightOars.contains(o6));
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
    void testWatchPosition(){
        assertThrows(NullPointerException.class, ()->{
            this.equipmentsManager.watchPosition();
        });
        equipmentsManager = new EquipmentsManager(List.of(o1, o2, o3, o4, o5, o6,o7,s1, watch), 3);
        assertEquals(new IntPosition(0, 1), this.equipmentsManager.watchPosition());
    }
    
    @Test
    void sailsTest() {
    	assertTrue(this.equipmentsManager.sails(true).get(0).equals(s1));
    	assertTrue(this.equipmentsManager.sails(false).get(0).equals(o7));
    }

    @Test
    void unusedLeftOarsTest() {
        List<Equipment> equipments = new ArrayList<>();
        equipments.addAll(List.of(o1, o2, o3, o4, o5, o6, o7, s1));

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
        equipments.addAll(List.of(o1, o2, o3, o4, o5, o6, o7, s1));

        Oar usedRightOar = new Oar(4,3); usedRightOar.setUsed(true);
        equipments.add(usedRightOar);

        EquipmentsManager equipmentManager2 = new EquipmentsManager(equipments, 3);

        List<Oar> unusedRightOars = equipmentManager2.unusedRightOars();
        assertEquals(3,unusedRightOars.size());
        assertFalse(unusedRightOars.contains(usedRightOar));
        unusedRightOars.forEach(oar -> assertFalse(oar.isUsed()));
    }
}