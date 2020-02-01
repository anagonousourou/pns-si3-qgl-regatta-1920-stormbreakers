package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.GameState;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;

public class Moteur {

	private Bateau ship;
	private List<Marin> sailors;

	private List<Equipment> rightOars;
	private List<Equipment> leftOars;
	private List<Equipment> oars;

	private static final double PI = Math.PI;
	private static final double EPS = 0.01;
	private GameState gState;

	public Moteur(GameState gameState){
		this.gState=gameState;
		this.sailors=gameState.getStateInit().getSailors();
		this.oars = gameState.getStateInit().getShip().getRames();
		this.ship = gameState.getStateInit().getShip();
		this.leftOars = this.oars.stream().filter(oar -> oar.getY() == 0).collect(Collectors.toList());
		this.rightOars = this.oars.stream().filter(oar -> oar.getY() != 0).collect(Collectors.toList());
	}

	public List<SailorAction> reachCheckPoint(Checkpoint target) {
		return dispatchSailors(target);
	}

	/**
	 * Initialisation des elements situes tout a babord, et ceux a tribord
	 */
	private void separateEntities() {
		ship.getRames().forEach(oar -> {
			if (oar.getY() == 0) {
				leftOars.add(oar);
			} else {
				rightOars.add(oar);
			}
		});
	}

	/**
	 * Methode servant a verifier si l'angle vers le checkpoint est valide ou pas.
	 * Creation des SailorAction en conséquence.
	 * 
	 * @param target Checkpoint cible
	 * @return les SailorAction finales
	 */
	public List<SailorAction> dispatchSailors(Checkpoint target) {
		Set<Double> oarsAngles = this.possibleOrientations();
		double angle = this.orientationNeeded(target);
		System.out.println("Angle needed: "+angle);

		if (Math.abs(angle) <= Moteur.EPS
				) {
					System.out.println("Tout droit");
					
			return this.toActivate(leftOars, rightOars);
		} else {
			
			double approachingAngle = oarsAngles.stream().filter(a -> a * angle > 0.0)
					.min((a, b) -> Double.compare(Math.abs(a- angle), Math.abs(b - angle))).get();
			System.out.println("Angle approching: "+approachingAngle);
			System.out.println("Possibles angles: "+oarsAngles);

			return this.minRepartition(this.angleToDiff(approachingAngle));
		}
	}

	public List<SailorAction> actions() {
		gState.actualiserCheckpoints();
		return this.dispatchSailors(gState.getNextCheckpoint());
	}


	/**
	 * Repartition des rames et marins pour aller tout droit.
	 * 
	 * @return les SailorAction finales
	 */
	private List<SailorAction> goesStraight() {
		List<Equipment> results;
		int leftOarsCount = leftOars.size();
		int rightOarsCount = rightOars.size();
		if (leftOarsCount == rightOarsCount) {
			results = ship.getRames();
		} else if (leftOarsCount > rightOarsCount) {
			results = dismissOars(leftOars, leftOarsCount - rightOarsCount);
			results.addAll(rightOars);
		} else {
			results = dismissOars(rightOars, rightOarsCount - leftOarsCount);
			results.addAll(leftOars);
		}

		List<Marin> sailorsInAction;
		// TODO Répartition des marins vers les rames nécessaires contenues dans
		// "results"
		return null;
		// return sailorsInAction.stream().map(marin -> new
		// Oar(marin.getId())).collect(Collectors.toList());
		

	}
/*
	public List<SailorAction> accelerate(){
		List<SailorAction> result = new ArrayList<>();
		List<Marin> yetBusy =new ArrayList<>();
		
		var correspondances = this.marinsDisponibles(this.oars,this.sailors);
		
		for (Equipment oar: this.leftOars){
			boolean breaking=false;
			if (correspondances.get(oar) != null) {
				for (Marin m : correspondances.get(oar)) {
					if(!yetBusy.contains(m)){
						yetBusy.add(m);
						result.add(new Oar(m.getId()));
						result.add(m.howToGoTo(oar.getX(),oar.getY()));
						breaking=true;
						break;
					}
				}
			}
			if(breaking){
				break;
			}
			
		}

		for (Equipment oar: this.rightOars){
			boolean breaking=false;
			if (correspondances.get(oar) != null) {
				for (Marin m : correspondances.get(oar)) {
					if(!yetBusy.contains(m)){
						yetBusy.add(m);
						result.add(new Oar(m.getId()));
						result.add(m.howToGoTo(oar.getX(),oar.getY()));
						breaking=true;
						
						break;
					}
				}
			}
			if(breaking){
				break;
			}
			
		}
		return result;
	}
*/
	/**
	 * Repartition des rames et marins pour avancer en deviant.
	 * 
	 * @param diffToCatch - difference entre le nombre de rames a babord et a
	 *                    babord. Peut etre negatif.
	 * @return les SailorAction finales
	 */
	private List<SailorAction> changeDirection(int diffToCatch) {
		List<Equipment> results;
		int leftOarsCount = leftOars.size();
		int rightOarsCount = rightOars.size();
		if (diffToCatch > 0) {
			results = dismissOars(leftOars, diffToCatch);
			results.addAll(rightOars);
		} else {
			results = dismissOars(rightOars, -diffToCatch);
			results.addAll(leftOars);
		}

		List<Marin> sailorsInAction;
		// TODO Répartition des marins vers les rames nécessaires contenues dans
		// "results"
		return null;
		// return sailorsInAction.stream().map(marin -> new
		// Oar(marin.getId())).collect(Collectors.toList());

	}

