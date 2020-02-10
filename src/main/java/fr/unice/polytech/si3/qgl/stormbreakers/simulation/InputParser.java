package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

/**
 * This class is used to parse the JSON formatted String
 */

public class InputParser {

	ObjectMapper mapper= new ObjectMapper();
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

	public int fetchWidth(String jString) throws JsonMappingException, JsonProcessingException {
		var mapper=new ObjectMapper();
		return mapper.readTree(jString).get("ship").get("deck").get("width").asInt();
	}

	public List<MoveAction> fetchMoves(String jString) throws JsonMappingException, JsonProcessingException {
		var mapper=new ObjectMapper();
		List<MoveAction> moves=new ArrayList<>();
		mapper.readTree(jString).forEach(action->{
			if(action.get("type").asText().equals("MOVING") ){
				moves.add(new MoveAction(action.get("sailorId").asInt(), action.get("xdistance").asInt(),action.get("ydistance").asInt() ));
			}
		});

		return moves;
	}

	public List<OarAction> fetchOarActions(String jString) throws JsonMappingException, JsonProcessingException {
		var mapper=new ObjectMapper();
		List<OarAction> actions =new ArrayList<>();
		mapper.readTree(jString).forEach(action->{
			if(action.get("type").asText().equals("OAR") ){
				actions.add(new OarAction(action.get("sailorId").asInt()));
			}
		});

		return actions;
	}
	/**
	 * Return the list of checkpoint in the json string
	 * @param jString
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public List<Checkpoint> fetchCheckpoints(String jString) throws JsonMappingException, JsonProcessingException {
		return null;
	}
	public List<Equipment> fetchEquipments(String jString) throws JsonMappingException, JsonProcessingException {
		List<Equipment> equipments = new ArrayList<>();

		mapper.readTree(jString).get("ship").get("entities").forEach(e -> {

			Equipment equipment = null;

			if(e.get("type").asText().equals("oar")) {

				equipment = new Oar(e.get("x").asInt(), e.get("y").asInt());

			} else if(e.get("type").asText().equals("rudder")){

				equipment = new Gouvernail(e.get("x").asInt(), e.get("y").asInt());

			} else {

				//TODO Voile/Vigie si besoin

			}

			equipments.add(equipment);

		});

		return equipments;
	}
	public Position fetchBoatPosition(String jString) throws JsonMappingException, JsonProcessingException{

		JsonNode positionNode=mapper.readTree(jString).get("ship").get("position");
		return new Position(positionNode.get("x").asDouble() , positionNode.get("y").asDouble(),
		positionNode.get("orientation").asDouble());

	}


}
