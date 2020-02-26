package fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CrewManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;

public class CoordinatorTest {
	private Coordinator coordinator;
	private EquipmentsManager equipmentsManager;
	private CrewManager crewManager;
	
	private Sailor m1, m2, m3, m4, m5, m6;
	private Sail s1, s2, s3, s4;
	private Oar o1, o2, o3, o4, o5, o6;
	private Gouvernail r1, r2;
	
	private List<Equipment> equipments;
	private List<Oar> rames;
	private List<Sail> voilesOuvertes;
	private List<Sail> voilesBaissees;
	private List<Sailor> marinsDisponibles;
	
	@BeforeEach
	void setup(){
		s1 = new Sail(7, 4, true);
		s2 = new Sail(2, 5, false);
		s3 = new Sail(2, 3, true);
		s4 = new Sail(1, 0, false);
		m1 = new Sailor(0, 	0, 1);
		m2 = new Sailor(1, 	3, 2);
		m3 = new Sailor(2, 	5, 1);
		m4 = new Sailor(3, 	1, 3);
		m5 = new Sailor(4, 	4, 2);
		m6 = new Sailor(5, 	6, 3);
		r1 = new Gouvernail(2, 3);
		r2 = new Gouvernail(10, 7);
		o1 = new Oar(0, 0);//
		o2 = new Oar(0, 2);//
		o3 = new Oar(2, 0);//
		o4 = new Oar(2, 2);
		o5 = new Oar(4, 0);
		o6 = new Oar(4, 2);
		
		marinsDisponibles = List.of(m1, m2, m3, m4, m5, m6);
		rames = List.of(o1, o2, o3, o4, o5, o6);
		voilesOuvertes = List.of(s1, s3);
		voilesBaissees = List.of(s2, s4);
		equipments = List.of(s1, s2, s3, s4);
		
		equipmentsManager= mock(EquipmentsManager.class);
		crewManager = mock(CrewManager.class);
		coordinator=  new Coordinator(crewManager, equipmentsManager);
		when(crewManager.marins()).thenReturn(marinsDisponibles);
		when(equipmentsManager.sails(true)).thenReturn(voilesOuvertes);
		when(equipmentsManager.sails(false)).thenReturn(voilesBaissees);
	}
	
	@Test
	void leftSailorsOnOarsTest() {
		when(equipmentsManager.allLeftOars()).thenReturn(List.of(o1,o3,o5));
		when(crewManager.marineAtPosition(o1.getPosition())).thenReturn(Optional.of(m1));
		assertTrue(coordinator.leftSailorsOnOars().contains(m1) && coordinator.leftSailorsOnOars().size()==1);
	}
	
	@Test
	void rightSailorsOnOarsTest() {
		when(equipmentsManager.allRightOars()).thenReturn(List.of(o2,o4,o6));
		when(crewManager.marineAtPosition(o6.getPosition())).thenReturn(Optional.of(m5));
		assertTrue(coordinator.rightSailorsOnOars().size()==1);
	}
    @Test
    void activateRudderTest() {
    	// Setup sans utiliser Mockito
    	List<Equipment> newVersion = new ArrayList<>(equipments);
    	newVersion.add(r1);
    	coordinator=  new Coordinator(new CrewManager(marinsDisponibles), new EquipmentsManager(newVersion, 4));
    	
    	// Orientaiton valide, au moins 1 marin proche
    	List<SailorAction> toTest1 = coordinator.activateRudder(Math.PI/4);
    	Optional<Sailor> sailorChosen1 = coordinator.marineForRudder();
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
    	coordinator =  new Coordinator(new CrewManager(marinsDisponibles), new EquipmentsManager(newVersion, 4));
		List<SailorAction> toTest3 = coordinator.activateRudder(Math.PI/2);
		
    	assertTrue(toTest3.isEmpty());
    }

