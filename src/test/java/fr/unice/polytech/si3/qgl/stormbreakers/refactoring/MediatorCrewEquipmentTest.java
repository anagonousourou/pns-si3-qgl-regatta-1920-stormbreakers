package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;

public class MediatorCrewEquipmentTest {
	MediatorCrewEquipment coordinator;
	EquipmentManager equipmentManager;
	Crew crew;
	
	Sail s1, s2, s3, s4;
	Marine m1, m2, m3, m4, m5, m6;
	Gouvernail r1, r2;
	
	List<Equipment> equipments;
	List<Sail> voilesOuvertes;
	List<Sail> voilesBaissees;
	List<Marine> marinsDisponibles;
	
	@BeforeEach
	void setup(){
		s1 = new Sail(7, 4, true);
		s2 = new Sail(2, 5, false);
		s3 = new Sail(2, 3, true);
		s4 = new Sail(1, 0, false);
		m1 = new Marine(0, 0, 1);
		m2 = new Marine(1, 3, 2);
		m3 = new Marine(2, 5, 1);
		m4 = new Marine(3, 1, 3);
		m5 = new Marine(4, 4, 2);
		m6 = new Marine(5, 6, 3);
		r1 = new Gouvernail(2, 3);
		r2 = new Gouvernail(10, 7);		
		
		marinsDisponibles = List.of(m1, m2, m3, m4, m5, m6);
		voilesOuvertes = List.of(s1, s3);
		voilesBaissees = List.of(s2, s4);
		equipments = List.of(s1, s2, s3, s4);
		
		equipmentManager= Mockito.mock(EquipmentManager.class);
		crew = Mockito.mock(Crew.class);
		coordinator=  new MediatorCrewEquipment(crew, equipmentManager);
		Mockito.when(crew.getAvailableSailors()).thenReturn(marinsDisponibles);
		Mockito.when(equipmentManager.sails(true)).thenReturn(voilesOuvertes);;
		Mockito.when(equipmentManager.sails(false)).thenReturn(voilesBaissees);
	}

	
    @Test
    void activateRudderTest() {
    	// Setup sans utiliser Mockito
    	List<Equipment> newVersion = new ArrayList<>(equipments);
    	newVersion.add(r1);
    	coordinator=  new MediatorCrewEquipment(new Crew(marinsDisponibles), new EquipmentManager(newVersion, 4));
    	
    	// Orientaiton valide, au moins 1 marin proche
    	List<SailorAction> toTest1 = coordinator.activateRudder(Math.PI/4);
    	Optional<Marine> sailorChosen1 = coordinator.marineForRudder();
    	assertTrue(toTest1.get(0) instanceof MoveAction);
    	int expectedX = sailorChosen1.get().getPosition().getX() + ((MoveAction) toTest1.get(0)).getXdistance();
    	int expectedY = sailorChosen1.get().getPosition().getY() + ((MoveAction) toTest1.get(0)).getYdistance();
    	assertEquals(r1.getX(), expectedX);
    	assertEquals(r1.getY(), expectedY);
    	
    	assertTrue(toTest1.get(1) instanceof Turn);
    	assertEquals(Math.PI/4, ((Turn) toTest1.get(1)).getRotation());
    	
    	// Aucun marin proche
    	newVersion.remove(r1);
    	newVersion.add(r2);
    	coordinator=  new MediatorCrewEquipment(new Crew(marinsDisponibles), new EquipmentManager(newVersion, 4));
    	List<SailorAction> toTest3 = coordinator.activateRudder(Math.PI/2);
    	assertTrue(toTest3.isEmpty());
    }
    
    @Test
	void activateNbOarsTest() {
		
	}
	
	/**
	 * Vérification que seul des marins proche de la voile soit pris en compte 
	 * + etat de toutes les voiles les même en fct des params
	 *
	 */
	@Test
	void marinsDisponnibleVoilesTest() {
		Map<Equipment, List<Marine>> results;
		results = coordinator.marinsDisponiblesVoiles(true);
		assertTrue(results.keySet().contains(s1));
		assertTrue(results.keySet().contains(s3));
		assertEquals(List.of(m3, m5, m6), results.get(s1));
		assertEquals(marinsDisponibles, results.get(s3));
		
		results = coordinator.marinsDisponiblesVoiles(false);
		assertTrue(results.keySet().contains(s2));
		assertTrue(results.keySet().contains(s4));
		assertEquals(List.of(m2, m4, m5), results.get(s2));
		assertEquals(List.of(m1, m2, m3, m4, m5), results.get(s4));
		
		Mockito.when(crew.getAvailableSailors()).thenReturn(List.of());
		results = coordinator.marinsDisponiblesVoiles(false);
		assertEquals(List.of(), results.get(s2));
		assertEquals(List.of(), results.get(s4));
		
		Mockito.when(equipmentManager.sails(true)).thenReturn(List.of());;
		assertTrue(coordinator.marinsDisponiblesVoiles(true).isEmpty());
	}
	
	/*
	 * "vérification" un marin une voile (test avec cas 1 marin unique peut accédé à 2 voiles return false)
	 * test cas simple de true et false 
	 */
	void canLiftAllSails() {
		
	}
	
