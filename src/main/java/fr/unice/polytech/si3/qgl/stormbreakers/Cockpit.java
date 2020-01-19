package fr.unice.polytech.si3.qgl.stormbreakers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Goal;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.OutputBuilder;

public class Cockpit implements ICockpit {

	public void initGame(String game) {
		InputParser parser = new InputParser();
		parser.fetchInitGameState(game);
	}

	public String nextRound(String round) {
		System.out.println("Next round input: " + round);

		// TODO: 19/01/2020 Creer action a partir des Marins recuperes

		List<SailorAction> actions = new ArrayList<SailorAction>();
		actions.add(new Oar(0));
		actions.add(new Oar(1));

		OutputBuilder outputBuilder = new OutputBuilder();
		return outputBuilder.writeActions(actions);
	}

	@Override
	public List<String> getLogs() {
		return new ArrayList<>();
	}

	public static void main(String[] args)  {
		Cockpit c=new Cockpit();
		c.initGame("");
		//c.nextRound("");
	}
}
