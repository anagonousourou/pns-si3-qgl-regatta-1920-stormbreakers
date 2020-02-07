package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is used to parse the JSON formatted String
 */

class InputParser {

	public List<Marine> fetchAllSailors(String jsonInput) throws JsonMappingException, JsonProcessingException {
		List<Marine> marins=new ArrayList<>();
		var mapper=new ObjectMapper();
		mapper.readTree(jsonInput).get("sailors").forEach(s->{
			marins.add(new Marine(
				s.get("id").asInt(),
				s.get("x").asInt(),
				s.get("y").asInt()
			) );
		});
		return marins;
	}

	public List<Oar> fetchAllOars(String jString) throws JsonMappingException, JsonProcessingException {
		List<Oar> oars=new ArrayList<>();
		var mapper=new ObjectMapper();
		mapper.readTree(jString).get("ship").get("entities").forEach(s->{
			oars.add(new Oar(
				s.get("x").asInt(),
				s.get("y").asInt()
			));
		});

		return oars;
	}

	int fetchWidth(String jString) throws JsonMappingException, JsonProcessingException {
		var mapper=new ObjectMapper();
		return mapper.readTree(jString).get("ship").get("deck").get("width").asInt();
	}

	List<MoveAction> fetchMoves(String jString) throws JsonMappingException, JsonProcessingException {
		var mapper=new ObjectMapper();
		List<MoveAction> moves=new ArrayList<>();
		mapper.readTree(jString).forEach(action->{
			if(action.get("type").asText().equals("MOVING") ){
				moves.add(new MoveAction(action.get("sailorId").asInt(), action.get("xdistance").asInt(),action.get("ydistance").asInt() ));
			}
		});

		return moves;
	}

}
