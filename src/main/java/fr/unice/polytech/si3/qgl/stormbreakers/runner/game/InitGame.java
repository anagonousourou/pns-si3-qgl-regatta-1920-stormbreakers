package fr.unice.polytech.si3.qgl.stormbreakers.runner.game;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Goal;
import fr.unice.polytech.si3.qgl.stormbreakers.runner.serializing.InitGameDeserializer;
import fr.unice.polytech.si3.qgl.stormbreakers.runner.serializing.Ship;

/**
 * Classe representant les parametres d'initialisation d'une partie
 */

@JsonDeserialize(using = InitGameDeserializer.class)
public class InitGame {

    private Goal goal;
    private Ship ship;
    private List<Equipment> equipments;
    private List<Sailor> sailors;
    private int shipCount;

    public InitGame(
            Goal goal,
            Ship ship,
            List<Equipment> equipments,
            List<Sailor> sailors,
            int shipCount
    ) {
        this.goal=goal;
        this.ship=ship;
        this.equipments=equipments;
        this.sailors=sailors;
        this.shipCount=shipCount;
    }

    public Goal getGoal() {
        return goal;
    }

    public List<Sailor> getSailors() {
        return sailors;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public int getBoatCount() {
        return shipCount;
    }

    public Ship getShip() {
        return ship;
    }
}

