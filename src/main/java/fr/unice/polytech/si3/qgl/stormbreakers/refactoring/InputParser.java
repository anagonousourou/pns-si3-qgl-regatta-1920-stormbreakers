package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

/**
 * This class is used to parse the JSON formatted String
 */

public class InputParser {
	ObjectMapper mapper = new ObjectMapper();

	public List<Marine> fetchAllSailors(String jsonInput) throws JsonMappingException, JsonProcessingException {
		List<Marine> marins = new ArrayList<>();
		mapper.readTree(jsonInput).get("sailors").forEach(s -> {
			marins.add(new Marine(s.get("id").asInt(), s.get("x").asInt(), s.get("y").asInt()));
		});
		return marins;
	}

	public List<Oar> fetchAllOars(String jString) throws JsonMappingException, JsonProcessingException {
		List<Oar> oars = new ArrayList<>();
		mapper.readTree(jString).get("ship").get("entities").forEach(s -> {
			oars.add(new Oar(s.get("x").asInt(), s.get("y").asInt()));
		});

		return oars;
	}

	public int fetchWidth(String jString) throws JsonMappingException, JsonProcessingException {
		return mapper.readTree(jString).get("ship").get("deck").get("width").asInt();
	}

	

	/**
	 * Return the list of checkpoint in the json string
	 * 
	 * @param jString
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public List<Checkpoint> fetchCheckpoints(String jString) throws JsonMappingException, JsonProcessingException {
		//TODO
		return List.of();
	}

	public List<Equipment> fetchEquipments(String jString) throws JsonMappingException, JsonProcessingException {
		//TODO 
		return List.of();
	}

	public Boat fetchBoat(String jString) throws JsonMappingException, JsonProcessingException {
		JsonNode result = mapper.readTree(jString).get("ship");

		int width = result.get("deck").get("width").asInt();
		int lenght = result.get("deck").get("length").asInt();
		int life = result.get("life").asInt();
		Position position = new Position(result.get("position").get("x").asDouble(),
				result.get("position").get("y").asDouble(), result.get("position").get("orientation").asDouble());

		return new Boat(position, lenght, width, life);

	}

}
