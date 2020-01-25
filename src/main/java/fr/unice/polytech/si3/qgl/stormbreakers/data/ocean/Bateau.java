package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Deck;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;

public class Bateau extends AutreBateau {
    private String name;
    private Deck deck;
    private List<Equipment> entities;

   /* Bateau(Position position, Shape shape, int life, String name, Deck deck, List<Equipment> entities) {
        super(position,shape,life);
        this.name = name;
        this.deck = deck;
        this.entities = entities;
    }
   */
    @JsonCreator 
    Bateau(
        @JsonProperty("position")Position position,
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
   

}
