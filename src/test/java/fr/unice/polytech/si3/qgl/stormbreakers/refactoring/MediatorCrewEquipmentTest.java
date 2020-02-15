package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;

public class MediatorCrewEquipmentTest {
	MediatorCrewEquipment coordinator;
	EquipmentManager equipmentManager;
	Crew crew;
	
	Sail s1, s2, s3, s4;
	Marine m1, m2, m3, m4, m5, m6;
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
		
		marinsDisponibles = List.of(m1, m2, m3, m4, m5, m6);
		voilesOuvertes = List.of(s1, s3);
		voilesBaissees = List.of(s2, s4);
		
		equipmentManager= Mockito.mock(EquipmentManager.class);
		crew = Mockito.mock(Crew.class);
		coordinator=  new MediatorCrewEquipment(crew, equipmentManager);
		Mockito.when(crew.getAvailableSailors()).thenReturn(marinsDisponibles);
		Mockito.when(equipmentManager.sails(true)).thenReturn(voilesOuvertes);;
		Mockito.when(equipmentManager.sails(false)).thenReturn(voilesBaissees);
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
}
