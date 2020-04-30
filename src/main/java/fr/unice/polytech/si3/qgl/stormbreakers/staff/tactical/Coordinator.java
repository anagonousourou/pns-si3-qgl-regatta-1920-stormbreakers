package fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.LiftSail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.LowerSail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.UseWatch;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CrewManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;

public class Coordinator {
    private CrewManager crewManager;
    private EquipmentsManager equipmentsManager;

    public Coordinator(CrewManager crewManager, EquipmentsManager equipmentsManager) {
        this.crewManager = crewManager;
        this.equipmentsManager = equipmentsManager;
    }

    void validateActions(List<SailorAction> actions) {
        actions.forEach(action -> this.getSailorById(action.getSailorId()).get().setDoneTurn(true));

    }

    public boolean rudderIsAccesible() {
        return this.crewManager.sailorAround(this.equipmentsManager.rudderPosition());
    }

    /**
     * Renvoie le marin sur le gouvernail ou le plus proche du gouvernail
     * 
     * @return
     */
    public Optional<Sailor> sailorForRudder() {
        return this.crewManager.availableSailorAtPosition(this.equipmentsManager.rudderPosition())
                .or(() -> this.crewManager.availableSailorClosestTo(this.equipmentsManager.rudderPosition()));
    }

    public List<SailorAction> activateRudder(double orientation) {
        var optMarine = this.sailorForRudder();
        if (optMarine.isPresent() && rudderIsAccesible()) {
            Sailor rudderSailor = optMarine.get();
            List<SailorAction> actions = new ArrayList<>(2);
            
            actions.add(rudderSailor.howToMoveTo(this.rudderPosition()));
            actions.add(new Turn(rudderSailor.getId(), orientation));
            return actions;
        }
        return List.of();

    }

    public boolean rudderIsPresent() {
        return this.equipmentsManager.rudderIsPresent();
    }

    public IntPosition rudderPosition() {
        return this.equipmentsManager.rudderPosition();
    }

    public int nbOars() {
        return this.equipmentsManager.nbOars();
    }

    public int nbLeftOars() {
        return this.equipmentsManager.nbLeftOars();
    }

    public int nbRightOars() {
        return this.equipmentsManager.nbRightOars();
    }

    public int nbSails() {
        return this.equipmentsManager.nbSails();
    }

    public int lowerSailsPartially(List<SailorAction> actions) {
        int nbOpenned = this.nbSailsOpenned();
        List<Sailor> busySailors = new ArrayList<>();
        var availableSailors = this.availableSailorsForSails(true);
        for (Sail sail : this.equipmentsManager.sails(true)) {

            for (Sailor sailor : availableSailors.get(sail)) {
                if (!sailor.isDoneTurn() && !busySailors.contains(sailor)) {
                    actions.add(sailor.howToMoveTo(sail.getPosition()));
                    actions.add(new LowerSail(sailor.getId()));
                    busySailors.add(sailor);
                    nbOpenned--;
                    break;
                }
            }

        }
        return nbOpenned;
    }

    public int liftSailsPartially(List<SailorAction> actions) {
        int nbOpenned = this.nbSailsOpenned();
        List<Sailor> busySailors = new ArrayList<>();
        var availableSailors = this.availableSailorsForSails(false);
        for (Sail sail : this.equipmentsManager.closedSails()) {
            for (Sailor sailor : availableSailors.get(sail)) {
                if (!sailor.isDoneTurn() && !busySailors.contains(sailor)) {
                    actions.add(sailor.howToMoveTo(sail.getPosition()));
                    actions.add(new LiftSail(sailor.getId()));
                    busySailors.add(sailor);
                    nbOpenned++;
                    break;
                }
            }

        }
        return nbOpenned;
    }

    private int nbSailsOpenned() {
        return this.equipmentsManager.nbOpennedSails();
    }

    public Map<Oar, List<Sailor>> marinsDisponiblesForOar() {
        HashMap<Oar, List<Sailor>> results = new HashMap<>();
        this.equipmentsManager.oars().forEach(oar -> results.put(oar, this.crewManager.sailors().stream()
                .filter(m -> m.canReach(oar.getPosition())).collect(Collectors.toList())));
        return results;
    }

