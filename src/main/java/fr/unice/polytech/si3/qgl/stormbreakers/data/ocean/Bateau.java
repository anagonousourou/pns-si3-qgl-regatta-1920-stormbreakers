package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Deck;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;

import javax.swing.text.html.parser.Entity;

public class Bateau extends AutreBateau implements Logable {
    private String name;
    private Deck deck;
    private List<Equipment> entities;


    @JsonCreator
    public Bateau(
            @JsonProperty("position") Position position,
            @JsonProperty("shape") Shape shape,
            @JsonProperty("life") int life,
            @JsonProperty("deck") Deck deck,
            @JsonProperty("entities") List<Equipment> entities
    ){
    	super(position,shape,life);
    	this.deck=deck;
    	this.entities=entities;
    }
    
    @JsonProperty("name")
    public String getName() {
		return name;
	}
    
    @JsonProperty("entities")
    public List<Equipment> getEquipments() {
    	return entities;
    }
    public List<Equipment> getRames() {
    	return this.entities.stream().filter(e-> e.getType().equals("oar")).collect(Collectors.toList());
    }
    @JsonProperty("deck")
    public Deck getDeck() {
		return deck;
	}


    @Override
    public String toLogs() {
        return deck.toLogs() + ",E:" + entitiesToString();
    }

    private String entitiesToString() {
        List<Logable> logables = new ArrayList<>(entities);
        return Logable.listToLogs(logables,"|", "[", "]");
    }

}