	public List<SailorAction> minRepartition(int diffToCatch) {
		if (diffToCatch < 0) {
			return this.toActivate(this.rightOars, -diffToCatch);
		}
		else{
			return this.toActivate(this.leftOars, diffToCatch);
		}

	}

	public List<SailorAction> toActivate(List<Equipment> oars, int nbToActivate) {
		List<SailorAction> result = new ArrayList<>();
		List<Marin> yetBusy =new ArrayList<>();
		int compteur=0;
		HashMap<Equipment,List<Marin>> correspondances = this.marinsDisponibles(oars,this.sailors);
		for (Equipment oar: oars){
			if (correspondances.get(oar) != null) {
				for (Marin m : correspondances.get(oar)) {
					if(!yetBusy.contains(m)){
						yetBusy.add(m);
						result.add(new Oar(m.getId()));
						result.add(m.howToGoTo(oar.getX(),oar.getY()));
						compteur++;
						break;
					}
				}
			}
			if(compteur==nbToActivate){
				break;
			}
			
		}
		return result;
	}
	/**
	 * méthode pour permettre le deplacement en ligne droite
	 * @param oars
	 * @return
	 */
	public List<SailorAction> toActivate(List<Equipment> oarsLeft,List<Equipment> oarsRight) {
		int sizeListMin=Math.min(oarsLeft.size(), oarsRight.size());
		List<SailorAction> result= new ArrayList<>();
		List<SailorAction> leftOarsActivated =toActivate(oarsLeft,sizeListMin);
		List<SailorAction> rightOarsActivated =toActivate(oarsLeft,sizeListMin);
		if(leftOarsActivated.size()==rightOarsActivated.size()) {
			result.addAll(leftOarsActivated);
			result.addAll(rightOarsActivated);
			return result;
		}else if(leftOarsActivated.size()>rightOarsActivated.size()){
			result.addAll(leftOarsActivated.subList(0, rightOarsActivated.size()));
			result.addAll(rightOarsActivated);
			return result;
		}else {
			result.addAll(leftOarsActivated);
			result.addAll(rightOarsActivated.subList(0, leftOarsActivated.size()));
			return result;
		}
	
	}	
	/**
	 * Methode servant a retirer les rames qui ne serviront pas
	 * 
	 * @param sideOars - le side du bateau qui a des rames en trop
	 * @param nb       - nombre de rames en trop
	 * @return List des rames qui seront utilisees
	 */
	private List<Equipment> dismissOars(List<Equipment> sideOars, int nb) {
		return sideOars.stream().limit(sideOars.size() - nb).collect(Collectors.toList());
	}

	/**
	 * Methode servant a retourner les angles possibles du navire, ainsi que la
	 * différence entre le nombre de rames a babord et le nombre de rames a tribord
	 * nécessaire pour chaque angle
	 * 
	 * @return une HashMap donc la key est l'angle, la value etant la difference
	 *         mentionnee au-dessus.
	 */
	private HashMap<Double, Integer> possibleAngles() {
		double nbOars = ship.getRames().size();
		HashMap<Double, Integer> results = new HashMap<>();
		results.put(0.0, 0);
		for (int i = 0; i < leftOars.size(); i++) {
			for (int j = 0; j < rightOars.size(); j++) {
				if (i != j) {
					results.put(PI / (nbOars / (j - i)), j - i);
				}
			}
		}
		return results;
	}

	private int angleToDiff(double angle) {
		var oarsAngles = possibleAngles();
		if (oarsAngles.get(angle) == null) {
			double mapAngle = oarsAngles.keySet().stream().filter(a -> a * angle > 0.0)
					.min((a, b) -> Double.compare(a - angle, b - angle)).get();
			return oarsAngles.get(mapAngle);
		} else {
			return oarsAngles.get(angle);
		}

	}

	private Set<Double> possibleOrientations() {
		double nbOars = ship.getRames().size();
		Set<Double> result = new HashSet<>();
		for (int i = 0; i < leftOars.size(); i++) {
			for (int j = 0; j < rightOars.size(); j++) {
				if (i != j) {
					result.add(PI / (nbOars / (j - i)));
				}
			}
		}

		return result;
	}

	public HashMap<Marin, List<Equipment>> ramesAccessibles() {
		HashMap<Marin, List<Equipment>> results = new HashMap<>();
		sailors.forEach(m -> {
			results.put(m,
					ship.getEquipments().stream()
							.filter(e -> (Math.abs(e.getX() - m.getX()) + Math.abs(e.getY() - m.getY())) <= 5)
							.collect(Collectors.toList()));
		});
		return results;
	}

	public HashMap<Equipment, List<Marin>> marinsDisponibles(List<Equipment> rames,List<Marin> marins) {
		HashMap<Equipment, List<Marin>> results = new HashMap<>();
		rames.forEach(oar -> {
			results.put(oar,
					marins.stream()
							.filter(e -> (Math.abs(e.getX() - oar.getX()) + Math.abs(e.getY() - oar.getY())) <= 5)
							.collect(Collectors.toList()));
		});
		return results;
	}

	public double travelDistance(Checkpoint target) {
		return ship.getPosition().distanceTo(target.getPosition());
	}

	public double orientationNeeded(Checkpoint target) {
		return ship.getPosition().thetaTo(target.getPosition());
	}
}