    public Map<Equipment, List<Sailor>> availableSailorsForSails(boolean isOpened) {
        HashMap<Equipment, List<Sailor>> results = new HashMap<>();
        this.equipmentsManager.sails(isOpened).forEach(sail -> results.put(sail, this.crewManager.sailors().stream()
                .filter(m -> m.canReach(sail.getPosition())).collect(Collectors.toList())));
        return results;
    }

    /**
     * Return sailoractions to activate exactly nb oars on left if it is not
     * possible return empty list Do not make compromise the size of the list must
     * be equals to either nb or 0 sailors actions include OarAction and MoveAction
     * 
     * @param nb
     * @return
     */
    public List<SailorAction> activateOarsOnLeft(int nb) {
        return this.activateNbOars(this.equipmentsManager.allLeftOars(), nb);
    }

    /**
     * Similar to activateOarsOnLeft(int nb) but on right
     * 
     * @param nb
     * @return
     */
    public List<SailorAction> activateOarsOnRight(int nb) {
        // Use the isDoneTurn method to know if a sailor has been used or not
        return this.activateNbOars(this.equipmentsManager.allRightOars(), nb);
    }

    List<SailorAction> activateNbOars(List<Oar> oars, int nb) {
        return this.activateNbOars(oars, nb, new ArrayList<>());

    }

    List<SailorAction> activateNbOars(List<Oar> oars, int nb, List<Sailor> yetBusy) {
        // Use the isDoneTurn method and a list to know if a sailor is available or not
        // but do not set value doneTurn only methods in Captain will do that
        List<SailorAction> result = new ArrayList<>();
        int compteur = 0;
        Map<Oar, List<Sailor>> matches = this.availableSailorsForOar();
        for (Oar oar : oars) {
            if (matches.get(oar) != null) {
                for (Sailor m : matches.get(oar)) {
                    if (!yetBusy.contains(m) && !m.isDoneTurn()) {
                        yetBusy.add(m);
                        result.add(m.howToMoveTo(new IntPosition(oar.x(), oar.y())));
                        result.add(new OarAction(m.getId()));
                        compteur++;
                        break;
                    }
                }
            }
            if (compteur == nb) {
                break;
            }

        }
        return result;
    }

    public List<SailorAction> activateOarsNotStrict(int nb) {
        List<SailorAction> actions;
        if (nb > 0) {

            do {
                actions = this.activateOarsOnRight(nb);
                nb--;
            } while (actions.isEmpty() && nb > 0);

            return actions;
        } else {
            nb = -nb;
            do {
                actions = this.activateOarsOnLeft(nb);
                nb--;

            } while (actions.isEmpty() && nb > 0);

            return actions;
        }
    }

