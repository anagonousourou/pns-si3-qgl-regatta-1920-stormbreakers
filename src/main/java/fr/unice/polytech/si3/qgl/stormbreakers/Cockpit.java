package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
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

	public double travelDistance(Checkpoint target){
		return this.gameData.getShip().getPosition().distanceTo(target.getPosition());
	}

	public double orientationNeeded(Checkpoint target){
		return this.gameData.getShip().getPosition().thetaTo(target.getPosition());
	}

	public String nextRound(String round) {
		OutputBuilder outputBuilder = new OutputBuilder();
		return outputBuilder.writeActions(this.actions());
	}

	public HashMap<Marin,List<Equipment>> ramesAccessibles(List<Equipment> equipments,List<Marin> marins){
		HashMap<Marin,List<Equipment>> results=new HashMap<>();
		marins.forEach(m->{
			results.put(m, equipments.stream()
										.filter(e-> (Math.abs(e.getX()-m.getX())+Math.abs(e.getY()-m.getY())) <=5 )
										.collect(Collectors.toList())

			);
		});

		return results;
	}

	@Override
	public List<String> getLogs() {
		return new ArrayList<>();
	}

	List<SailorAction> actions() {
			List<Marin> leftSailors = new ArrayList<>();
			List<Marin> rightSailors = new ArrayList<>();
			marins.forEach(marin -> {
				if(marin.getY() == 0) {
					leftSailors.add(marin);
				} else {
					rightSailors.add(marin);
				}
			});
			return dispatchSailors(leftSailors, rightSailors);
		
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
			finalSailorsList.addAll(rightSailors);
		} else {
			finalSailorsList = dismissSailors(rightSailors, rightSailorsCount - leftSailorsCount);
			finalSailorsList.addAll(leftSailors);
		}
		return finalSailorsList.stream().map(marin-> new Oar(marin.getId()))
				.collect(Collectors.toList());
	}
	
	/**
	 * Methode servant a retirer les marins qui ne rameront pas
	 * @param sideSailors - le side du bateau qui a trop de marins
	 * @param nb - nombre de marins en trop
	 * @return List des marins qui rameront
	 */
	private List<Marin> dismissSailors(List<Marin> sideSailors, int nb) {
		return sideSailors.stream().limit(sideSailors.size()-nb)
		.collect(Collectors.toList());
	}

	public int getCurrentCheckpoint() {
		return currentCheckpoint;
	}

	public List<Checkpoint> getCheckpoints() {
		return checkpoints;
	}
	
}
