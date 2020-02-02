package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.GameState;
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

	public Moteur(GameState gameState) {
		this.gState = gameState;
		this.sailors = gameState.getStateInit().getSailors();
		this.oars = gameState.getStateInit().getShip().getRames();
		this.ship = gameState.getStateInit().getShip();
		this.leftOars = this.oars.stream().filter(oar -> oar.getY() == 0).collect(Collectors.toList());
		this.rightOars = this.oars.stream().filter(oar -> oar.getY() != 0).collect(Collectors.toList());
	}

	public List<SailorAction> reachCheckPoint(Checkpoint target) {
		return dispatchSailors(target);
	}

	/**
	 * Methode servant a verifier si l'angle vers le checkpoint est valide ou pas.
	 * Creation des SailorAction en consequence.
	 * 
	 * @param target Checkpoint cible
	 * @return les SailorAction finales
	 */
	public List<SailorAction> dispatchSailors(Checkpoint target) {
		Set<Double> oarsAngles = this.possibleOrientations();
		double angle = this.orientationNeeded(target);
		System.out.println("Angle needed: " + angle);
		List<Marin> marinUtilise = new ArrayList<>();
		if (Math.abs(angle) <= Moteur.EPS) {
			System.out.println("Tout droit");

			return this.toActivate(leftOars, rightOars, marinUtilise);
		} else {
			Optional<Double> optAngle = oarsAngles.stream().filter(a -> a * angle > 0.0)

					.min((a, b) -> Double.compare(Math.abs(a - angle), Math.abs(b - angle)));
			if (optAngle.isPresent()) {
				double approachingAngle = optAngle.get();
				System.out.println("Angle approching: " + approachingAngle);
				System.out.println("Possibles angles: " + oarsAngles);
				return this.minRepartition(this.angleToDiff(approachingAngle), marinUtilise);
			} else {
				return new ArrayList<>();
			}

		}
	}

	public List<SailorAction> actions() {
		gState.actualiserCheckpoints();
		return this.dispatchSailors(gState.getNextCheckpoint());
	}

	/**
	 * Cette méthode fait avancer le bateau en utilisant un marin de chaque coté
	 * 
	 * @return la liste des actions
	 */

	public List<SailorAction> accelerate() {
		List<SailorAction> result = new ArrayList<>();
		List<Marin> yetBusy = new ArrayList<>();

		var correspondances = this.marinsDisponibles(this.oars, this.sailors);

		for (Equipment oar : this.leftOars) {
			boolean breaking = false;
			if (correspondances.get(oar) != null) {
				for (Marin m : correspondances.get(oar)) {
					if (!yetBusy.contains(m)) {
						yetBusy.add(m);
						result.add(new Oar(m.getId()));
						result.add(m.howToGoTo(oar.getX(), oar.getY()));
						breaking = true;
						break;
					}
				}
			}
			if (breaking) {
				break;
			}

		}

		for (Equipment oar : this.rightOars) {
			boolean breaking = false;
			if (correspondances.get(oar) != null) {
				for (Marin m : correspondances.get(oar)) {
					if (!yetBusy.contains(m)) {
						yetBusy.add(m);
						result.add(new Oar(m.getId()));
						result.add(m.howToGoTo(oar.getX(), oar.getY()));
						breaking = true;

						break;
					}
				}
			}
			if (breaking) {
				break;
			}

		}
		return result;
	}

	public List<SailorAction> minRepartition(int diffToCatch, List<Marin> marinUtilise) {
		if (diffToCatch < 0) {
			return this.toActivate(this.rightOars, -diffToCatch, marinUtilise);
		} else {
			return this.toActivate(this.leftOars, diffToCatch, marinUtilise);
		}

	}

	public List<SailorAction> toActivate(List<Equipment> oars, int nbToActivate, List<Marin> marinUtilise) {
		List<SailorAction> result = new ArrayList<>();
		List<Marin> yetBusy = marinUtilise;
		int compteur = 0;
		Map<Equipment, List<Marin>> correspondances = this.marinsDisponibles(oars, this.sailors);
		for (Equipment oar : oars) {
			if (correspondances.get(oar) != null) {
				for (Marin m : correspondances.get(oar)) {
					if (!yetBusy.contains(m)) {
						yetBusy.add(m);
						result.add(new Oar(m.getId()));
						result.add(m.howToGoTo(oar.getX(), oar.getY()));
						compteur++;
						break;
					}
				}
			}
			if (compteur == nbToActivate) {
				break;
			}

		}
		return result;
	}

	/**
	 * methode pour permettre le deplacement en ligne droite
	 * 
	 * @param oars
	 * @return
	 */
	public List<SailorAction> toActivate(List<Equipment> oarsLeft, List<Equipment> oarsRight,
			List<Marin> marinUtilise) {
		int sizeListMin = Math.min(oarsLeft.size(), oarsRight.size());
		List<SailorAction> result = new ArrayList<>();
		List<Marin> areBusyList = marinUtilise;
		List<SailorAction> leftOarsActivated = toActivate(oarsLeft, sizeListMin, areBusyList);
		List<SailorAction> rightOarsActivated = toActivate(oarsLeft, leftOarsActivated.size(), areBusyList);
		if (leftOarsActivated.size() == rightOarsActivated.size()) {
			result.addAll(leftOarsActivated);
			result.addAll(rightOarsActivated);
			return result;
		} else if (leftOarsActivated.size() > rightOarsActivated.size()) {
			result.addAll(leftOarsActivated.subList(0, rightOarsActivated.size()));
			result.addAll(rightOarsActivated);
			return result;
		} else {
			result.addAll(leftOarsActivated);
			result.addAll(rightOarsActivated.subList(0, leftOarsActivated.size()));
			return result;
		}

	}

	/**
	 * Methode servant a retourner les angles possibles du navire, ainsi que la
	 * difference entre le nombre de rames a babord et le nombre de rames a tribord
	 * necessaire pour chaque angle
	 * 
	 * @return une HashMap donc la key est l'angle, la value etant la difference
	 *         mentionnee au-dessus.
	 */
	private Map<Double, Integer> possibleAngles() {
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
		return possibleAngles().get(angle);

	}

	public Set<Double> possibleOrientations() {
		int nbOars = ship.getRames().size();
		Set<Double> result = new HashSet<>();

		double step = PI / nbOars;
		for (int i = 0; i <= nbOars; i++) {
			result.add((-PI / 2) + (i * step));
		}

		return result;
	}

	public Map<Marin, List<Equipment>> ramesAccessibles() {
		HashMap<Marin, List<Equipment>> results = new HashMap<>();
		sailors.forEach(m -> results.put(m,
				ship.getEquipments().stream()
						.filter(e -> (Math.abs(e.getX() - m.getX()) + Math.abs(e.getY() - m.getY())) <= 5)
						.collect(Collectors.toList())));
		return results;
	}

	public Map<Equipment, List<Marin>> marinsDisponibles(List<Equipment> rames, List<Marin> marins) {
		HashMap<Equipment, List<Marin>> results = new HashMap<>();
		rames.forEach(oar -> results.put(oar,
				marins.stream().filter(e -> (Math.abs(e.getX() - oar.getX()) + Math.abs(e.getY() - oar.getY())) <= 5)
						.collect(Collectors.toList())));
		return results;
	}

	public double travelDistance(Checkpoint target) {
		return ship.getPosition().distanceTo(target.getPosition());
	}

	public double orientationNeeded(Checkpoint target) {
		return ship.getPosition().thetaTo(target.getPosition());
	}
}
