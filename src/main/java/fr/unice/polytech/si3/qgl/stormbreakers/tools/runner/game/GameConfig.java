package fr.unice.polytech.si3.qgl.stormbreakers.tools.runner.game;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.*;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.runner.serializing.GameConfigSerializer;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.runner.serializing.Ship;

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

    public List<Reef> getReefs() {
        return entities.stream()
                .filter(ent -> OceanEntityType.REEF.equals(ent.getEnumType()))
                .map(ent -> (Reef) ent)
                .collect(Collectors.toList());
    }

    public List<Stream> getStreams() {
        return entities.stream()
                .filter(ent -> OceanEntityType.STREAM.equals(ent.getEnumType()))
                .map(ent -> (Stream) ent)
                .collect(Collectors.toList());
    }
}