    @Test
	void activateNbOarsTest() {
    	// Marins dispo, rames présentes
    	coordinator =  new Coordinator(new CrewManager(marinsDisponibles), new EquipmentsManager(new ArrayList<Equipment>(rames), 4));
    	List<SailorAction> toOar1 = coordinator.activateNbOars(rames, 3, new ArrayList<>());
    	assertTrue(toOar1.get(0) instanceof MoveAction);
    	int expectedX = m1.getPosition().getX() + ((MoveAction) toOar1.get(0)).getXdistance();
    	int expectedY = m1.getPosition().getY() + ((MoveAction) toOar1.get(0)).getYdistance();
    	assertEquals(o1.getX(), expectedX);
    	assertEquals(o1.getY(), expectedY);

    	assertTrue(toOar1.get(1) instanceof OarAction);
    	assertEquals(0, toOar1.get(0).getSailorId());
    	assertEquals(0, toOar1.get(1).getSailorId());
    	
    	// Marin#0 est indispo
    	m1 = mock(Sailor.class);
    	when(m1.isDoneTurn()).thenReturn(true);
    	marinsDisponibles = List.of(m1, m2, m3, m4, m5, m6);
    	coordinator=  new Coordinator(new CrewManager(marinsDisponibles), new EquipmentsManager(new ArrayList<Equipment>(rames), 4));
    	
    	List<SailorAction> toOar2 = coordinator.activateNbOars(rames, 3, new ArrayList<>());
    	assertEquals(1, toOar2.get(0).getSailorId());
    	assertEquals(1, toOar2.get(1).getSailorId());
    	
    	// Aucun marin dispo
    	List<SailorAction> toOar3 = coordinator.activateNbOars(rames, 3, new ArrayList<>(marinsDisponibles));
    	assertTrue(toOar3.isEmpty());
	}
	
	/**
	 * Vérification que seul des marins proche de la voile soit pris en compte 
	 * + etat de toutes les voiles les même en fct des params
	 *
	 */
	@Test
	void marinsDisponiblesVoilesTest() {
		Map<Equipment, List<Sailor>> results;
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
		
		when(crewManager.marins()).thenReturn(List.of());
		results = coordinator.marinsDisponiblesVoiles(false);
		assertEquals(List.of(), results.get(s2));
		assertEquals(List.of(), results.get(s4));
		
		when(equipmentsManager.sails(true)).thenReturn(List.of());;
		assertTrue(coordinator.marinsDisponiblesVoiles(true).isEmpty());
	}
	
	@Test
	void canLiftAllSailsTest() {
		assertTrue(coordinator.canLiftAllSails());
		when(crewManager.marins()).thenReturn(List.of(m4));
		assertFalse(coordinator.canLiftAllSails());
		when(crewManager.marins()).thenReturn(List.of(m4, m5));
		assertTrue(coordinator.canLiftAllSails());
	}

	@Test
	void canLowerAllSailsTest(){
		assertTrue(coordinator.canLowerAllSails());
		when(crewManager.marins()).thenReturn(List.of());
		assertFalse(coordinator.canLowerAllSails());
	}

	@Test
	void addOaringSailorsOnEachSideTestWhenNoOarsOnLeftSide() {
		List<Oar> leftOars = List.of();
		when(equipmentsManager.unusedLeftOars()).thenReturn(leftOars);
		List<Oar> rightOars = new ArrayList<>();
		rightOars.add(new Oar(0,3));
		when(equipmentsManager.unusedRightOars()).thenReturn(rightOars);

		assertTrue(coordinator.addOaringSailorsOnEachSide().isEmpty());
	}

	@Test
	void addOaringSailorsOnEachSideTestWhenNoOarsOnRightSide() {
		List<Oar> leftOars = new ArrayList<>();
		leftOars.add(new Oar(0,0));
		when(equipmentsManager.unusedLeftOars()).thenReturn(leftOars);
		List<Oar> rightOars = List.of();
		when(equipmentsManager.unusedRightOars()).thenReturn(rightOars);

		assertTrue(coordinator.addOaringSailorsOnEachSide().isEmpty());
	}

