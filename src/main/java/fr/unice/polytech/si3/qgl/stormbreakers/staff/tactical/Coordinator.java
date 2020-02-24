package fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical;

import java.util.*;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.LiftSail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.LowerSail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
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
        actions.forEach(action -> 
            this.getMarinById(action.getSailorId()).get().setDoneTurn(true)
        );

    }

    public boolean rudderIsAccesible() {

        return this.crewManager.marinAround(this.equipmentsManager.rudderPosition());
    }

    /**
     * Renvoie le marin sur le gouvernail ou le plus proche du gouvernail
     * 
     * @return
     */
    public Optional<Sailor> marineForRudder() {
        return this.crewManager.marineAtPosition(this.equipmentsManager.rudderPosition())
                .or(() -> this.crewManager.marineClosestTo(this.equipmentsManager.rudderPosition()));
    }

    public List<SailorAction> activateRudder(double orientation) {
        var optMarine = this.marineForRudder();
        if (optMarine.isPresent() && rudderIsAccesible()) {
            Sailor rudderMarine = optMarine.get();
            List<SailorAction> actions = new ArrayList<>();
            MoveAction tmpMove = rudderMarine.howToMoveTo(this.rudderPosition());
            actions.add(new MoveAction(rudderMarine.getId(), tmpMove.getXdistance(), tmpMove.getYdistance()));
            actions.add(new Turn(rudderMarine.getId(), orientation));
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
        List<Sailor> marinesBusy = new ArrayList<>();
        var marinsDisponibles = this.marinsDisponiblesVoiles(true);
        for (Sail sail : this.equipmentsManager.sails(true)) {

            for (Sailor sailor : marinsDisponibles.get(sail)) {
                if (!sailor.isDoneTurn() && !marinesBusy.contains(sailor)) {
                    actions.add(sailor.howToMoveTo(sail.getPosition()));
                    actions.add(new LowerSail(sailor.getId()));
                    marinesBusy.add(sailor);
                    nbOpenned--;
                    break;
                }
            }

        }
        return nbOpenned;
    }

    public int liftSailsPartially(List<SailorAction> actions) {
        int nbOpenned = this.nbSailsOpenned();
        List<Sailor> marinesBusy = new ArrayList<>();
        var marinsDisponibles = this.marinsDisponiblesVoiles(false);
        for (Sail sail : this.equipmentsManager.closedSails()) {
            for (Sailor sailor : marinsDisponibles.get(sail)) {
                if (!sailor.isDoneTurn() && !marinesBusy.contains(sailor)) {
                    actions.add(sailor.howToMoveTo(sail.getPosition()));
                    actions.add(new LiftSail(sailor.getId()));
                    marinesBusy.add(sailor);
                    nbOpenned++;
                    break;
                }
            }

        }
        return nbOpenned;
    }

    public boolean allSailsAreOpenned() {
        return this.nbSails() == this.nbSailsOpenned();
    }

    private int nbSailsOpenned() {
        return this.equipmentsManager.nbOpennedSails();
    }

    public int nbMarinsOnLeftOars() {
        return (int) this.equipmentsManager.allLeftOars().stream()
                .filter(oar -> this.crewManager.marineAtPosition(oar.getPosition()).isPresent()).count();
    }

    public int nbMarinsOnRightOars() {
        return (int) this.equipmentsManager.allRightOars().stream()
                .filter(oar -> this.crewManager.marineAtPosition(oar.getPosition()).isPresent()).count();
    }

    public Map<Oar, List<Sailor>> marinsDisponiblesForOar() {
        HashMap<Oar, List<Sailor>> results = new HashMap<>();
        this.equipmentsManager.oars().forEach(oar -> results.put(oar,
                this.crewManager.marins().stream().filter(m -> m.canReach(oar.getPosition())).collect(Collectors.toList())));
        return results;
    }

    public Map<Equipment, List<Sailor>> marinsDisponiblesVoiles(boolean isOpened) {
        HashMap<Equipment, List<Sailor>> results = new HashMap<>();
        this.equipmentsManager.sails(isOpened).forEach(sail -> results.put(sail,
                this.crewManager.marins().stream().filter(m -> m.canReach(sail.getPosition())).collect(Collectors.toList())));
        return results;
    }

    /**
     * Return sailoractions to activate exactly nb oars on left if it is not
     * possible return empty list Do not make compromise the size of the list must
     * be equals to either nb or 0 sailors actions include OarAction and MoveAction
     * 
     * @param nb
     * @param idsOfBusySailors
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

    public List<SailorAction> activateOarsOnRight(int nb, List<Sailor> busy) {
        // Use the isDoneTurn method to know if a sailor has been used or not
        return this.activateNbOars(this.equipmentsManager.allRightOars(), nb, busy);
    }

    public List<SailorAction> activateOarsOnLeft(int nb, List<Sailor> busy) {
        // Use the isDoneTurn method to know if a sailor has been used or not
        return this.activateNbOars(this.equipmentsManager.allLeftOars(), nb, busy);
    }

    List<SailorAction> activateNbOars(List<Oar> oars, int nb) {
        return this.activateNbOars(oars, nb, new ArrayList<>());

    }

    List<SailorAction> activateNbOars(List<Oar> oars, int nb, List<Sailor> yetBusy) {
        // Use the isDoneTurn method and a list to know if a sailor is available or not
        // but do not set value doneTurn only methods in Captain will do that
        List<SailorAction> result = new ArrayList<>();
        int compteur = 0;
        Map<Oar, List<Sailor>> correspondances = this.marinsDisponiblesForOar();
        for (Oar oar : oars) {
            if (correspondances.get(oar) != null) {
                for (Sailor m : correspondances.get(oar)) {
                    if (!yetBusy.contains(m) && !m.isDoneTurn()) {
                        yetBusy.add(m);
                        result.add(m.howToMoveTo(new IntPosition(oar.getX(), oar.getY())));
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

    /**
     * @param nb différence entre nbdroite-nbgauche
     * @return
     */
    public List<SailorAction> activateOars(int nb) {

        if (nb > 0) {
            return this.activateOarsOnRight(nb);
        } else {
            return this.activateOarsOnLeft(-nb);
        }

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

            } while (actions.isEmpty()&& nb > 0);

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

        Optional<Sailor> leftSailor = Optional.empty();
        Optional<Sailor> rightSailor = Optional.empty();

        Oar currLeftOar = null;
        Oar currRightOar = null;

        // Try to find someone who can oar on left side
        while (!leftSailor.isPresent() && !freeOarsOnLeftSide.isEmpty()) {
            currLeftOar = freeOarsOnLeftSide.get(0);
            freeOarsOnLeftSide.remove(currLeftOar);

            IntPosition targetPos = currLeftOar.getPosition();
            List<Sailor> sailorsInReach = crewManager.getSailorsWhoCanReach(availableSailors, targetPos);
            leftSailor = crewManager.marineClosestTo(targetPos, sailorsInReach);
        }

        if (leftSailor.isPresent()) {
            // Cannot be used on both sides
            availableSailors.remove(leftSailor.get());

            // Try to find someone who can oar on right side
            while (!rightSailor.isPresent() && !freeOarsOnRightSide.isEmpty()) {
                currRightOar = freeOarsOnRightSide.get(0);
                freeOarsOnRightSide.remove(currRightOar);

                IntPosition targetPos = currRightOar.getPosition();
                List<Sailor> sailorsInReach = crewManager.getSailorsWhoCanReach(availableSailors, targetPos);
                rightSailor = crewManager.marineClosestTo(targetPos, sailorsInReach);
            }
        }

        if (leftSailor.isPresent() && rightSailor.isPresent()) {
            // Ask left sailor to move then oar
            actionsToReturn.add(leftSailor.get().howToMoveTo(currLeftOar.getPosition()));
            actionsToReturn.add(new OarAction(leftSailor.get().getId()));

            // Ask right sailor to move then oar
            actionsToReturn.add(rightSailor.get().howToMoveTo(currRightOar.getPosition()));
            actionsToReturn.add(new OarAction(rightSailor.get().getId()));
        }

        return actionsToReturn;

    }

    public void resetAvailability() {
        this.crewManager.resetAvailability();
        this.equipmentsManager.resetUsedStatus();
    }

    public Optional<Sailor> getMarinById(int id) {
        return this.crewManager.getMarinById(id);
    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à gauche
     * 
     * @param equimentManager
     * @return
     */
    public List<Sailor> leftSailorsOnOars() {
        List<Sailor> sailors = new ArrayList<>();
        for (Oar oar : equipmentsManager.allLeftOars()) {
            Optional<Sailor> theSailor = this.crewManager.marineAtPosition(oar.getPosition());
            if (theSailor.isPresent()) {
                sailors.add(theSailor.get());
            }
        }

        return sailors;

    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à droite
     * 
     * @param equimentManager
     * @return
     */
    public List<Sailor> rightSailorsOnOars() {
        List<Sailor> sailors = new ArrayList<>();
        for (Oar oar : equipmentsManager.allRightOars()) {
            Optional<Sailor> theSailor = this.crewManager.marineAtPosition(oar.getPosition());
            if (theSailor.isPresent()) {
                sailors.add(theSailor.get());
            }
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
        Map<Equipment, List<Sailor>> correspondances = marinsDisponiblesVoiles(isOpened);
        for (Sail sail : sails) {
            if (correspondances.get(sail).isEmpty()) {
                return false;
            }
            Sailor firstSailor = correspondances.get(sail).get(0);
            // Retrieve the sailor that can act on the current sail
            // from every entry, so that next sails will know that this sailor can't act on
            // them
            correspondances.entrySet().forEach(c -> {
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

            var optMarin = this.crewManager.getMarinById(oa.getSailorId());

            if (optMarin.isPresent()) {

                Sailor sailor = optMarin.get();

                var eq = this.equipmentsManager.equipmentAt(sailor.getPosition());

                if (eq.isPresent() && oa.compatibleEquipmentType().equals(eq.get().getType())) {
                    eq.get().setUsed(true);
                }
            }

        }

    }

    /**
     * Cette méthode va déplacer les marins en utilisant les MoveAction
     * de la liste
     * puis simulera l'execution des autres actions en marquant
     * les équipements concernés avec used=true
     * il faut vérifier que l'action correspond au type d'équipement
     * bien sur si le movingaction conduit le marin vers une case où il n'y
     * a pas d'équipement on fait rien
     * Cela permettra de voir les équipements déja utilisés
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
     * Rapproche au mieux les marins non-assignés de rames non-occupées
     * NB: utilise une copie de sailors
     */
    List<MoveAction> bringSailorsCloserToOars() {
        List<Sailor> availableSailors = crewManager.getAvailableSailors();

        List<Oar> unusedOars = new ArrayList<>();
        unusedOars.addAll(equipmentsManager.unusedLeftOars());
        unusedOars.addAll(equipmentsManager.unusedRightOars());

        return bringSailorsCloserToOars(new ArrayList<>(availableSailors),unusedOars);
    }

    /**
     * Rapproche au mieux les marins des rames en parametre
     * NB: modifie la liste de marins (retire ceux assignés)
     * @param sailors Liste de marins
     * @param oars rames à approcher
     */
    List<MoveAction> bringSailorsCloserToOars(List<Sailor> sailors, List<Oar> oars) {
        List<MoveAction> moves = new ArrayList<>();
        Iterator<Oar> it = oars.iterator();

        for ( ; it.hasNext(); ) {
            Oar currentOar = it.next();
            Optional<Sailor> closestSailorOpt = sailors.stream().min(Comparator.comparingInt(a -> a.getDistanceTo(currentOar.getPosition())));

            if (closestSailorOpt.isPresent()) {
                Sailor closestSailor = closestSailorOpt.get();
                moves.add(closestSailor.howToGetCloserTo(currentOar.getPosition()));
                sailors.remove(closestSailor); // Already assigned
            }
        }
        return moves;
    }

}