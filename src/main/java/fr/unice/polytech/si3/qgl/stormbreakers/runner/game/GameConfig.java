package fr.unice.polytech.si3.qgl.stormbreakers.runner.game;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.*;
import fr.unice.polytech.si3.qgl.stormbreakers.runner.serializing.GameConfigSerializer;
import fr.unice.polytech.si3.qgl.stormbreakers.runner.serializing.Ship;

import java.util.List;
import java.util.stream.Collectors;

@JsonDeserialize(using = GameConfigSerializer.class)
public class GameConfig {
    private List<Checkpoint> checkpoints;
    private Ship ship;
    private Wind wind;
    private List<OceanEntity> entities;

    public GameConfig(List<Checkpoint> checkpoints, Ship ship, Wind wind, List<OceanEntity> entities) {
        this.checkpoints = checkpoints;
        this.ship = ship;
        this.wind = wind;
        this.entities = entities;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public List<OceanEntity> getEntities() {
        return entities;
    }

    public Wind getWind() {
        return wind;
    }

    public Ship getShip() {
        return ship;
    }

    public List<Recif> getReefs() {
        return entities.stream()
                .filter(ent -> OceanEntityType.RECIF.equals(ent.getEnumType()))
                .map(ent -> (Recif) ent)
                .collect(Collectors.toList());
    }

    public List<Courant> getStreams() {
        return entities.stream()
                .filter(ent -> OceanEntityType.COURANT.equals(ent.getEnumType()))
                .map(ent -> (Courant) ent)
                .collect(Collectors.toList());
    }
}
