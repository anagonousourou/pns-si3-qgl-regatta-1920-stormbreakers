package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CheckpointsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CrewManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Captain;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Coordinator;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Navigator;

public class ElementsConstructor {
    private InputParser parser = new InputParser();
	private OutputBuilder outputBuilder = new OutputBuilder();
	private CrewManager crewManager = null;
	private EquipmentsManager equipmentsManager = null;
	private Captain captain = null;
	private Boat boat = null;
	private CheckpointsManager checkpointsManager = null;
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
			equipmentsManager = new EquipmentsManager(parser.fetchEquipments(game), parser.fetchBoatWidth(game), parser);
			//
			checkpointsManager = new CheckpointsManager(parser.fetchCheckpoints(game));

			int life = this.parser.fetchBoatLife(game);
			int decklength = this.parser.fetchBoatLength(game);
			int deckwidth = this.parser.fetchBoatWidth(game);
			Position position = this.parser.fetchBoatPosition(game);
			boat = new Boat(position, decklength, deckwidth, life, parser);

			coordinator = new Coordinator(crewManager, equipmentsManager);
			seaElements = new WeatherAnalyst(wind, boat, equipmentsManager);

			captain = new Captain(boat, checkpointsManager, navigator, seaElements,
					coordinator);

			this.observableData.addPropertyChangeListener(this.wind);
			this.observableData.addPropertyChangeListener(this.equipmentsManager);
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