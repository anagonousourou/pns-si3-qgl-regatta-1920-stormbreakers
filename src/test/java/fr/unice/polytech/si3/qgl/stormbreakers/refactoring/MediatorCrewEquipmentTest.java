package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;

public class MediatorCrewEquipmentTest {
	MediatorCrewEquipment crewEquip;
	EquipmentManager equipmentManager;
	Crew crew;
	List<Sail> voilesOuverte;
	List<Sail> voilesBaissee;
	List<Marine> marinsDisponnible;
	
	@BeforeEach
	void setup(){
		equipmentManager= Mockito.mock(EquipmentManager.class);
		crew = Mockito.mock(Crew.class);
		crewEquip=  new MediatorCrewEquipment(crew, equipmentManager);
		Mockito.when(crew.getAvailableSailors()).thenReturn(marinsDisponnible);
		Mockito.when(equipmentManager.sails(true)).thenReturn(voilesOuverte);;
		Mockito.when(equipmentManager.sails(false)).thenReturn(voilesBaissee);
		//TODO ajouter jeu de données marin et voile
	}
	
	/**
	 * Vérification que seul des marins proche de la voile soit pris en compte 
	 * + etat de toutes les voiles les même en fct des params
	 *
	 */
	@Test
	void marinsDisponnibleVoilesTest() {
		
	}
	
	/*
	 * "vérification" un marin une voile (test avec cas 1 marin unique peut accédé à 2 voiles return false)
	 * test cas simple de true et false 
	 */
	@Test
	void canLiftAllSails() {
		
	}
	
	/**
	 * same as above
	 */
	@Test
	void canLowerAllSails(){
		
	}
}
