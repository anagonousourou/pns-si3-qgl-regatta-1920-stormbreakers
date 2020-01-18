package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import fr.unice.polytech.si3.qgl.stormbreakers.data.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.NextRound;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Goal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.RegattaGoal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;

/**
 * This class is used to parse the JSON formatted String
 */

public class InputParser {
    
    public InitGame fetchInitGameState(String jsonInput) {
    	/**
    	 * 
    	 */
    	try {
			jsonInput = new String(this.getClass().getResourceAsStream("/init.json").readAllBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    ObjectMapper objectMapper = new ObjectMapper();
		JsonNode result;
		try {
			final JsonNodeFactory factory = JsonNodeFactory.instance;
			ArrayNode array=factory.arrayNode();
			
			result = objectMapper.readTree(jsonInput);
			
			List <Marin> marins=createListeMarin(result,objectMapper);
			Goal goal = createGoal(result,objectMapper);
			int shipCount= objectMapper.readValue(result.get("shipCount").toPrettyString(),Integer.class);
			Bateau bateau = createBateau(result,objectMapper);
			return new InitGame(goal,bateau,marins,shipCount);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }



	public NextRound fetchNextRoundState(String jsonInput) {
        return null;
    }
    


	/** object Creator **/
	
	
    private Bateau createBateau(JsonNode result, ObjectMapper objectMapper) {
		// TODO Auto-generated method stub
    	try {
    		return objectMapper.readValue(result.get("ship").toPrettyString(),Bateau.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
	}
    
	
	private Goal createGoal(JsonNode result, ObjectMapper objectMapper) {
    	
    	Goal goal;
		try {
			goal = objectMapper.readValue(result.get("goal").toPrettyString(),Goal.class);
			List<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
			if(goal.getMode().equals("REGATA")) {
				
				result.get("checkpoints").forEach(r->{
					try {
						Checkpoint checkpoint= objectMapper.readValue(r.toPrettyString(),Checkpoint.class);
						checkpoints.add(checkpoint);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				});
				goal= new RegattaGoal(checkpoints);
			}
			return goal;
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
	}

	private List<Marin> createListeMarin(JsonNode result, ObjectMapper objectMapper) {
    	List<Marin> marins;
		try {
			return objectMapper.readValue(result.get("sailors").toPrettyString(),new TypeReference<List<Marin>>(){});
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
