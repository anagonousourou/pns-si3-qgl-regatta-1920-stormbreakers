package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.OutputBuilder;

public class ElementsConstructor {
    private InputParser parser = new InputParser();
	private OutputBuilder outputBuilder = new OutputBuilder();
	private CrewManager crewManager = null;
	private EquipmentManager equipmentManager = null;
	private Captain captain = null;
	private Boat boat = null;
	private CheckpointManager checkpointManager = null;
	private Navigator navigator = new Navigator();
	private WeatherAnalyst seaElements;
	private Wind wind;
	private Coordinator coordinator;
	private ObservableData observableData = new ObservableData();
    
    public ElementsConstructor(String game){
        try {
			wind = new Wind(parser);// son état est modifié au gré des tours
			crewManager = new CrewManager(this.parser.fetchAllSailors(game));// construit une fois pour toute
			// son état varie en fonction des rounds à cause des sails
			equipmentManager = new EquipmentManager(parser.fetchEquipments(game), parser.fetchBoatWidth(game), parser);
			//
			checkpointManager = new CheckpointManager(parser.fetchCheckpoints(game));

			int life = this.parser.fetchBoatLife(game);
			int decklength = this.parser.fetchBoatLength(game);
			int deckwidth = this.parser.fetchBoatWidth(game);
			Position position = this.parser.fetchBoatPosition(game);
			boat = new Boat(position, decklength, deckwidth, life, parser);

			coordinator = new Coordinator(crewManager, equipmentManager);
			seaElements = new WeatherAnalyst(wind, boat, equipmentManager);

			captain = new Captain(boat, checkpointManager, navigator, seaElements,
					coordinator);

			this.observableData.addPropertyChangeListener(this.wind);
			this.observableData.addPropertyChangeListener(this.equipmentManager);
			this.observableData.addPropertyChangeListener(this.boat);

		} catch (JsonProcessingException e) {
			Logger.getInstance().log(e.getMessage());
		}
    }


    public String sendActions(String round){

        // Update informations
		this.observableData.setValue(round);

		// Phase de calcul des actions
		List<SailorAction> actions = this.captain.nextRoundActions();

		//Log current checkpoint

		// Log actions generated
		List<Logable> logableActions = new ArrayList<>(actions);
		Logger.getInstance().log("A:" + Logable.listToLogs(logableActions, ",", "[", "]"));

		// Save logs and prepare for new instructions
		
		return outputBuilder.writeActions(actions);
    }

    
}