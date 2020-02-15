package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

public class MediatorCrewEquipment {
    private Crew crew;
    private EquipmentManager equipmentManager;

    public MediatorCrewEquipment(Crew crew, EquipmentManager equipmentManager) {
        this.crew = crew;
        this.equipmentManager = equipmentManager;
    }

    void validateActions(List<SailorAction> actions) {
        actions.forEach(action -> {
            this.getMarinById(action.getSailorId()).get().setDoneTurn(true);
        });

    }

    public boolean rudderIsAccesible() {

        return this.crew.marinAround(this.equipmentManager.rudderPosition());
    }

    /**
     * Renvoie le marin sur le gouvernail ou le plus proche du gouvernail
     * 
     * @return
     */
    public Optional<Marine> marineForRudder() {
        return this.crew.marineAtPosition(this.equipmentManager.rudderPosition())
                .or(() -> this.crew.marineClosestTo(this.equipmentManager.rudderPosition()));
    }

    public List<SailorAction> activateRudder(double orientation) {
        var optMarine = this.marineForRudder();
        if (optMarine.isPresent() && rudderIsAccesible()) {
            Marine rudderMarine = optMarine.get();
            List<SailorAction> actions = new ArrayList<>();
            MoveAction tmpMove = rudderMarine.howToMoveTo(this.rudderPosition());
            actions.add(new MoveAction(rudderMarine.getId(), tmpMove.getXdistance(), tmpMove.getYdistance()));
            actions.add(new Turn(rudderMarine.getId(), orientation));
            return actions;
        }
        return List.of();

    }

    public boolean rudderIsPresent() {
        return this.equipmentManager.rudderIsPresent();
    }

    public IntPosition rudderPosition() {
        return this.equipmentManager.rudderPosition();
    }

    public int nbOars() {
        return this.equipmentManager.nbOars();
    }

    public int nbLeftOars() {
        return this.equipmentManager.nbLeftOars();
    }

    public int nbRightOars() {
        return this.equipmentManager.nbRightOars();
    }

    public int nbSails() {
        return this.equipmentManager.nbSails();
    }

    public int lowerSailsPartially(List<SailorAction> actions) {
        int nbOpenned = this.nbSailsOpenned();
        List<Marine> marinesBusy = new ArrayList<>();
        var marinsDisponibles = this.marinsDisponiblesVoiles(true);
        for (Sail sail : this.equipmentManager.sails(true)) {

            for (Marine marine : marinsDisponibles.get(sail)) {
                if (!marine.isDoneTurn() && !marinesBusy.contains(marine)) {
                    actions.add(marine.howToMoveTo(sail.getPosition()));
                    actions.add(new LowerSail(marine.getId()));
                    marinesBusy.add(marine);
                    nbOpenned--;
                    break;
                }
            }

        }
        return nbOpenned;
    }