    /**
     * Prépare l'ajout d'un rameur de chaque coté du bateau
     * 
     * @return actions move et oar nécessaires
     */
    public List<SailorAction> addOaringSailorsOnEachSide() {
        List<SailorAction> actionsToReturn = new ArrayList<>();

        List<Oar> freeOarsOnLeftSide = new ArrayList<>(equipmentsManager.unusedLeftOars());
        List<Oar> freeOarsOnRightSide = new ArrayList<>(equipmentsManager.unusedRightOars());

        List<Sailor> availableSailors = crewManager.getAvailableSailors();

        // Can't assign to non-free oars
        // Can't assign when no sailors available
        if (freeOarsOnLeftSide.isEmpty() || freeOarsOnRightSide.isEmpty() || availableSailors.isEmpty())
            return List.of();

        Optional<Sailor> leftSailorOpt = Optional.empty();
        Optional<Sailor> rightSailorOpt = Optional.empty();

        Oar currLeftOar = null;
        Oar currRightOar = null;

        // Try to find someone who can oar on left side
        while (leftSailorOpt.isEmpty() && !freeOarsOnLeftSide.isEmpty()) {
            currLeftOar = freeOarsOnLeftSide.get(0);
            freeOarsOnLeftSide.remove(currLeftOar);

            IntPosition targetPos = currLeftOar.getPosition();
            List<Sailor> sailorsInReach = crewManager.getSailorsWhoCanReach(availableSailors, targetPos);
            leftSailorOpt = crewManager.sailorClosestTo(targetPos, sailorsInReach);
        }

        if (leftSailorOpt.isPresent()) {
            // Cannot be used on both sides
            availableSailors.remove(leftSailorOpt.get());

            // Try to find someone who can oar on right side
            while (rightSailorOpt.isEmpty() && !freeOarsOnRightSide.isEmpty()) {
                currRightOar = freeOarsOnRightSide.get(0);
                freeOarsOnRightSide.remove(currRightOar);

                IntPosition targetPos = currRightOar.getPosition();
                List<Sailor> sailorsInReach = crewManager.getSailorsWhoCanReach(availableSailors, targetPos);
                rightSailorOpt = crewManager.sailorClosestTo(targetPos, sailorsInReach);
            }
        }

        if (leftSailorOpt.isPresent() && rightSailorOpt.isPresent()) {
            // Ask left sailor to move then oar
            actionsToReturn.add(leftSailorOpt.get().howToMoveTo(currLeftOar.getPosition()));
            actionsToReturn.add(new OarAction(leftSailorOpt.get().getId()));

            // Ask right sailor to move then oar
            actionsToReturn.add(rightSailorOpt.get().howToMoveTo(currRightOar.getPosition()));
            actionsToReturn.add(new OarAction(rightSailorOpt.get().getId()));
        }

        return actionsToReturn;

    }

    public void resetAvailability() {
        this.crewManager.resetAvailability();
        this.equipmentsManager.resetUsedStatus();
    }

