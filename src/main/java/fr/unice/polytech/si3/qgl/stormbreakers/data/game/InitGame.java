package fr.unice.polytech.si3.qgl.stormbreakers.data.game;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public InitGame(
            @JsonProperty("goal") Goal goal,
            @JsonProperty("ship") Bateau ship,
            @JsonProperty("sailors") List<Marin> sailors,
            @JsonProperty("shipCount") int shipCount
    ) {
    	this.goal=goal;
    	this.ship=ship;
    	this.sailors=sailors;
    	this.shipCount=shipCount;
    }

    @JsonProperty("goal")
    public Goal getGoal() {
        return goal;
    }

    @JsonProperty("ship")
    public Bateau getShip() {
        return ship;
    }

    @JsonProperty("sailors")
    public List<Marin> getSailors() {
        return sailors;
    }

    @JsonProperty("shipCount")
    public int getShipCount() {
        return shipCount;
    }

}
