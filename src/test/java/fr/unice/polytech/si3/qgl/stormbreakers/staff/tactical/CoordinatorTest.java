package fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.UseWatch;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Rudder;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Watch;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CrewManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;

public class CoordinatorTest {
	private Coordinator coordinator;
	private EquipmentsManager equipmentsManager;
	private CrewManager crewManager;
	
	private Sailor m1, m2, m3, m4, m5, m6;
	private Sail s1, s2, s3, s4;
	private Oar o1, o2, o3, o4, o5, o6;
	private Rudder r1, r2;
	private Watch w1, w2;
	
	private List<Equipment> equipments;
	private List<Oar> oars;
	private List<Sail> opennedSails;
	private List<Sail> closedSails;
	private List<Sailor> availableSailors;
	
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
		r1 = new Rudder(2, 3);
		r2 = new Rudder(10, 7);
		w1 = new Watch(1, 0);
		w2 = new Watch(10, 7);
		o1 = new Oar(0, 0);
		o2 = new Oar(0, 2);
		o3 = new Oar(2, 0);
		o4 = new Oar(2, 2);
		o5 = new Oar(4, 0);
		o6 = new Oar(4, 2);
		
		availableSailors = List.of(m1, m2, m3, m4, m5, m6);
		oars = List.of(o1, o2, o3, o4, o5, o6);
		opennedSails = List.of(s1, s3);
		closedSails = List.of(s2, s4);
		equipments = List.of(s1, s2, s3, s4);
		
