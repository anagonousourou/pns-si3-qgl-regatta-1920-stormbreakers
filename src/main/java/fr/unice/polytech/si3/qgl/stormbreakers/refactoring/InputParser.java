package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

/**
 * This class is used to parse the JSON formatted String
 */

public class InputParser {
	ObjectMapper mapper = new ObjectMapper();
	private final String shapeKey="shape";
	private final String idKey="id"; 
	private final String shipKey="ship";
	private final String entitiesKey="entities";
	private final String checkpointsKey="checkpoints";
	private final String sailorsKey="sailors";
	private final String xKey="x";
	private final String yKey="y";

	public List<Marine> fetchAllSailors(String jsonInput) throws JsonProcessingException {
		List<Marine> marins = new ArrayList<>();
		mapper.readTree(jsonInput).get(sailorsKey).forEach(s -> {
			marins.add(new Marine(s.get(idKey).asInt(), s.get(xKey).asInt(), s.get(yKey).asInt()));
		});
		return marins;
	}

	public List<Oar> fetchAllOars(String jString) throws JsonProcessingException {
		List<Oar> oars = new ArrayList<>();
		mapper.readTree(jString).get(shipKey).get(entitiesKey).forEach(s -> {
			oars.add(new Oar(s.get(xKey).asInt(), s.get(yKey).asInt()));
		});

		return oars;
	}

	

	/**
	 * Return the list of checkpoint in the json string
	 * 
	 * @param jString
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public List<Checkpoint> fetchCheckpoints(String jString) throws JsonProcessingException {
		List<Checkpoint> checkpoints = new ArrayList<>();
		mapper.readTree(jString).get("goal").get(checkpointsKey).forEach(c -> {
			Shape shape;
			if (c.get(shapeKey).get("type").asText().equals("circle")) {
				shape = new Circle(c.get(shapeKey).get("radius").asDouble());
			} else {
				shape = new Rectangle(c.get(shapeKey).get("width").asDouble(), c.get("shape").get("height").asDouble(),
						c.get("shape").get("orientation").asDouble());
			}
			checkpoints.add(new Checkpoint(
					new Position(c.get("position").get("x").asInt(), c.get("position").get("y").asInt()), shape));
		});
		return checkpoints;
	}

	public List<Equipment> fetchEquipments(String jString) throws JsonProcessingException {
		List<Equipment> equipments = new ArrayList<>();
		mapper.readTree(jString).get("ship").get("entities").forEach(e -> {
			Equipment equipment = null;
			if (e.get("type").asText().equals("oar")) {
				equipment = new Oar(e.get("x").asInt(), e.get("y").asInt());
			} else if (e.get("type").asText().equals("rudder")) {
				equipment = new Gouvernail(e.get("x").asInt(), e.get("y").asInt());
			} else if (e.get("type").asText().equals("sail")) {
				equipment = new Sail(e.get("x").asInt(), e.get("y").asInt(), e.get("openned").asBoolean());

			} else {
				//Vigie plus tard
			}
			equipments.add(equipment);
		});
		return equipments;
	}

	public Position fetchBoatPosition(String jString) throws JsonProcessingException{
		JsonNode result = mapper.readTree(jString).get("ship");
		return new Position(result.get("position").get("x").asDouble(),
		result.get("position").get("y").asDouble(), result.get("position").get("orientation").asDouble());
	}

	public int fetchBoatLength(String jString) throws JsonProcessingException{
		return mapper.readTree(jString).get("ship").get("deck").get("length").asInt();
	}
	

	public int fetchBoatLife(String jString) throws JsonProcessingException{
		JsonNode result = mapper.readTree(jString).get("ship");
		return result.get("life").asInt();
	}

	public int fetchBoatWidth(String jString) throws JsonProcessingException {
		return mapper.readTree(jString).get("ship").get("deck").get("width").asInt();
	}


	public double fetchWindStrength(String jString) throws JsonProcessingException{
		JsonNode result = mapper.readTree(jString).get("wind"); 
		if (result == null) {
			return 0.0;
		} else {
			return  result.get("strength").asDouble();
		}
	}

	public double fetchWindOrientation(String jString) throws JsonProcessingException{
		JsonNode result = mapper.readTree(jString).get("wind"); 
		if (result == null) {
			return 0.0;
		} else {
			return  result.get("orientation").asDouble();
		}
	}

}
