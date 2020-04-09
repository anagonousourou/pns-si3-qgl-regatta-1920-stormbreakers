package fr.unice.polytech.si3.qgl.stormbreakers.runner.serializing;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Goal;
import fr.unice.polytech.si3.qgl.stormbreakers.runner.game.InitGame;

import java.io.IOException;
import java.util.List;

public class InitGameDeserializer extends JsonDeserializer<InitGame> {
    private static final String GOAL_KEY = "goal";
    private static final String SHIP_KEY = "ship";
    private static final String SAILORS_KEY = "sailors";
    private static final String SHIPCOUNT_KEY = "shipCount";

    @Override
    public InitGame deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode root = jp.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
        Goal goal = mapper.readValue( root.get(GOAL_KEY).toString() , Goal.class);
        Ship ship = mapper.readValue( root.get(SHIP_KEY).toString() , Ship.class);
        List<Equipment> equipments = ship.getEquipments();
        List<Sailor> sailors = mapper.readValue( root.get(SAILORS_KEY).toString() , new TypeReference<List<Sailor>>(){});
        int shipCount = root.get(SHIPCOUNT_KEY).asInt();

        return new InitGame(goal,ship,equipments,sailors,shipCount);
    }
}
    
    
    
    

