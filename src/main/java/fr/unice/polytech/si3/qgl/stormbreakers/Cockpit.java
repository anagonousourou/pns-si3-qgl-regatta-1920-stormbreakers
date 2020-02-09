package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.GameState;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.navire.Captain;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.navire.Moteur;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.OutputBuilder;

public class Cockpit implements ICockpit {
	private Moteur engine;
	private GameState gState;
	private InputParser parser = new InputParser();

	public void initGame(String game) {
		InitGame initGame = this.parser.fetchInitGameState(game);

		Logger.getInstance().log(initGame.toLogs());
		Logger.getInstance().next();

		this.gState = new GameState(initGame);
		this.engine = new Moteur(gState, new Captain());
	}

	public String nextRound(String round) {
		this.gState.updateTurn(this.parser.fetchNextRoundState(round));
		OutputBuilder outputBuilder = new OutputBuilder();

		// Log current checkpoint
		Logger.getInstance().log(" CP-" + gState.getNextCheckpoint().toLogs() + " ");

		List<SailorAction> actions = this.engine.actions();

		// Log actions generated
		List<Logable> logableActions = new ArrayList<>(actions);
		Logger.getInstance().log("A:"+Logable.listToLogs(logableActions,",","[","]"));

		gState.actualiserActions(actions);

		// Save logs and prepare for new instructions
		Logger.getInstance().next();

		return outputBuilder.writeActions(actions);

	}

	@Override
	public List<String> getLogs() {
		return Logger.getInstance().getSavedData();
	}

}
