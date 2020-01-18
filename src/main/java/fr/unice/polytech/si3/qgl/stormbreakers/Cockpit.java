package fr.unice.polytech.si3.qgl.stormbreakers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;

public class Cockpit implements ICockpit {

	private ArrayNode actions;

	public void initGame(String game) {
		try {
			game = new String(this.getClass().getResourceAsStream("/init.json").readAllBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode result;
		try {
			final JsonNodeFactory factory = JsonNodeFactory.instance;
			var array=factory.arrayNode();
			result = objectMapper.readTree(game);
			result.get("sailors").forEach(r->{
				var jn= factory.objectNode();
				jn.set("sailorId", r.get("id"));
				jn.set("type",factory.textNode("OAR"));
				array.add(jn);
			});
			
			
			this.actions=array;
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Init game input: " + game);
	}

	public String nextRound(String round) {
		System.out.println("Next round input: " + round);

		

		//System.out.println(this.actions.toPrettyString());
		return this.actions.toPrettyString();
	}

	@Override
	public List<String> getLogs() {
		return new ArrayList<>();
	}

	public static void main(String[] args) throws IOException {
		Cockpit c=new Cockpit();
		c.initGame("");
		c.nextRound("");
	}
}
