package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.OutputBuilder;

public class Cockpit implements ICockpit {
	private InitGame gameData;
	private List<Marin> marins;
	public void initGame(String game) {
		InputParser parser = new InputParser();
		this.gameData=parser.fetchInitGameState(game);
		this.marins=gameData.getSailors();
		
	}

	public String nextRound(String round) {
		System.out.println("Next round input: " + round);

		// TODO: 19/01/2020 Creer action a partir des Marins recuperes

		

		OutputBuilder outputBuilder = new OutputBuilder();
		String r=outputBuilder.writeActions(this.actions());
		System.out.println(r);
		return r;
	}

	@Override
	public List<String> getLogs() {
		return new ArrayList<>();
	}

	private List<SailorAction> actions() {
		if(this.gameData.getShip().getRames().size()==this.marins.size()){
			return this.marins.stream().map(marin-> new SailorAction(marin.getId(),"oar"))
			.collect(Collectors.toList());
		}
		return null;
		
	}
}
