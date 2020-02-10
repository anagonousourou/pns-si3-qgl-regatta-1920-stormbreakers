package fr.unice.polytech.si3.qgl.stormbreakers.processing.navire;

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
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.Logger;

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

	public Moteur(GameState gameState, Captain captain) {
		this.gState = gameState;
		this.captain = captain;
		this.sailors = gameState.getStateInit().getSailors();
		this.oars = gameState.getStateInit().getShip().getRames();
		this.ship = gameState.getStateInit().getShip();
		int widthship = gameState.getShip().getDeck().getWidth();
		if (widthship % 2 == 1) {// impair
			this.leftOars = this.oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
			this.rightOars = this.oars.stream().filter(oar -> oar.getY() > widthship / 2).collect(Collectors.toList());
		} else {
			this.leftOars = this.oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
			this.rightOars = this.oars.stream().filter(oar -> oar.getY() >= widthship / 2).collect(Collectors.toList());
		}

	}

	/**
	 * Fonction pour donner les actions pour atteindre un checkpoint
	 * 
	 * @param target
	 * @return La liste d'action à effectuer pour se diriger vers le checkpoint
	 */
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
	List<SailorAction> dispatchSailors(Checkpoint target) {
		Set<Fraction> oarsAngles = this.possibleOrientations();
		double angle = this.orientationNeeded(target);
		List<SailorAction> result = new ArrayList<>();

		// Log angle to move by
		Logger.getInstance().log("a:" + angle + " ");
		List<Marin> marinUtilise = new ArrayList<>();
		if (Math.abs(angle) <= Moteur.EPS) {
			result.addAll(captain.toActivate(this.leftOars, this.rightOars, marinUtilise, this.sailors));
			return result;
		} else if (this.haveGouvernailandIsAccessible() && Math.abs(angle) <= Math.PI / 4) {

			List<Marin> marinAccssibles = captain.marinsProcheGouvernail(this.ship.getGouvernail().get(0),
					this.sailors);
			result.addAll(
					captain.activateRudder(marinUtilise, marinAccssibles, this.ship.getGouvernail().get(0), angle));
			result.addAll(captain.toActivate(this.leftOars, this.rightOars, marinUtilise, this.sailors));
			return result;
		}

		else {
			final double angleForSailor = angle;
			Optional<Fraction> optAngle = oarsAngles.stream().filter(a -> a.eval() * angleForSailor > 0.0)

					.min((a, b) -> Double.compare(Math.abs(a.eval() * PI - angleForSailor),
							Math.abs(b.eval() * PI - angleForSailor)));
			if (optAngle.isPresent()) {
				Fraction approachingAngle = optAngle.get();
				result.addAll(captain.minRepartition(this.rightOars, this.leftOars, this.angleToDiff(approachingAngle),
						marinUtilise, this.sailors));
				return result;
			} else {
				return result;
			}

		}
	}

	private boolean haveGouvernailandIsAccessible() {
		List<Equipment> gouvernail = this.ship.getGouvernail();
		if (!gouvernail.isEmpty()) {
			List<Marin> marinAccssibles = captain.marinsProcheGouvernail(gouvernail.get(0), this.sailors);
			return !marinAccssibles.isEmpty();
		}
		return false;
	}

	public List<SailorAction> actions() {
		gState.actualiserCheckpoints();
		return this.dispatchSailors(gState.getNextCheckpoint());
	}

	/**
	 * Methode servant a retourner les angles possibles du navire, ainsi que la
	 * difference entre le nombre de rames a babord(gauche) et le nombre de rames a
	 * tribord(droite) necessaire pour chaque angle
	 * 
	 * @return une HashMap donc la key est l'angle, la value etant la difference
	 *         mentionnee au-dessus.
	 */
	Map<Fraction, Integer> possibleAngles() {
		int nbOars = ship.getRames().size();
		HashMap<Fraction, Integer> results = new HashMap<>();
		for (int i = 0; i <= leftOars.size(); i++) {
			for (int j = 0; j <= rightOars.size(); j++) {
				if (Math.abs(new Fraction(j - i, nbOars).eval()) <= 0.5) {
						results.put(new Fraction(j - i, nbOars), j - i);
				}
			}
		}
		return results;
	}

	private int angleToDiff(Fraction angle) {
		int e = possibleAngles().get(angle);
		return e;
	}

	public Set<Fraction> possibleOrientations() {
		int nbOars = ship.getRames().size();
		Set<Fraction> result = new HashSet<>();
		for (int i = 0; i <= nbOars; i++) {
			var f = new Fraction(-1, 2).add(new Fraction(i, nbOars));
			result.add(f);
		}
		return result;
	}

	public double travelDistance(Checkpoint target) {
		return ship.getPosition().distanceTo(target.getPosition());
	}

	double orientationNeeded(Checkpoint target) {
		double orientationShip = ship.getPosition().getOrientation();
		Vector orientationUnit = Vector.createUnitVector(orientationShip);

		Vector shipToTarget = new Vector(ship.getPosition(), target.getPosition());

		int angleSign = getAngleSign(target.getPosition(), ship.getPosition(), orientationShip);

		// Si le bateau est sur le point même du checkpoint, l'angle duquel tourner est
		// definit a 0
		Double angleValue = (shipToTarget.norm() > 0) ? orientationUnit.angleBetween(shipToTarget) : 0;
		return (double) ((angleValue != 0) ? angleSign : 1) * angleValue;
	}

	private int getAngleSign(Position target, Position shipPos, Double shipOrientation) {
		// On bouge le plan pour ramener le bateau en 0,0
		Position target2 = new Position(target.getX() - shipPos.getX(), target.getY() - shipPos.getY(), 0);
		// On tourne le plan pour faire pointer le bateau selon l'axe x
		Position target3 = target2.getRotatedBy(-shipOrientation);
		// Si y positif on tourne en sens trigo
		return (target3.getY() > 0) ? 1 : -1;
	}
}
