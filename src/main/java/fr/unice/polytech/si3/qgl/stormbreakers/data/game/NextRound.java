package fr.unice.polytech.si3.qgl.stormbreakers.data.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Vent;

import java.util.List;

public class NextRound {
    private Bateau ship;
    private Vent wind;
    private List<OceanEntity> visibleEntities;

    @JsonCreator
    NextRound(
            @JsonProperty("ship") Bateau ship,
            @JsonProperty("wind") Vent wind,
            @JsonProperty("visibleEntities") List<OceanEntity> visibleEntities
    ) {
        this.ship = ship;
        this.wind = wind;
        this.visibleEntities = visibleEntities;
    }

    @JsonProperty("ship")
    public Bateau getShip() {
        return ship;
    }

    @JsonProperty("wind")
    public Vent getWind() {
        return wind;
    }

    @JsonProperty("visibleEntities")
    public List<OceanEntity> getVisibleEntities() {
        return visibleEntities;
    }
}
