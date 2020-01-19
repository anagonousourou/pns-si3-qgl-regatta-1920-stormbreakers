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

	List<SailorAction> actions() {
		if(this.gameData.getShip().getRames().size()==this.marins.size()){
			return this.marins.stream().map(marin-> new SailorAction(marin.getId(),"oar"))
			.collect(Collectors.toList());
		} else if(this.gameData.getShip().getRames().size() > this.marins.size()) {
			List<Marin> leftSailors = new ArrayList<>();
			List<Marin> rightSailors = new ArrayList<>();
			marins.forEach(marin -> {
				if(marin.getX() == gameData.getShip().getDeck().getLength() - 1) {
					rightSailors.add(marin);
				} else {
					leftSailors.add(marin);
				}
			});
			return dispatchSailors(leftSailors, rightSailors);
		} else if(this.gameData.getShip().getRames().size()<this.marins.size()){
			//TODO : si c'est un cas possible... sinon, condition a virer
			return null;
		}
		return null;
		
	}
	/**
	 * Methode servant a trouver un nombre equilibre entre les marins ramant a droite et ceux a gauche
	 * @param leftSailors
	 * @param rightSailors
	 * @return les SailorAction finales
	 */
	private List<SailorAction> dispatchSailors(List<Marin> leftSailors, List<Marin> rightSailors){
		List<Marin> finalSailorsList;
		int leftSailorsCount = leftSailors.size();
		int rightSailorsCount = rightSailors.size();
		if(leftSailorsCount == rightSailorsCount) {
			finalSailorsList = marins;
		} else if(leftSailorsCount > rightSailorsCount) {
			finalSailorsList = dismissSailors(leftSailors, leftSailorsCount - rightSailorsCount);
		} else {
			finalSailorsList = dismissSailors(rightSailors, rightSailorsCount - leftSailorsCount);
		}
		
		return finalSailorsList.stream().map(marin-> new SailorAction(marin.getId(),"oar"))
				.collect(Collectors.toList());
	}
	
	/**
	 * Methode servant a retirer les marins qui ne rameront pas
	 * @param sideSailors - le side du bateau qui a trop de marins
	 * @param nb - nombre de marins en trop
	 * @return List des marins qui ne rameront pas
	 */
	private List<Marin> dismissSailors(List<Marin> sideSailors, int nb) {
		List<Marin> tmp = new ArrayList<>(marins);
		while(nb > 0) {
			tmp.remove(sideSailors.get(nb-1));
			nb--;
		}
		return tmp;
	}
}