	/**
	 * same as above
	 */
	void canLowerAllSails(){
		
	}

	@Test
	void addOaringSailorsOnEachSideTestWhenNoOarsOnLeftSide() {
		List<Oar> leftOars = List.of();
		Mockito.when(equipmentManager.unusedLeftOars()).thenReturn(leftOars);
		List<Oar> rightOars = new ArrayList<>();
		rightOars.add(new Oar(0,3));
		Mockito.when(equipmentManager.unusedRightOars()).thenReturn(rightOars);

		assertTrue(coordinator.addOaringSailorsOnEachSide().isEmpty());
	}

	@Test
	void addOaringSailorsOnEachSideTestWhenNoOarsOnRightSide() {
		List<Oar> leftOars = new ArrayList<>();
		leftOars.add(new Oar(0,0));
		Mockito.when(equipmentManager.unusedLeftOars()).thenReturn(leftOars);
		List<Oar> rightOars = List.of();
		Mockito.when(equipmentManager.unusedRightOars()).thenReturn(rightOars);

		assertTrue(coordinator.addOaringSailorsOnEachSide().isEmpty());
	}

	@Test
	void addOaringSailorsOnEachSideTestWhenFirstOarsReachable() {
		Marine sailorGoingLeft = new Marine(0,4,1);
		Marine sailorGoingRight = new Marine(1,4,2);
		// Not assigned because too far
		Marine sailorNotGoingLeft = new Marine(2,20,1);
		Marine sailorNotGoingRight = new Marine(3,20,2);
		Crew crew2 = new Crew(List.of(sailorGoingLeft,sailorGoingRight, sailorNotGoingLeft, sailorNotGoingRight));

		MediatorCrewEquipment coordinator2 =  new MediatorCrewEquipment(crew2, equipmentManager);

		Oar oarL1 = new Oar(0,0);
		Oar oarL2 = new Oar(2,0);
		List<Oar> leftOars = List.of(oarL1, oarL2);

		Oar oarR1 = new Oar(0,3);
		Oar oarR2 = new Oar(2,3);
		List<Oar> rightOars = List.of(oarR1,oarR2);

		Mockito.when(equipmentManager.unusedLeftOars()).thenReturn(leftOars);
		Mockito.when(equipmentManager.unusedRightOars()).thenReturn(rightOars);

		List<SailorAction> resultActions = coordinator2.addOaringSailorsOnEachSide();

		assertEquals(4,resultActions.size());
		MoveAction move1 = (MoveAction) resultActions.get(0);
		assertAll(
				// TODO: 15/02/2020 MoveAction equals
				() -> assertEquals(sailorGoingLeft.getId(),move1.getSailorId()),
				() -> assertEquals(-4, move1.getXdistance()),
				() -> assertEquals(-1, move1.getYdistance())
		);
		MoveAction move2 = (MoveAction) resultActions.get(2);
		assertAll(
				() -> assertEquals(sailorGoingRight.getId(),move2.getSailorId()),
				() -> assertEquals(-4, move2.getXdistance()),
				() -> assertEquals(1, move2.getYdistance())
		);
	}

	@Test
	void addOaringSailorsOnEachSideTestWhenOnlyLastOarsReachable() {
		Marine sailorGoingLeft = new Marine(1,12,0);
		Marine sailorGoingRight = new Marine(3,12,3);
		// Not assigned because too far
		Marine sailorNotGoingLeft = new Marine(0,20,0);
		Marine sailorNotGoingRight = new Marine(2,20,3);

		Crew crew2 = new Crew(List.of(sailorNotGoingLeft,sailorGoingLeft,sailorNotGoingRight,sailorGoingRight));

		MediatorCrewEquipment coordinator2 =  new MediatorCrewEquipment(crew2, equipmentManager);


		Oar oarL1 = new Oar(0,0);
		Oar oarL2 = new Oar(10,0);
		List<Oar> leftOars = List.of(oarL1,oarL2);

		Oar oarR1 = new Oar(0,3);
		Oar oarR2 = new Oar(10,3);
		List<Oar> rightOars =  List.of(oarR1,oarR2);

		Mockito.when(equipmentManager.unusedLeftOars()).thenReturn(leftOars);
		Mockito.when(equipmentManager.unusedRightOars()).thenReturn(rightOars);

		List<SailorAction> resultActions = coordinator2.addOaringSailorsOnEachSide();
		assertEquals(4,resultActions.size());

		MoveAction move1 = (MoveAction) resultActions.get(0);
		assertAll(
				// TODO: 15/02/2020 MoveAction equals
				() -> assertEquals(sailorGoingLeft.getId(),move1.getSailorId()),
				() -> assertEquals(-2, move1.getXdistance()),
				() -> assertEquals(0, move1.getYdistance())
		);

		MoveAction move2 = (MoveAction) resultActions.get(2);
		assertAll(
				() -> assertEquals(sailorGoingRight.getId(),move2.getSailorId()),
				() -> assertEquals(-2, move2.getXdistance()),
				() -> assertEquals(0, move2.getYdistance())
		);

	}


}