	@Test
	void addOaringSailorsOnEachSideTestWhenFirstOarsReachable() {
		Sailor sailorGoingLeft = new Sailor(0,4,1);
		Sailor sailorGoingRight = new Sailor(1,4,2);
		// Not assigned because too far
		Sailor sailorNotGoingLeft = new Sailor(2,20,1);
		Sailor sailorNotGoingRight = new Sailor(3,20,2);
		CrewManager crew2 = new CrewManager(List.of(sailorGoingLeft,sailorGoingRight, sailorNotGoingLeft, sailorNotGoingRight));

		Coordinator coordinator2 =  new Coordinator(crew2, equipmentsManager);

		Oar oarL1 = new Oar(0,0);
		Oar oarL2 = new Oar(2,0);
		List<Oar> leftOars = List.of(oarL1, oarL2);

		Oar oarR1 = new Oar(0,3);
		Oar oarR2 = new Oar(2,3);
		List<Oar> rightOars = List.of(oarR1,oarR2);

		when(equipmentsManager.unusedLeftOars()).thenReturn(leftOars);
		when(equipmentsManager.unusedRightOars()).thenReturn(rightOars);

		List<SailorAction> resultActions = coordinator2.addOaringSailorsOnEachSide();

		assertEquals(4,resultActions.size());
		MoveAction move1 = (MoveAction) resultActions.get(0);
		assertAll(
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
		Sailor sailorGoingLeft = new Sailor(1,12,0);
		Sailor sailorGoingRight = new Sailor(3,12,3);
		// Not assigned because too far
		Sailor sailorNotGoingLeft = new Sailor(0,20,0);
		Sailor sailorNotGoingRight = new Sailor(2,20,3);

		CrewManager crew2 = new CrewManager(List.of(sailorNotGoingLeft,sailorGoingLeft,sailorNotGoingRight,sailorGoingRight));

		Coordinator coordinator2 =  new Coordinator(crew2, equipmentsManager);


		Oar oarL1 = new Oar(0,0);
		Oar oarL2 = new Oar(10,0);
		List<Oar> leftOars = List.of(oarL1,oarL2);

		Oar oarR1 = new Oar(0,3);
		Oar oarR2 = new Oar(10,3);
		List<Oar> rightOars =  List.of(oarR1,oarR2);

		when(equipmentsManager.unusedLeftOars()).thenReturn(leftOars);
		when(equipmentsManager.unusedRightOars()).thenReturn(rightOars);

		List<SailorAction> resultActions = coordinator2.addOaringSailorsOnEachSide();
		assertEquals(4,resultActions.size());

		MoveAction move1 = (MoveAction) resultActions.get(0);
		assertAll(
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


    @Test
    void bringSailorsCloserToOarsTestAllOarsCovered() {
		Sailor sailor1 = new Sailor(0,3,3);
		Sailor sailor2 = new Sailor(1,5,5);
		Sailor sailor3 = new Sailor(2,1,1);
		List<Sailor> availableSailors = List.of(sailor1,sailor2,sailor3);

		Oar oar1 = new Oar(4,4);
		Oar oar2 = new Oar(6,6);
		Oar oar3 = new Oar(7,7);
		List<Oar> unusedOars = List.of(oar1,oar2,oar3);

		List<MoveAction> moves = coordinator.bringSailorsCloserToOars(new ArrayList<>(availableSailors),unusedOars);

		assertEquals(3,moves.size());
    }

	@Test
	void bringSailorsCloserToOarsTestWhenNoOars() {
		Sailor sailor1 = new Sailor(0,3,3);
		Sailor sailor2 = new Sailor(1,5,5);
		Sailor sailor3 = new Sailor(2,1,1);
		List<Sailor> availableSailors = List.of(sailor1,sailor2,sailor3);

		List<Oar> unusedOars = List.of();

		List<MoveAction> moves = coordinator.bringSailorsCloserToOars(new ArrayList<>(availableSailors),unusedOars);

		assertTrue(moves.isEmpty());
	}

	@Test
	void bringSailorsCloserToOarsTestWhenNoSailors() {
		List<Sailor> availableSailors = List.of();

		Oar oar1 = new Oar(4,4);
		Oar oar2 = new Oar(6,6);
		Oar oar3 = new Oar(7,7);
		List<Oar> unusedOars = List.of(oar1,oar2,oar3);

		List<MoveAction> moves = coordinator.bringSailorsCloserToOars(new ArrayList<>(availableSailors),unusedOars);

		assertTrue(moves.isEmpty());
	}

	@Test
	void bringSailorsCloserToOarsTestWhenNotEnoughSailors() {
		Sailor sailor1 = new Sailor(0,3,3);
		Sailor sailor2 = new Sailor(1,5,5);
		List<Sailor> availableSailors = List.of(sailor1,sailor2);

		Oar oar1 = new Oar(4,4);
		Oar oar2 = new Oar(6,6);
		Oar oar3 = new Oar(7,7);
		List<Oar> unusedOars = List.of(oar1,oar2,oar3);

		List<MoveAction> moves = coordinator.bringSailorsCloserToOars(new ArrayList<>(availableSailors),unusedOars);

		assertEquals(2,moves.size());
	}

	@Test
	void bringSailorsCloserToOarsTestWhenNotEnoughOars() {
		Sailor sailor1 = new Sailor(0,3,3);
		Sailor sailor2 = new Sailor(1,5,5);
		Sailor sailor3 = new Sailor(2,1,1);
		List<Sailor> availableSailors = List.of(sailor1,sailor2,sailor3);

		Oar oar1 = new Oar(4,4);
		Oar oar2 = new Oar(6,6);
		List<Oar> unusedOars = List.of(oar1,oar2);

		List<MoveAction> moves = coordinator.bringSailorsCloserToOars(new ArrayList<>(availableSailors),unusedOars);

		assertEquals(2,moves.size());
	}
}
