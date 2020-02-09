package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.OutputBuilder;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.Captain;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.CheckpointManager;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.Compas;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.Crew;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.EquipmentManager;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.InputParser;

public class Cockpit implements ICockpit {
	private InputParser parser = new InputParser();
	private OutputBuilder outputBuilder = new OutputBuilder();
	private Crew crew = null;
	private EquipmentManager equipmentManager = null;
	private Captain captain = null;
	private Boat boat = null;
	private CheckpointManager checkpointManager = null;
	private Compas compas = new Compas();

	public void initGame(String game) {

		try {
			crew = new Crew(this.parser.fetchAllSailors(game));
			equipmentManager = new EquipmentManager(parser.fetchEquipments(game), parser.fetchWidth(game));
			boat = this.parser.fetchBoat(game);
			checkpointManager = new CheckpointManager(parser.fetchCheckpoints(game));
			captain = new Captain(boat, checkpointManager, equipmentManager, crew, compas);

		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String nextRound(String round) {
		// TODO Phase d'updates


		//Phase de calcul des actions
		List<SailorAction> actions=this.captain.nextRoundActions();
		//Appliquer les actions
		this.crew.executeMovingsInSailorAction(actions);
		
		

		// TODO Log current checkpoint
		

		// Log actions generated
		List<Logable> logableActions = new ArrayList<>(actions);
		Logger.getInstance().log("A:" + Logable.listToLogs(logableActions, ",", "[", "]"));

		

		// Save logs and prepare for new instructions
		Logger.getInstance().next();

		return outputBuilder.writeActions(actions);

	}

	@Override
	public List<String> getLogs() {
		return Logger.getInstance().getSavedData();
	}

}
