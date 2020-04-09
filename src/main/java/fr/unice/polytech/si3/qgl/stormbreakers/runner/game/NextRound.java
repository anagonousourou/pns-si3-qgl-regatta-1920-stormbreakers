package fr.unice.polytech.si3.qgl.stormbreakers.runner.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;
import fr.unice.polytech.si3.qgl.stormbreakers.runner.serializing.Ship;

import java.util.List;

public class NextRound {
    private Ship ship;
    private Wind wind;
    private List<OceanEntity> visibleEntities;

    @JsonCreator
    public NextRound(
            @JsonProperty("ship") Ship ship,
            @JsonProperty("wind") Wind wind,
            @JsonProperty("visibleEntities") List<OceanEntity> visibleEntities
    ) {
        this.ship = ship;
        this.wind = wind;
        this.visibleEntities = visibleEntities;
    }

    @JsonProperty("ship")
    public Ship getShip() {
        return ship;
    }

    @JsonProperty("wind")
    public Wind getWind() {
        return wind;
    }

    @JsonProperty("visibleEntities")
    public List<OceanEntity> getVisibleEntities() {
        return visibleEntities;
    }

}
