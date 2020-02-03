package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
	private Captain captain;

	public Moteur(GameState gameState,Captain captain) {
		this.gState = gameState;
		this.captain=captain;
		this.sailors = gameState.getStateInit().getSailors();
		this.oars = gameState.getStateInit().getShip().getRames();
		this.ship = gameState.getStateInit().getShip();
		int widthship = gameState.getShip().getDeck().getWidth();
		if (widthship % 2 == 1) {//impair
			this.leftOars = this.oars.stream().filter(oar -> oar.getY() < widthship/2).collect(Collectors.toList());
			this.rightOars = this.oars.stream().filter(oar -> oar.getY() > widthship/2).collect(Collectors.toList());
		} else {
			this.leftOars = this.oars.stream().filter(oar -> oar.getY() < widthship/2).collect(Collectors.toList());
			this.rightOars = this.oars.stream().filter(oar -> oar.getY() >= widthship/2).collect(Collectors.toList());
		}

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
		double distance =this.travelDistance(target);
		System.out.println("Angle needed: " + angle);
		System.out.println("Distance :"+distance);
		List<Marin> marinUtilise = new ArrayList<>();
		if (Math.abs(angle) <= Moteur.EPS) {
			System.out.println("Tout droit");

			return captain.toActivate(this.leftOars, this.rightOars, marinUtilise,this.sailors);
		} else {
			Optional<Double> optAngle = oarsAngles.stream().filter(a -> a * angle > 0.0)

					.min((a, b) -> Double.compare(Math.abs(a - angle), Math.abs(b - angle)));
			if (optAngle.isPresent()) {
				double approachingAngle = optAngle.get();
				System.out.println("Angle approching: " + approachingAngle);
				System.out.println("Possibles angles: " + oarsAngles);
				return captain.minRepartition(this.rightOars,this.leftOars, this.angleToDiff(approachingAngle), marinUtilise,this.sailors);
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
		for (int i = 0; i <= leftOars.size(); i++) {
			for (int j = 0; j <= rightOars.size(); j++) {
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

	/**
	 * A déplacer 
	 * @return
	 */
	public Map<Marin, List<Equipment>> ramesAccessibles() {
		HashMap<Marin, List<Equipment>> results = new HashMap<>();
		sailors.forEach(m -> results.put(m,
				ship.getEquipments().stream()
						.filter(e -> (Math.abs(e.getX() - m.getX()) + Math.abs(e.getY() - m.getY())) <= 5)
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
