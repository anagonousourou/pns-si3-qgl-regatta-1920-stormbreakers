package fr.unice.polytech.si3.qgl.stormbreakers.data.game;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Moving;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
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

    private Vent wind;


    GameState(Position positionBateau, int vieBateau, List<Marin> orgaMarins, List<Equipment> equipmentState) {
        this.positionBateau = positionBateau;
        this.vieBateau = vieBateau;
        this.orgaMarins = orgaMarins;
        this.equipmentState = equipmentState;
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
    }

    /**
     * Actualise l'etat du jeu
     * NB : ne prend pas en compte les actions realisees
     * @param nextRound contient les donnees actualisees
     */
    void actualiser(NextRound nextRound) {
        positionBateau = nextRound.getShip().getPosition();
        vieBateau = nextRound.getShip().getLife();
        equipmentState = nextRound.getShip().getEquipments();
        wind = nextRound.getWind();
    }

    /**
     * Actualise l'etat du jeu, a partir d'une liste de deplacement
     * @param moves liste de deplacement des marins
     */
    void actualiser(List<Moving> moves) {
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



}
