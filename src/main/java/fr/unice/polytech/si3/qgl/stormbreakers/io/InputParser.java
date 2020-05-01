package fr.unice.polytech.si3.qgl.stormbreakers.io;

import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Stream;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.ParsingException;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;

public interface InputParser {

	List<Sailor> fetchAllSailors(String jsonInput) throws ParsingException;

	List<Oar> fetchAllOars(String jString) throws ParsingException;

	/**
	 * Return the list of checkpoint in the json string
	 * 
	 * @param jString
	 * @return
	 * @throws ParsingException
	 */
	List<Checkpoint> fetchCheckpoints(String jString) throws ParsingException;

	List<Equipment> fetchEquipments(String jString) throws ParsingException;
	// ----

	Position fetchBoatPosition(String jString) throws ParsingException;

	Shape fetchBoatShape(String jString) throws ParsingException;

	int fetchBoatLength(String jString) throws ParsingException;

	int fetchBoatLife(String jString) throws ParsingException;

	int fetchBoatWidth(String jString) throws ParsingException;

	double fetchWindStrength(String jString) throws ParsingException;

	double fetchWindOrientation(String jString) throws ParsingException;

	/**
	 * Renvoie les courants LATER
	 * 
	 * @param jString
	 * @return
	 * @throws ParsingException
	 */

	List<Stream> fetchStreams(String jString) throws ParsingException;

	/**
	 * Renvoie les courants LATER
	 * 
	 * @param jString
	 * @return
	 * @throws ParsingException
	 */

	List<OceanEntity> fetchOceanEntities(String jString) throws ParsingException;

}