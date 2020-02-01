package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.GameState;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.NextRound;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Moteur;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.OutputBuilder;

public class Cockpit implements ICockpit {
	private GameState gameData;
	

	public void initGame(String game) {
		InputParser parser = new InputParser();
		InitGame initGame = parser.fetchInitGameState(game);
		this.gameData = new GameState(initGame);

	}

	public String nextRound(String round) {
		InputParser parser = new InputParser();
		NextRound nextRound = parser.fetchNextRoundState(round);
		gameData.actualiserTour(nextRound);
		gameData.actualiserCheckpoints();

		OutputBuilder outputBuilder = new OutputBuilder();

		List<SailorAction> actions = this.actions();
		gameData.actualiserActions(actions);
		return outputBuilder.writeActions(actions);
	}

	@Override
	public List<String> getLogs() {
		return new ArrayList<>();
	}

	List<SailorAction> actions() {
		Moteur shipEngine = new Moteur(gameData.getShip(), gameData.getOrgaMarins());
		return shipEngine.sendActions(gameData.getNextCheckpoint());
	}
	
}