    public Optional<Sailor> getSailorById(int id) {
        return this.crewManager.getSailorById(id);
    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à gauche
     * 
     * @return
     */
    public List<Sailor> leftSailorsOnOars() {
        List<Sailor> sailors = new ArrayList<>();
        for (Oar oar : equipmentsManager.allLeftOars()) {
            Optional<Sailor> theSailor = this.crewManager.sailorAtPosition(oar.getPosition());
            theSailor.ifPresent(sailors::add);
        }

        return sailors;

    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à droite
     * 
     * @return
     */
    public List<Sailor> rightSailorsOnOars() {
        List<Sailor> sailors = new ArrayList<>();
        for (Oar oar : equipmentsManager.allRightOars()) {
            Optional<Sailor> theSailor = this.crewManager.sailorAtPosition(oar.getPosition());
            theSailor.ifPresent(sailors::add);
        }

        return sailors;
    }

    /**
     * Method used to know if we can lift or lower all of the sails.
     * 
     * @param isOpened - true if we want to lower them, false if we want to lift
     *                 them.
     * @return true or false
     */

    private boolean canActOnSails(boolean isOpened) {
        List<Sail> sails = equipmentsManager.sails(isOpened);
        Map<Equipment, List<Sailor>> matches = availableSailorsForSails(isOpened);
        for (Sail sail : sails) {
            if (matches.get(sail).isEmpty()) {
                return false;
            }
            Sailor firstSailor = matches.get(sail).get(0);
            // Retrieve the sailor that can act on the current sail
            // from every entry, so that next sails will know that this sailor can't act on
            // them
            matches.entrySet().forEach(c -> {
                if (c.getValue().contains(firstSailor)) {
                    c.getValue().remove(firstSailor);
                }
            });
        }
        return true;
    }

    /**
     * Method used to know if we can lower all of the sails.
     * 
     * @return true or false
     */
    public boolean canLowerAllSails() {
        return canActOnSails(true);
    }

    /**
     * Method used to know if we can lift all of the sails.
     * 
     * @return true or false
     */
    public boolean canLiftAllSails() {
        return canActOnSails(false);
    }

    public void markEquipmentUsedByActions(List<SailorAction> actions) {

        for (SailorAction oa : actions) {

            var optSailor = this.crewManager.getSailorById(oa.getSailorId());

            if (optSailor.isPresent()) {

                Sailor sailor = optSailor.get();

                var eq = this.equipmentsManager.equipmentAt(sailor.getPosition());

                if (eq.isPresent() && oa.compatibleEquipmentType().equals(eq.get().getType())) {
                    eq.get().setUsed(true);
                }
            }

        }

    }

    /**
     * Cette méthode va déplacer les marins en utilisant les MoveAction de la liste
     * puis simulera l'execution des autres actions en marquant les équipements
     * concernés avec used=true il faut vérifier que l'action correspond au type
     * d'équipement bien sur si le movingaction conduit le marin vers une case où il
     * n'y a pas d'équipement on fait rien Cela permettra de voir les équipements
     * déja utilisés
     * 
     * @param actions
     */

    void executeSailorsActions(List<SailorAction> actions) {

        List<MoveAction> moves = actions.stream().filter(act -> act.getType().equals(ActionType.MOVING.actionCode))
                .map(act -> (MoveAction) act).collect(Collectors.toList());

        List<SailorAction> otherActions = actions.stream()
                .filter(act -> !act.getType().equals(ActionType.MOVING.actionCode)).collect(Collectors.toList());

        this.crewManager.executeMoves(moves);
        this.markEquipmentUsedByActions(otherActions);

    }

    /**
     * Rapproche, tant que possible, de chaque équipement non-utilisé, un marin non
     * assignés ordre de priorité: gouvernail, puis rames, puis voiles
     * 
     * @return déplacements générés
     */
    public List<SailorAction> manageUnusedSailors() {
        List<Sailor> availableSailors = crewManager.getAvailableSailors();
        List<SailorAction> moves = new ArrayList<>();

        if (rudderIsPresent() && !equipmentsManager.isRudderUsed()) {
            moves.add(crewManager.bringClosestSailorCloserTo(availableSailors, rudderPosition()));
        }
        moves.addAll(bringSailorsCloserToEquipments(availableSailors,
                new ArrayList<Equipment>(equipmentsManager.unusedOars())));
        moves.addAll(
                bringSailorsCloserToEquipments(availableSailors, new ArrayList<Equipment>(equipmentsManager.sails())));

        return moves.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Rapproche au mieux les marins des equipements en parametre NB: modifie la
     * liste de marins (retire ceux assignés)
     * 
     * @param sailors    Liste de marins
     * @param equipments à approcher
     * @return déplacements générés
     */
    List<MoveAction> bringSailorsCloserToEquipments(List<Sailor> sailors, List<Equipment> equipments) {
        List<MoveAction> moves = new ArrayList<>();
        Iterator<Equipment> it = equipments.iterator();

        while (it.hasNext()) {
            Equipment currentEquipment = it.next();
            MoveAction generatedMove = crewManager.bringClosestSailorCloserTo(sailors, currentEquipment.getPosition());
            if (generatedMove != null)
                moves.add(generatedMove);
        }
        return moves;
    }
    
    /**
     * Trouve un Sailor proche ou positionné à la vigie
     * @return selected sailor
     */
    
    Optional<Sailor> findSailorForWatch(){
    	IntPosition watchPos = equipmentsManager.watchPosition();
    	if(this.crewManager.sailorAround(this.equipmentsManager.watchPosition()))
    	 return this.crewManager.availableSailorAtPosition(watchPos)
        .or(() -> this.crewManager.availableSailorClosestTo(watchPos));
    	return Optional.empty();
    }
    /**
     * Assigne un sailor a la vigie
     * @return liste des actions à effectuer pour cela
     */
	List<SailorAction> setSailorToWatch() {
		List<Sailor> availableSailors = crewManager.getAvailableSailors();
		List<SailorAction> actions = new ArrayList<>();
		if(equipmentsManager.watchIsPresent() && !equipmentsManager.isWatchUsed() && !availableSailors.isEmpty()) {
			Optional<Sailor> chosenSailor = findSailorForWatch();
			if(chosenSailor.isPresent()) {
                Sailor sailorForWatch = chosenSailor.get();
                
                actions.add(sailorForWatch.howToMoveTo(equipmentsManager.watchPosition()));
                actions.add(new UseWatch(sailorForWatch.getId()));
			}
		}
		return actions;
	}
}