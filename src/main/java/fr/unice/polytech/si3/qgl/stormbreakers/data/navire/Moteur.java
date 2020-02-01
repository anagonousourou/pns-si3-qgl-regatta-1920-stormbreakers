package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;

public class Moteur {

	private Bateau ship;
	private List<Marin> sailors;

	private List<Equipment> rightOars;
	private List<Equipment> leftOars;
	
	private static final double PI = Math.PI;
	
	public Moteur(Bateau b, List<Marin> s) {
		ship = b;
		sailors = s;
		rightOars = new ArrayList<>();
		leftOars = new ArrayList<>();
		seperateEntities();
	}
	
	public List<SailorAction> sendActions(Checkpoint target) {
		dispatchSailors(target);
		return null;
	}

	/**
	 * Initialisation des elements situes tout a babord, et ceux a tribord
	 */
	private void seperateEntities() {
		ship.getRames().forEach(oar -> {
			if(oar.getY() == 0) {
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
	 * @param target le Checkpoint cible
	 * @return les SailorAction finales
	 */
	public List<SailorAction> dispatchSailors(Checkpoint target) {
		Set<Double> oarsAngles = possibleAngles().keySet();
		double angle = orientationNeeded(target);
		if (angle == 0 || !oarsAngles.contains(angle)) {
			return goesStraight();
		} else {
			return changeDirection(possibleAngles().get(angle));
		}
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
		
		List<Marin> sailorsInAction = new ArrayList<>();
		//TODO Répartition des marins vers les rames nécessaires contenues dans "results"
		return sailorsInAction.stream().map(marin -> new Oar(marin.getId())).collect(Collectors.toList());
	}
	
	/**
	 * Repartition des rames et marins pour avancer en deviant.
	 * 
	 * @param diffToCatch - difference entre le nombre de rames a babord et a babord. Peut etre negatif.
	 * @return les SailorAction finales
	 */
	private List<SailorAction> changeDirection(int diffToCatch) {
		List<Equipment> results;
		int leftOarsCount = leftOars.size();
		int rightOarsCount = rightOars.size();
		if(diffToCatch > 0) {
			results = dismissOars(leftOars, diffToCatch);
			results.addAll(rightOars);
		} else {
			results = dismissOars(rightOars, -diffToCatch);
			results.addAll(leftOars);
		}
		
		List<Marin> sailorsInAction = new ArrayList<>();
		//TODO Répartition des marins vers les rames nécessaires contenues dans "results"
		return sailorsInAction.stream().map(marin -> new Oar(marin.getId())).collect(Collectors.toList());
	}

	/**
	 * Methode servant a retirer les rames qui ne serviront pas
	 * 
	 * @param sideOars - le side du bateau qui a des rames en trop
	 * @param nb - nombre de rames en trop
	 * @return List des rames qui seront utilisees
	 */
	private List<Equipment> dismissOars(List<Equipment> sideOars, int nb) {
		return sideOars.stream().limit(sideOars.size()-nb)
		.collect(Collectors.toList());
	}
	
	/**
	 * Methode servant a retourner les angles possibles du navire, ainsi que 
	 * la différence entre le nombre de rames a babord et le nombre de rames a tribord
	 * nécessaire pour chaque angle
	 * 
	 * @return une HashMap donc la key est l'angle, la value etant la difference mentionnee au-dessus.
	 */
	private HashMap<Double, Integer> possibleAngles(){
		double nbOars = ship.getRames().size();
		HashMap<Double, Integer> results = new HashMap<>();
		results.put(0.0, 0);
		for(int i = 0; i < leftOars.size(); i++) {
			for(int j = 0; j < rightOars.size(); j++) {
				if(i != j) {
					results.put(PI/(nbOars/(j-i)), j-i);
				}
			}
		}
		return results;
	}
	
	public HashMap<Marin,List<Equipment>> ramesAccessibles(){
		HashMap<Marin,List<Equipment>> results = new HashMap<>();
		sailors.forEach(m->{
			results.put(m, ship.getEquipments().stream()
										.filter(e-> (Math.abs(e.getX()-m.getX())+Math.abs(e.getY()-m.getY())) <=5 )
										.collect(Collectors.toList())
			);
		});
		return results;
	}
	
	public double travelDistance(Checkpoint target){
		return ship.getPosition().distanceTo(target.getPosition());
	}

	public double orientationNeeded(Checkpoint target){
		return ship.getPosition().thetaTo(target.getPosition());
	}

	
}
