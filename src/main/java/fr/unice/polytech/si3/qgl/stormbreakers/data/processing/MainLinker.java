package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;
import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.ParsingException;
import fr.unice.polytech.si3.qgl.stormbreakers.io.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.io.json.JsonInputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.io.json.JsonOutputBuilder;
import fr.unice.polytech.si3.qgl.stormbreakers.math.graph.Graph;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.Cartographer;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CheckpointsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CrewManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.StreamManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.TargetDefiner;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.WeatherAnalyst;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Captain;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Coordinator;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical.Navigator;

public class MainLinker {
	private InputParser parser = new JsonInputParser();
	private JsonOutputBuilder outputBuilder = new JsonOutputBuilder();
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
	private TargetDefiner targetDefiner;
	private StreamManager streamManager;
	private Graph graph;
	private Cartographer cartographer;

	public MainLinker(String game) {
		try {
			wind = new Wind(parser);// son état est modifié au gré des tours
			crewManager = new CrewManager(this.parser.fetchAllSailors(game));// construit une fois pour toute
			// son état varie en fonction des rounds à cause des sails
			equipmentsManager = new EquipmentsManager(parser.fetchEquipments(game), parser.fetchBoatWidth(game),
					parser);
			//
			checkpointsManager = new CheckpointsManager(parser.fetchCheckpoints(game));

			int life = this.parser.fetchBoatLife(game);
			int decklength = this.parser.fetchBoatLength(game);
			int deckwidth = this.parser.fetchBoatWidth(game);
			Position position = this.parser.fetchBoatPosition(game);
			Shape boatShape = this.parser.fetchBoatShape(game);
			boat = new Boat(position, decklength, deckwidth, life, parser, boatShape);

			coordinator = new Coordinator(crewManager, equipmentsManager);
			seaElements = new WeatherAnalyst(wind, boat, equipmentsManager);

			streamManager = new StreamManager(parser, boat);

			graph = new Graph(streamManager, seaElements);

			cartographer = new Cartographer(checkpointsManager, graph, boat);

			targetDefiner = new TargetDefiner(checkpointsManager, streamManager, boat, navigator, cartographer);

			captain = new Captain(boat, checkpointsManager, navigator, seaElements, coordinator, targetDefiner);

			this.observableData.addPropertyChangeListener(this.wind);
			this.observableData.addPropertyChangeListener(this.equipmentsManager);
			this.observableData.addPropertyChangeListener(this.boat);
			this.observableData.addPropertyChangeListener(this.streamManager);

		} catch (ParsingException e) {
			Logger.getInstance().logErrorMsg(e);
		}
	}

	public String sendActions(String round) {

		// Update informations
		this.observableData.setValue(round);

		return outputBuilder.writeActions(this.captain.nextRoundActions());
	}

}