		equipmentsManager= mock(EquipmentsManager.class);
		crewManager = mock(CrewManager.class);
		coordinator=  new Coordinator(crewManager, equipmentsManager);
		when(crewManager.sailors()).thenReturn(availableSailors);
		when(equipmentsManager.sails(true)).thenReturn(opennedSails);
		when(equipmentsManager.sails(false)).thenReturn(closedSails);
	}
	
	@Test
	void leftSailorsOnOarsTest() {
		when(equipmentsManager.allLeftOars()).thenReturn(List.of(o1,o3,o5));
		when(crewManager.sailorAtPosition(o1.getPosition())).thenReturn(Optional.of(m1));
		assertTrue(coordinator.leftSailorsOnOars().contains(m1) && coordinator.leftSailorsOnOars().size()==1);
	}
	
	@Test
	void rightSailorsOnOarsTest() {
		when(equipmentsManager.allRightOars()).thenReturn(List.of(o2,o4,o6));
		when(crewManager.sailorAtPosition(o6.getPosition())).thenReturn(Optional.of(m5));
        assertEquals(1, coordinator.rightSailorsOnOars().size());
	}
    @Test
    void activateRudderTest() {
    	// Setup sans utiliser Mockito
    	List<Equipment> newVersion = new ArrayList<>(equipments);
    	newVersion.add(r1);
    	coordinator=  new Coordinator(new CrewManager(availableSailors), new EquipmentsManager(newVersion, 4));
    	
    	// Orientaiton valide, au moins 1 marin proche
    	List<SailorAction> toTest1 = coordinator.activateRudder(Math.PI/4);
    	Optional<Sailor> sailorChosen1 = coordinator.sailorForRudder();
    	assertTrue(toTest1.get(0) instanceof MoveAction);
    	int expectedX = sailorChosen1.get().getPosition().x() + ((MoveAction) toTest1.get(0)).getXdistance();
    	int expectedY = sailorChosen1.get().getPosition().y() + ((MoveAction) toTest1.get(0)).getYdistance();
    	assertEquals(r1.x(), expectedX);
    	assertEquals(r1.y(), expectedY);
    	
    	assertTrue(toTest1.get(1) instanceof Turn);
    	assertEquals(Math.PI/4, ((Turn) toTest1.get(1)).getRotation());
    	
    	// Aucun marin proche
    	newVersion.remove(r1);
    	newVersion.add(r2);
    	coordinator =  new Coordinator(new CrewManager(availableSailors), new EquipmentsManager(newVersion, 4));
		List<SailorAction> toTest3 = coordinator.activateRudder(Math.PI/2);
		
    	assertTrue(toTest3.isEmpty());
    }

    @Test
	void activateNbOarsTest() {
    	// Marins dispo, rames présentes
    	coordinator =  new Coordinator(new CrewManager(availableSailors), new EquipmentsManager(new ArrayList<Equipment>(oars), 4));
    	List<SailorAction> toOar1 = coordinator.activateNbOars(oars, 3, new ArrayList<>());
    	assertTrue(toOar1.get(0) instanceof MoveAction);
    	int expectedX = m1.getPosition().x() + ((MoveAction) toOar1.get(0)).getXdistance();
    	int expectedY = m1.getPosition().y() + ((MoveAction) toOar1.get(0)).getYdistance();
    	assertEquals(o1.x(), expectedX);
    	assertEquals(o1.y(), expectedY);

    	assertTrue(toOar1.get(1) instanceof OarAction);
    	assertEquals(0, toOar1.get(0).getSailorId());
    	assertEquals(0, toOar1.get(1).getSailorId());
    	
    	// Marin#0 est indispo
    	m1 = mock(Sailor.class);
    	when(m1.isDoneTurn()).thenReturn(true);
    	availableSailors = List.of(m1, m2, m3, m4, m5, m6);
    	coordinator=  new Coordinator(new CrewManager(availableSailors), new EquipmentsManager(new ArrayList<Equipment>(oars), 4));
    	
    	List<SailorAction> toOar2 = coordinator.activateNbOars(oars, 3, new ArrayList<>());
    	assertEquals(1, toOar2.get(0).getSailorId());
    	assertEquals(1, toOar2.get(1).getSailorId());
    	
    	// Aucun marin dispo
    	List<SailorAction> toOar3 = coordinator.activateNbOars(oars, 3, new ArrayList<>(availableSailors));
    	assertTrue(toOar3.isEmpty());
	}
	
	/**
	 * Vérification que seul des marins proche de la voile soit pris en compte 
	 * + etat de toutes les voiles les même en fct des params
	 *
	 */
	@Test
	void availableSailorsForSailsTest() {
		Map<Equipment, List<Sailor>> results;
		results = coordinator.availableSailorsForSails(true);
		
		assertTrue(results.containsKey(s1));
		assertTrue(results.containsKey(s3));
		assertEquals(List.of(m3, m5, m6), results.get(s1));
		assertEquals(availableSailors, results.get(s3));
		
		results = coordinator.availableSailorsForSails(false);
		assertTrue(results.containsKey(s2));
		assertTrue(results.containsKey(s4));
		assertEquals(List.of(m2, m4, m5), results.get(s2));
		assertEquals(List.of(m1, m2, m3, m4, m5), results.get(s4));
		
		when(crewManager.sailors()).thenReturn(List.of());
		results = coordinator.availableSailorsForSails(false);
		assertEquals(List.of(), results.get(s2));
		assertEquals(List.of(), results.get(s4));
		
		when(equipmentsManager.sails(true)).thenReturn(List.of());
		assertTrue(coordinator.availableSailorsForSails(true).isEmpty());
	}
	
	@Test
	void canLiftAllSailsTest() {
		assertTrue(coordinator.canLiftAllSails());
		when(crewManager.sailors()).thenReturn(List.of(m4));
		assertFalse(coordinator.canLiftAllSails());
		when(crewManager.sailors()).thenReturn(List.of(m4, m5));
		assertTrue(coordinator.canLiftAllSails());
	}

	@Test
	void canLowerAllSailsTest(){
		assertTrue(coordinator.canLowerAllSails());
		when(crewManager.sailors()).thenReturn(List.of());
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

		// LATER: 24/02/2020 MoveAction equals
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
    void bringSailorsCloserToEquipmentsTestAllEquipmentsCovered() {
		crewManager = new CrewManager(List.of()); // required for bringClosestSailorCloserTo
		coordinator=  new Coordinator(crewManager, equipmentsManager);

		Sailor sailor1 = new Sailor(0,3,3);
		Sailor sailor2 = new Sailor(1,5,5);
		Sailor sailor3 = new Sailor(2,1,1);
		List<Sailor> availableSailors = List.of(sailor1,sailor2,sailor3);

		Equipment equipment1 = new Oar(4,4);
		Equipment equipment2 = new Oar(6,6);
		Equipment equipment3 = new Oar(7,7);
		List<Equipment> equipments = List.of(equipment1,equipment2,equipment3);

		List<MoveAction> moves = coordinator.bringSailorsCloserToEquipments(new ArrayList<>(availableSailors),new ArrayList<>(equipments));

		assertEquals(3,moves.size());
    }

	@Test
	void bringSailorsCloserToEquipmentsTestWhenNoEquipments() {
		crewManager = new CrewManager(List.of()); // required for bringClosestSailorCloserTo
		coordinator=  new Coordinator(crewManager, equipmentsManager);

		Sailor sailor1 = new Sailor(0,3,3);
		Sailor sailor2 = new Sailor(1,5,5);
		Sailor sailor3 = new Sailor(2,1,1);
		List<Sailor> availableSailors = List.of(sailor1,sailor2,sailor3);

		List<Equipment> equipments = List.of();

		List<MoveAction> moves = coordinator.bringSailorsCloserToEquipments(new ArrayList<>(availableSailors),new ArrayList<>(equipments));

		assertTrue(moves.isEmpty());
	}

	@Test
	void bringSailorsCloserToEquipmentsTestWhenNoSailors() {
		crewManager = new CrewManager(List.of()); // required for bringClosestSailorCloserTo
		coordinator=  new Coordinator(crewManager, equipmentsManager);

		List<Sailor> availableSailors = List.of();

		Equipment equipment1 = new Oar(4,4);
		Equipment equipment2 = new Oar(6,6);
		Equipment equipment3 = new Oar(7,7);
		List<Equipment> equipments = List.of(equipment1,equipment2,equipment3);

		List<MoveAction> moves = coordinator.bringSailorsCloserToEquipments(new ArrayList<>(availableSailors),new ArrayList<>(equipments));

		assertTrue(moves.isEmpty());
	}

	@Test
	void bringSailorsCloserToEquipmentsTestWhenNotEnoughSailors() {
		crewManager = new CrewManager(List.of()); // required for bringClosestSailorCloserTo
		coordinator=  new Coordinator(crewManager, equipmentsManager);

		Sailor sailor1 = new Sailor(0,3,3);
		Sailor sailor2 = new Sailor(1,5,5);
		List<Sailor> availableSailors = List.of(sailor1,sailor2);

		Equipment equipment1 = new Oar(4,4);
		Equipment equipment2 = new Oar(6,6);
		Equipment equipment3 = new Oar(7,7);
		List<Equipment> equipments = List.of(equipment1,equipment2,equipment3);

		List<MoveAction> moves = coordinator.bringSailorsCloserToEquipments(new ArrayList<>(availableSailors),new ArrayList<>(equipments));

		assertEquals(2,moves.size());
	}

	@Test
	void bringSailorsCloserToEquipmentsTestWhenNotEnoughOars() {
		crewManager = new CrewManager(List.of()); // required for bringClosestSailorCloserTo
		coordinator=  new Coordinator(crewManager, equipmentsManager);

		Sailor sailor1 = new Sailor(0,3,3);
		Sailor sailor2 = new Sailor(1,5,5);
		Sailor sailor3 = new Sailor(2,1,1);
		List<Sailor> availableSailors = List.of(sailor1,sailor2,sailor3);

		Equipment equipment1 = new Oar(4,4);
		Equipment equipment2 = new Oar(6,6);
		List<Equipment> equipments = List.of(equipment1,equipment2);

		List<MoveAction> moves = coordinator.bringSailorsCloserToEquipments(new ArrayList<>(availableSailors),new ArrayList<>(equipments));

		assertEquals(2,moves.size());
	}

	@Test
	void manageUnusedSailorsTestWhenNoAvailableSailor() {
		List<Sailor> sailors = List.of();
		CrewManager crew2 = new CrewManager(sailors);

		Coordinator coordinator2 =  new Coordinator(crew2, equipmentsManager);
		List<SailorAction> moves = coordinator2.manageUnusedSailors();

		assertEquals(0,moves.size());
	}

    @Test
    void manageUnusedSailorsTestWhenNotEnoughSailors() {
        Sailor sailor1 = new Sailor(0,0,0);
        Sailor sailor2 = new Sailor(1,0,0);
        List<Sailor> sailors = List.of(sailor1,sailor2);
        CrewManager crew2 = new CrewManager(sailors);

        // Rudder
        when(equipmentsManager.rudderIsPresent()).thenReturn(true);
        when(equipmentsManager.isRudderUsed()).thenReturn(true);
        IntPosition rudderPos = new IntPosition(5,3);
        when(equipmentsManager.rudderPosition()).thenReturn(rudderPos);

        // Oars
        Oar oarL1 = new Oar(4,0);
        List<Oar> oars = List.of(oarL1);
        when(equipmentsManager.unusedOars()).thenReturn(oars);

        // Sails
        Sail sail1 = new Sail(8,3);
        List<Sail> sails = List.of(sail1);
        when(equipmentsManager.sails()).thenReturn(sails);

        Coordinator coordinator2 =  new Coordinator(crew2, equipmentsManager);
        List<SailorAction> moves = coordinator2.manageUnusedSailors();

        assertEquals(2,moves.size());
    }

	@Test
	void manageUnusedSailorsTestWhenTooManySailors() {
		Sailor sailor1 = new Sailor(0,0,0);
		Sailor sailor2 = new Sailor(1,0,0);
		Sailor sailor3 = new Sailor(2,0,0);
		Sailor sailor4 = new Sailor(3,0,0);
		Sailor sailor5 = new Sailor(4,0,0);
		List<Sailor> sailors = List.of(sailor1,sailor2, sailor3, sailor4,sailor5);
		CrewManager crew2 = new CrewManager(sailors);

		// Rudder
		when(equipmentsManager.rudderIsPresent()).thenReturn(true); // Rudder
		when(equipmentsManager.isRudderUsed()).thenReturn(false);   // Needs someone
		IntPosition rudderPos = new IntPosition(5,3);
		when(equipmentsManager.rudderPosition()).thenReturn(rudderPos);

		// Oars
		Oar oarL1 = new Oar(4,0);
		List<Oar> oars = List.of(oarL1);
		when(equipmentsManager.unusedOars()).thenReturn(oars);

		// Sails
		Sail sail1 = new Sail(8,3);
		List<Sail> sails = List.of(sail1);
		when(equipmentsManager.sails()).thenReturn(sails);

		Coordinator coordinator2 =  new Coordinator(crew2, equipmentsManager);
		List<SailorAction> moves = coordinator2.manageUnusedSailors();

		assertEquals(3,moves.size());
	}

	@Test
	void manageUnusedSailorsTestAllEquipmentsCovered() {
		Sailor sailor1 = new Sailor(0,0,0);
		Sailor sailor2 = new Sailor(1,0,0);
		Sailor sailor3 = new Sailor(2,0,0);
		Sailor sailor4 = new Sailor(3,0,0);
		Sailor sailor5 = new Sailor(4,0,0);
		List<Sailor> sailors = List.of(sailor1,sailor2, sailor3, sailor4,sailor5);
		CrewManager crew2 = new CrewManager(sailors);

		// Rudder
		when(equipmentsManager.rudderIsPresent()).thenReturn(true); // Rudder
		when(equipmentsManager.isRudderUsed()).thenReturn(false);   // Needs someone
		when(equipmentsManager.rudderPosition()).thenReturn(new IntPosition(5,3));

		// Oars
		Oar oarL1 = new Oar(4,0);
		Oar oarR1 = new Oar(4,5);
		List<Oar> oars = List.of(oarL1,oarR1);
		when(equipmentsManager.unusedOars()).thenReturn(oars);

		// Sails
		Sail sail1 = new Sail(5,3);
		Sail sail2 = new Sail(8,3);
		List<Sail> sails = List.of(sail1,sail2);
		when(equipmentsManager.sails()).thenReturn(sails);

		Coordinator coordinator2 =  new Coordinator(crew2, equipmentsManager);
		List<SailorAction> moves = coordinator2.manageUnusedSailors();

		assertEquals(sailors.size(),moves.size());
	}

	@Test
	void manageUnusedSailorsTestWhenNoRudder() {
		Sailor sailor1 = new Sailor(0,0,0);
		Sailor sailor2 = new Sailor(1,0,0);
		Sailor sailor3 = new Sailor(2,0,0);
		Sailor sailor4 = new Sailor(3,0,0);
		Sailor sailor5 = new Sailor(4,0,0);
		List<Sailor> sailors = List.of(sailor1,sailor2, sailor3, sailor4,sailor5);
		CrewManager crew2 = new CrewManager(sailors);

		// Rudder
		when(equipmentsManager.rudderIsPresent()).thenReturn(false);
		when(equipmentsManager.isRudderUsed()).thenReturn(false);

		// Oars
		Oar oarL1 = new Oar(4,0);
		Oar oarR1 = new Oar(4,5);
		List<Oar> oars = List.of(oarL1,oarR1);
		when(equipmentsManager.unusedOars()).thenReturn(oars);

		// Sails
		Sail sail1 = new Sail(5,3);
		Sail sail2 = new Sail(8,3);
		List<Sail> sails = List.of(sail1,sail2);
		when(equipmentsManager.sails()).thenReturn(sails);

		Coordinator coordinator2 =  new Coordinator(crew2, equipmentsManager);
		List<SailorAction> moves = coordinator2.manageUnusedSailors();

		assertEquals(sailors.size()-1,moves.size());
	}

	@Test
	void manageUnusedSailorsTestWhenRudderFree() {
		Sailor sailor1 = new Sailor(0,0,0);
		Sailor sailor2 = new Sailor(1,0,0);
		List<Sailor> sailors = List.of(sailor1,sailor2);
		CrewManager crew2 = new CrewManager(sailors);

		// Rudder
		when(equipmentsManager.rudderIsPresent()).thenReturn(true);
		when(equipmentsManager.isRudderUsed()).thenReturn(false);
		when(equipmentsManager.rudderPosition()).thenReturn(new IntPosition(5,3));

		// No Oars or Sails
		when(equipmentsManager.unusedOars()).thenReturn(List.of());
		when(equipmentsManager.sails()).thenReturn(List.of());

		Coordinator coordinator2 =  new Coordinator(crew2, equipmentsManager);
		List<SailorAction> moves = coordinator2.manageUnusedSailors();

		assertEquals(1,moves.size());
	}

	@Test
	void manageUnusedSailorsTestWhenRudderUsed() {
		Sailor sailor1 = new Sailor(0,0,0);
		Sailor sailor2 = new Sailor(1,0,0);
		List<Sailor> sailors = List.of(sailor1,sailor2);
		CrewManager crew2 = new CrewManager(sailors);

		// Rudder
		when(equipmentsManager.rudderIsPresent()).thenReturn(true);
		when(equipmentsManager.isRudderUsed()).thenReturn(true);
		when(equipmentsManager.rudderPosition()).thenReturn(new IntPosition(5,3));

		// No Oars or Sails
		when(equipmentsManager.unusedOars()).thenReturn(List.of());
		when(equipmentsManager.sails()).thenReturn(List.of());

		Coordinator coordinator2 =  new Coordinator(crew2, equipmentsManager);
		List<SailorAction> moves = coordinator2.manageUnusedSailors();

		assertEquals(0,moves.size());
	}
	
	@Test
	void setSailorToWatchTest() {
		// Setup sans utiliser Mockito
    	List<Equipment> newVersion = new ArrayList<>(equipments);
    	newVersion.add(w1);
    	coordinator =  new Coordinator(new CrewManager(availableSailors), new EquipmentsManager(newVersion, 4));
    	
    	// Orientaiton valide, au moins 1 marin proche
    	Optional<Sailor> sailorForRudder = coordinator.findSailorForWatch();
    	List<SailorAction> toTest1 = coordinator.setSailorToWatch();
    	assertTrue(toTest1.get(0) instanceof MoveAction);
    	
    	int expectedX = sailorForRudder.get().getPosition().x() + ((MoveAction) toTest1.get(0)).getXdistance();
    	int expectedY = sailorForRudder.get().getPosition().y() + ((MoveAction) toTest1.get(0)).getYdistance();
    	assertEquals(w1.x(), expectedX);
    	assertEquals(w1.y(), expectedY);
    	
    	assertTrue(toTest1.get(1) instanceof UseWatch);
    	assertEquals(sailorForRudder.get().getId(), toTest1.get(1).getSailorId());
    	
    	// Watch already used
    	w1.setUsed(true);
    	assertTrue(coordinator.setSailorToWatch().isEmpty());
    	
    	// Aucun marin proche
    	newVersion.remove(w1);
    	newVersion.add(w2);
    	coordinator =  new Coordinator(new CrewManager(availableSailors), new EquipmentsManager(newVersion, 4));
		List<SailorAction> toTest3 = coordinator.setSailorToWatch();
		
    	assertTrue(toTest3.isEmpty());
	}
}
