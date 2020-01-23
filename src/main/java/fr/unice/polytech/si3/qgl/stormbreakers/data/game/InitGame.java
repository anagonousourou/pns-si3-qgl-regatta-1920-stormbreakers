package fr.unice.polytech.si3.qgl.stormbreakers.data.game;

import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Goal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;

/**
 * Classe representant les parametres d'initialisation d'une partie
 */

public class InitGame {
    private Goal goal;
    private Bateau ship;
    private List<Marin> sailors;
    private int shipCount;
    
    public InitGame(Goal goal, Bateau ship, List<Marin> sailors, int shipCount) {
    	this.goal=goal;
    	this.ship=ship;
    	this.sailors=sailors;
    	this.shipCount=shipCount;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Bateau getShip() {
        return ship;
    }

    public void setShip(Bateau ship) {
        this.ship = ship;
    }

    public List<Marin> getSailors() {
        return sailors;
    }

    public void setSailors(List<Marin> sailors) {
        this.sailors = sailors;
    }

    public int getShipCount() {
        return shipCount;
    }

    public void setShipCount(int shipCount) {
        this.shipCount = shipCount;
    }
}
