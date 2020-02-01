package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.GameState;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Moteur;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.OutputBuilder;

public class Cockpit implements ICockpit {
	private Moteur engine;
	private GameState gState;
	private InputParser parser=new InputParser();


	public void initGame(String game) {
		this.gState=new GameState(this.parser.fetchInitGameState(game));
		this.engine = new Moteur(gState);
	}

	public String nextRound(String round) {
		
		this.gState.updateTurn(this.parser.fetchNextRoundState(round));

		
		OutputBuilder outputBuilder = new OutputBuilder();
		return outputBuilder.writeActions(this.engine.actions());
	}

	@Override
	public List<String> getLogs() {
		return new ArrayList<>();
	}

}
