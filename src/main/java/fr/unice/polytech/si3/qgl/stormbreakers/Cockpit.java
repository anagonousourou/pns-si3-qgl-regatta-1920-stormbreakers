package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Moteur;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.RegattaGoal;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.OutputBuilder;

public class Cockpit implements ICockpit {
	private InitGame gameData;
	private List<Marin> marins;
	private List<Checkpoint> checkpoints;
	private int currentCheckpoint=0;
	

	public void initGame(String game) {
		InputParser parser = new InputParser();
		this.gameData=parser.fetchInitGameState(game);
		if(this.gameData.getGoal().getMode().equals("REGATTA")){
			this.checkpoints=((RegattaGoal)this.gameData.getGoal()).getCheckpoints();
		}
		
		this.marins=gameData.getSailors();
	}

	public String nextRound(String round) {
		OutputBuilder outputBuilder = new OutputBuilder();
		return outputBuilder.writeActions(this.actions());
	}

	@Override
	public List<String> getLogs() {
		return new ArrayList<>();
	}

	List<SailorAction> actions() {
		Moteur shipEngine = new Moteur(gameData.getShip(), marins);
		return shipEngine.sendActions(checkpoints.get(currentCheckpoint));
	}
	
	public int getCurrentCheckpoint() {
		return currentCheckpoint;
	}

	public List<Checkpoint> getCheckpoints() {
		return checkpoints;
	}
	
}