    public int liftSailsPartially(List<SailorAction> actions) {
        int nbOpenned = this.nbSailsOpenned();
        List<Marine> marinesBusy = new ArrayList<>();
        var marinsDisponibles = this.marinsDisponiblesVoiles(false);
        for (Sail sail : this.equipmentManager.closedSails()) {
            for (Marine marine : marinsDisponibles.get(sail)) {
                if (!marine.isDoneTurn() && !marinesBusy.contains(marine)) {
                    actions.add(marine.howToMoveTo(sail.getPosition()));
                    actions.add(new LiftSail(marine.getId()));
                    marinesBusy.add(marine);
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
        return this.equipmentManager.nbOpennedSails();
    }

    public int nbMarinsOnLeftOars() {
        return (int) this.equipmentManager.allLeftOars().stream()
                .filter(oar -> this.crew.marineAtPosition(oar.getPosition()).isPresent()).count();
    }

    public int nbMarinsOnRightOars() {
        return (int) this.equipmentManager.allRightOars().stream()
                .filter(oar -> this.crew.marineAtPosition(oar.getPosition()).isPresent()).count();
    }

    public Map<Oar, List<Marine>> marinsDisponiblesForOar() {
        HashMap<Oar, List<Marine>> results = new HashMap<>();
        this.equipmentManager.oars().forEach(oar -> results.put(oar,
                this.crew.marins().stream().filter(m -> m.canReach(oar.getPosition())).collect(Collectors.toList())));
        return results;
    }

    public Map<Equipment, List<Marine>> marinsDisponiblesVoiles(boolean isOpened) {
        HashMap<Equipment, List<Marine>> results = new HashMap<>();
        this.equipmentManager.sails(isOpened).forEach(sail -> results.put(sail,
                this.crew.marins().stream().filter(m -> m.canReach(sail.getPosition())).collect(Collectors.toList())));
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
        return this.activateNbOars(this.equipmentManager.allLeftOars(), nb);
    }

    /**
     * Similar to activateOarsOnLeft(int nb) but on right
     * 
     * @param nb
     * @return
     */
    public List<SailorAction> activateOarsOnRight(int nb) {
        // Use the isDoneTurn method to know if a sailor has been used or not
        return this.activateNbOars(this.equipmentManager.allRightOars(), nb);
    }

    public List<SailorAction> activateOarsOnRight(int nb, List<Marine> busy) {
        // Use the isDoneTurn method to know if a sailor has been used or not
        return this.activateNbOars(this.equipmentManager.allRightOars(), nb, busy);
    }

    public List<SailorAction> activateOarsOnLeft(int nb, List<Marine> busy) {
        // Use the isDoneTurn method to know if a sailor has been used or not
        return this.activateNbOars(this.equipmentManager.allLeftOars(), nb, busy);
    }

    List<SailorAction> activateNbOars(List<Oar> oars, int nb) {
        return this.activateNbOars(oars, nb, new ArrayList<>());

    }

    List<SailorAction> activateNbOars(List<Oar> oars, int nb, List<Marine> yetBusy) {
        // Use the isDoneTurn method and a list to know if a sailor is available or not
        // but do not set value doneTurn only methods in Captain will do that
        List<SailorAction> result = new ArrayList<>();
        int compteur = 0;
        Map<Oar, List<Marine>> correspondances = this.marinsDisponiblesForOar();
        for (Oar oar : oars) {
            if (correspondances.get(oar) != null) {
                for (Marine m : correspondances.get(oar)) {
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
        if (nb > 0) {
            List<SailorAction> actions = new ArrayList<>();
            do {
                actions = this.activateOarsOnRight(nb);
                nb--;
            } while (actions.isEmpty());

            return actions;
        } else {
            nb = -nb;
            List<SailorAction> actions = new ArrayList<>();
            do {
                actions = this.activateOarsOnLeft(nb);
                nb--;

            } while (actions.isEmpty());

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

        List<Oar> freeOarsOnLeftSide = new ArrayList<>(equipmentManager.unusedLeftOars());
        List<Oar> freeOarsOnRightSide = new ArrayList<>(equipmentManager.unusedRightOars());

        List<Marine> availableSailors = crew.getAvailableSailors();

        // Can't assign to non-free oars
        // Can't assign when no sailors available
        if (freeOarsOnLeftSide.isEmpty() || freeOarsOnRightSide.isEmpty() || availableSailors.isEmpty())
            return List.of();

        Optional<Marine> leftSailor = Optional.empty();
        Optional<Marine> rightSailor = Optional.empty();

        Oar currLeftOar = null;
        Oar currRightOar = null;

        // Try to find someone who can oar on left side
        while (!leftSailor.isPresent() && !freeOarsOnLeftSide.isEmpty()) {
            currLeftOar = freeOarsOnLeftSide.get(0);
            freeOarsOnLeftSide.remove(currLeftOar);

            // TODO: 15/02/2020 suggestion : method Crew: Optional<Marine>
            // getClosestInReachOf(IntPosition, List<Marine>)
            IntPosition targetPos = currLeftOar.getPosition();
            List<Marine> sailorsInReach = crew.getSailorsWhoCanReach(availableSailors, targetPos);
            leftSailor = crew.marineClosestTo(targetPos, sailorsInReach);
        }

        if (leftSailor.isPresent()) {
            // Cannot be used on both sides
            availableSailors.remove(leftSailor.get());

            // Try to find someone who can oar on right side
            while (!rightSailor.isPresent() && !freeOarsOnRightSide.isEmpty()) {
                currRightOar = freeOarsOnRightSide.get(0);
                freeOarsOnRightSide.remove(currRightOar);

                IntPosition targetPos = currRightOar.getPosition();
                List<Marine> sailorsInReach = crew.getSailorsWhoCanReach(availableSailors, targetPos);
                rightSailor = crew.marineClosestTo(targetPos, sailorsInReach);
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
        this.crew.resetAvailability();
        this.equipmentManager.resetUsedStatus();;
    }

    public Optional<Marine> getMarinById(int id) {
        return this.crew.getMarinById(id);
    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à gauche
     * 
     * @param equimentManager
     * @return
     */
    public List<Marine> leftSailorsOnOars() {
        List<Marine> marines = new ArrayList<>();
        for (Oar oar : equipmentManager.allLeftOars()) {
            Optional<Marine> theSailor = this.crew.marineAtPosition(oar.getPosition());
            if (theSailor.isPresent()) {
                marines.add(theSailor.get());
            }
        }

        return marines;

    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à droite
     * 
     * @param equimentManager
     * @return
     */
    public List<Marine> rightSailorsOnOars() {
        List<Marine> marines = new ArrayList<>();
        for (Oar oar : equipmentManager.allRightOars()) {
            Optional<Marine> theSailor = this.crew.marineAtPosition(oar.getPosition());
            if (theSailor.isPresent()) {
                marines.add(theSailor.get());
            }
        }

        return marines;
    }

    /**
     * Method used to know if we can lift or lower all of the sails.
     * 
     * @param isOpened - true if we want to lower them, false if we want to lift
     *                 them.
     * @return true or false
     */

    private boolean canActOnSails(boolean isOpened) {
        List<Sail> sails = equipmentManager.sails(isOpened);
        Map<Equipment, List<Marine>> correspondances = marinsDisponiblesVoiles(isOpened);
        for (Sail sail : sails) {
            if (correspondances.get(sail).isEmpty()) {
                return false;
            }
            Marine firstSailor = correspondances.get(sail).get(0);
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

    // TODO
    public List<SailorAction> actionsToLiftSails() {
        return List.of();
    }

    // TODO
    public List<SailorAction> actionsToLowerSails() {
        return List.of();
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

            var optMarin = this.crew.getMarinById(oa.getSailorId());

            if (optMarin.isPresent()) {

                Marine marine = optMarin.get();

                var eq = this.equipmentManager.equipmentAt(marine.getPosition());

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
        this.crew.executeMoves(moves);
        this.markEquipmentUsedByActions(otherActions);

    }

}