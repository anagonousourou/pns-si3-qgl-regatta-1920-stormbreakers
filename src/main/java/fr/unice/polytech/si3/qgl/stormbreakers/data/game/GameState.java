package fr.unice.polytech.si3.qgl.stormbreakers.data.game;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Moving;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.RegattaGoal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Vent;

import java.util.List;

/**
 * Classe representant l'etat du jeu au debut du tour courrant
 */



public class GameState {

    private Position positionBateau;
    private int vieBateau;
    private List<Marin> orgaMarins;
    private List<Equipment> equipmentState;

    private List<Checkpoint> checkpoints;

    private Vent wind;


    GameState(Position positionBateau, int vieBateau, List<Marin> orgaMarins, List<Equipment> equipmentState, List<Checkpoint> checkpoints) {
        this.positionBateau = positionBateau;
        this.vieBateau = vieBateau;
        this.orgaMarins = orgaMarins;
        this.equipmentState = equipmentState;
        this.checkpoints = checkpoints;
        this.wind = null;
    }

    // TODO: 23/01/2020 Recup gameSate depuis InitGame ???
    /**
     * Constructeur qui recupere l'etat initial du jeu
     * @param initGame contient les donnees d'initialisation
     */
    GameState(InitGame initGame) {
        this.positionBateau = initGame.getShip().getPosition();
        this.vieBateau = initGame.getShip().getLife();
        this.orgaMarins = initGame.getSailors();
        this.equipmentState = initGame.getShip().getEquipments();
        this.wind = new Vent(0.0,0.0);

        if (initGame.getGoal().getMode().equals("REGATTA")) {
            RegattaGoal regattaGoal = (RegattaGoal) initGame.getGoal();
            checkpoints = regattaGoal.getCheckpoints();
        }
    }

    /**
     * Actualise l'etat du jeu
     * NB : ne prend pas en compte les actions realisees
     * @param nextRound contient les donnees actualisees
     */
    void actualiserTour(NextRound nextRound) {
        positionBateau = nextRound.getShip().getPosition();
        vieBateau = nextRound.getShip().getLife();
        equipmentState = nextRound.getShip().getEquipments();
        wind = nextRound.getWind();
        if (checkpoints!=null) actualiserCheckpoints();
    }

    void actualiserCheckpoints() {
        if (isCheckpointOk(getNextCheckpoint())) validateCheckpoint();
    }

    /**
     * Actualise l'etat du jeu, a partir d'une liste de deplacement
     * @param moves liste de deplacement des marins
     */
    void actualiserDeplacements(List<Moving> moves) {
        for (Moving move : moves) {
            Marin sailor = findSailorById(move.getSailorId());
            move.applyTo(sailor);
        }
    }

    private Marin findSailorById(int sailorId) {
        for (Marin sailor : orgaMarins) {
            if (sailor.getId() == sailorId) return sailor;
        }
        return null;
    }

	public Position getPositionBateau() {
		return positionBateau;
	}

	public int getVieBateau() {
		return vieBateau;
	}

	public List<Equipment> getEquipmentState() {
		return equipmentState;
	}

	public Vent getWind() {
		return wind;
	}


    public Checkpoint getNextCheckpoint() {
        if (checkpoints==null || checkpoints.size()==0) return null;
        return checkpoints.get(0);
    }

    private void validateCheckpoint() {
        checkpoints.remove(0);
    }

    private boolean isCheckpointOk(Checkpoint checkPt) {
        return checkPt.isPosInside(positionBateau.getX(),positionBateau.getY());

    }

    public List<Marin> getOrgaMarins() {
        return orgaMarins;
    }
}
