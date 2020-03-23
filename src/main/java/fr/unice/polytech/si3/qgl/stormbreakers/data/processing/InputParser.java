package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

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
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Vigie;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;

/**
 * This class is used to parse the JSON formatted String
 */

public class InputParser {
	private ObjectMapper mapper = new ObjectMapper();
	private static final String SHAPE_KEY = "shape";
	private static final String ID_KEY = "id";
	private static final String SHIP_KEY = "ship";
	private static final String ENTITIES_KEY = "entities";
	private static final String CHECKPOINTS_KEY = "checkpoints";
	private static final String SAILORS_KEY = "sailors";
	private static final String XKEY = "x";
	private static final String YKEY = "y";
	private static final String ORIENTATION_KEY = "orientation";
	private static final String POSITION_KEY = "position";

	public List<Sailor> fetchAllSailors(String jsonInput) throws JsonProcessingException {
		List<Sailor> marins = new ArrayList<>();
		mapper.readTree(jsonInput).get(SAILORS_KEY)
				.forEach(s -> marins.add(new Sailor(s.get(ID_KEY).asInt(), s.get(XKEY).asInt(), s.get(YKEY).asInt())));
		return marins;
	}

	public List<Oar> fetchAllOars(String jString) throws JsonProcessingException {
		List<Oar> oars = new ArrayList<>();
		mapper.readTree(jString).get(SHIP_KEY).get(ENTITIES_KEY)
				.forEach(s -> oars.add(new Oar(s.get(XKEY).asInt(), s.get(YKEY).asInt())));

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
		mapper.readTree(jString).get("goal").get(CHECKPOINTS_KEY).forEach(c -> {
			Shape shape;
			if (c.get(SHAPE_KEY).get("type").asText().equals("circle")) {
				shape = new Circle(c.get(SHAPE_KEY).get("radius").asDouble());
			} else {
				shape = new Rectangle(c.get(SHAPE_KEY).get("width").asDouble(),
						c.get(SHAPE_KEY).get("height").asDouble(), c.get(SHAPE_KEY).get(ORIENTATION_KEY).asDouble());
			}
			checkpoints.add(new Checkpoint(
					new Position(
							c.get(POSITION_KEY).get("x").asDouble(),
							c.get(POSITION_KEY).get("y").asDouble()),
					shape));
		});
		return checkpoints;
	}

	public List<Equipment> fetchEquipments(String jString) throws JsonProcessingException {
		List<Equipment> equipments = new ArrayList<>();
		mapper.readTree(jString).get(SHIP_KEY).get(ENTITIES_KEY).forEach(e -> {
			Equipment equipment = null;
			if (e.get("type").asText().equals("oar")) {
				equipment = new Oar(e.get(XKEY).asInt(), e.get(YKEY).asInt());
			} else if (e.get("type").asText().equals("rudder")) {
				equipment = new Gouvernail(e.get(XKEY).asInt(), e.get(YKEY).asInt());
			} else if (e.get("type").asText().equals("sail")) {
				equipment = new Sail(e.get(XKEY).asInt(), e.get(YKEY).asInt(), e.get("openned").asBoolean());

			} else if (e.get("type").asText().equals("watch")) {
				equipment = new Vigie(e.get(XKEY).asInt(), e.get(YKEY).asInt());
// ---
			}
			if (equipment != null) {
				equipments.add(equipment);
			}

		});
		return equipments;
	}
// ----

	public Position fetchBoatPosition(String jString) throws JsonProcessingException {
		JsonNode result = mapper.readTree(jString).get(SHIP_KEY);
		return new Position(result.get(POSITION_KEY).get(XKEY).asDouble(),
				result.get(POSITION_KEY).get(YKEY).asDouble(),
				result.get(POSITION_KEY).get(ORIENTATION_KEY).asDouble());
	}

	public Shape fetchBoatShape(String jString) throws JsonProcessingException {
		// LATER: 12/03/2020 Tests ?
		JsonNode shipNode = mapper.readTree(jString).get(SHIP_KEY);
		JsonNode boatShape = shipNode.get(SHAPE_KEY);
		if (boatShape != null) {
			return mapper.readValue(boatShape.toString(), Shape.class);
		}
		return null;

	}

  public int fetchBoatLength(String jString) throws JsonProcessingException {
		return mapper.readTree(jString).get(SHIP_KEY).get("deck").get("length").asInt();
	}

	public int fetchBoatLife(String jString) throws JsonProcessingException {
		JsonNode result = mapper.readTree(jString).get(SHIP_KEY);
		return result.get("life").asInt();
	}

	public int fetchBoatWidth(String jString) throws JsonProcessingException {
		return mapper.readTree(jString).get(SHIP_KEY).get("deck").get("width").asInt();
	}

	public double fetchWindStrength(String jString) throws JsonProcessingException {
		JsonNode result = mapper.readTree(jString).get("wind");
		if (result == null) {
			return 0.0;
		} else {
			return result.get("strength").asDouble();
		}
	}

	public double fetchWindOrientation(String jString) throws JsonProcessingException {
		JsonNode result = mapper.readTree(jString).get("wind");
		if (result == null) {
			return 0.0;
		} else {
			return result.get(ORIENTATION_KEY).asDouble();
		}
	}

	/**
	 * Renvoie les courants LATER
	 * 
	 * @param jString
	 * @return
	 * @throws JsonProcessingException
	 */

	public List<Courant> fetchStreams(String jString) throws JsonProcessingException {

		JsonNode visibleEntities = mapper.readTree(jString).get("visibleEntities");
		if (visibleEntities == null) {
			return List.of();
		} else {
			return mapper.readValue(visibleEntities.toString(),
					mapper.getTypeFactory().constructCollectionType(List.class, Courant.class));
		}
	}

	/**
	 * Renvoie les courants LATER
	 * 
	 * @param jString
	 * @return
	 * @throws JsonProcessingException
	 */

	public List<OceanEntity> fetchOceanEntities(String jString) throws JsonProcessingException {

		JsonNode visibleEntities = mapper.readTree(jString).get("visibleEntities");
		if (visibleEntities == null) {
			return List.of();
		} else {
			return mapper.readValue(visibleEntities.toString(),
					mapper.getTypeFactory().constructCollectionType(List.class, OceanEntity.class));
		}
	}

}
