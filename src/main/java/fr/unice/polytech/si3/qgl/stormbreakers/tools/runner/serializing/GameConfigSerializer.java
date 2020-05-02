package fr.unice.polytech.si3.qgl.stormbreakers.tools.runner.serializing;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.runner.game.GameConfig;

import java.io.IOException;
import java.util.List;

public class GameConfigSerializer extends JsonDeserializer<GameConfig> {
    private static final String GOAL_KEY = "goal";
    private static final String CHECKPOINTS_KEY = "checkpoints";
    private static final String SHIP_KEY = "ship";
    private static final String WIND_KEY = "wind";
    private static final String ENTITIES_KEY = "seaEntities";

    @Override
    public GameConfig deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode root = jp.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);

        Ship ship = mapper.readValue( root.get(SHIP_KEY).toString() , Ship.class);
        List<Checkpoint> checkpoints = mapper.readValue( root.get(GOAL_KEY).get(CHECKPOINTS_KEY).toString() , new TypeReference<List<Checkpoint>>(){});
        List<OceanEntity> entities = mapper.readValue( root.get(ENTITIES_KEY).toString() , new TypeReference<List<OceanEntity>>(){});
        Wind wind = mapper.readValue( root.get(WIND_KEY).toString(), Wind.class);

        return new GameConfig(checkpoints,ship,wind,entities);
    }
}






