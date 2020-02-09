package fr.unice.polytech.si3.qgl.stormbreakers.data.game;

import java.util.List;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.ActionType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.RegattaGoal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Vent;

/**
 * Classe representant l'etat du jeu au debut du tour courant
 */

public class GameState {

    private Bateau ship;
    private List<Marin> orgaMarins;
    private List<Checkpoint> checkpoints;
    private InitGame stateInit;

    private Vent wind;

    GameState(Bateau ship, List<Marin> orgaMarins, List<Checkpoint> checkpoints) {
        this.ship = ship;
        this.orgaMarins = orgaMarins;
        this.checkpoints = checkpoints;
        this.wind = null;
    }

    /**
     * Constructeur qui recupere l'etat initial du jeu
     * 
     * @param initGame contient les donnees d'initialisation
     */
    public GameState(InitGame initGame) {
        this.ship = initGame.getShip();
        this.orgaMarins = initGame.getSailors();
        this.wind = new Vent(0.0, 0.0);
        this.stateInit = initGame;

        if (initGame.getGoal().getMode().equals("REGATTA")) {
            RegattaGoal regattaGoal = (RegattaGoal) initGame.getGoal();
            this.checkpoints = regattaGoal.getCheckpoints();
        }
    }

    /**
     * Actualise l'etat du jeu NB : ne prend pas en compte les actions realisees
     * 
     * @param nextRound contient les donnees actualisees
     */
    public void updateTurn(NextRound nextRound) {
        ship = nextRound.getShip();
        wind = nextRound.getWind();
        if (checkpoints != null)
            actualiserCheckpoints();
    }

    public void actualiserCheckpoints() {
        if (isCurrentCheckpointOk()) {
            validateCheckpoint();
        }
    }

    /**
     * Actualise l'etat du jeu, a partir d'une liste de deplacement
     * 
     * @param moves liste de deplacement des marins
     */
    void actualiserDeplacements(List<MoveAction> moves) {
        for (MoveAction move : moves) {
            Marin sailor = findSailorById(move.getSailorId());
            move.applyTo(sailor);
        }
    }

    /**
     * Actualise l'etat du jeu, a partir d'une liste d'actions
     * 
     * @param actions liste d'actions
     */
    public void actualiserActions(List<SailorAction> actions) {
        // Traitement des deplacements
        List<SailorAction> movingList = actions.stream()
                .filter(act -> act.getType().equals(ActionType.MOVING.actionCode)).collect(Collectors.toList());
        List<MoveAction> movings = movingList.stream().map(x -> (MoveAction) x).collect(Collectors.toList());
        this.actualiserDeplacements(movings);
    }

    private Marin findSailorById(int sailorId) {
        for (Marin sailor : orgaMarins) {
            if (sailor.getId() == sailorId)
                return sailor;
        }
        return null;
    }

    public Bateau getShip() {
        return ship;
    }

    public Position getPositionBateau() {
        return ship.getPosition();
    }

    public int getVieBateau() {
        return ship.getLife();
    }

    public List<Equipment> getEquipmentState() {
        return ship.getEquipments();
    }

    public List<Marin> getOrgaMarins() {
        return orgaMarins;
    }

    public Vent getWind() {
        return wind;
    }

    public Checkpoint getNextCheckpoint() {
        if (checkpoints == null || checkpoints.isEmpty())
            return null;
        return checkpoints.get(0);
    }

    private void validateCheckpoint() {
        if (!checkpoints.isEmpty()) {
            checkpoints.remove(0);
        }

    }

    private boolean isCurrentCheckpointOk() {
        return isCheckpointOk(getNextCheckpoint());
    }

    private boolean isCheckpointOk(Checkpoint checkPt) {
        if (checkPt == null) {
            return true;
        }

        return checkPt.isPosInside(getPositionBateau());
    }

    public InitGame getStateInit() {
        return stateInit;
    }
}
