package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.unice.polytech.si3.qgl.stormbreakers.data.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.NextRound;

/**
 * This class is used to parse the JSON formatted String
 */

public class InputParser {

	/**
	 * Cree un objet contenant les donnees d'initialisation de la partie
	 * 
	 * @param jsonInput donnes d'initialisation au format JSON
	 * @return InitGame
	 */
	public InitGame fetchInitGameState(String jsonInput) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonInput, InitGame.class);
		} catch (JsonProcessingException e) {

			Logger.getInstance().logLine(e.getMessage());
		}
		return null;
	}

	public NextRound fetchNextRoundState(String jsonInput) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonInput, NextRound.class);
		} catch (JsonProcessingException e) {
			Logger.getInstance().logLine(e.getMessage());
			
		}
		return null;
	}

}
