package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Marin implements Deckable {
    private int id;
    private int x;
    private int y;
    private String name;
    
    @JsonCreator
    public Marin(
    		@JsonProperty("id") int id,
    		@JsonProperty("x") int x,
    		@JsonProperty("y") int y,
    		@JsonProperty("name") String name) 
    {
    	this.id=id;
    	this.x=x;
    	this.y=y;
    	this.name=name;
    }
    
    public String getName() {
    	return name;
    }

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}
    
	public int getY() {
		return y;
	}
	
	public String toString() {
		return "le marin "+name+" d'ID "+id+" est de position ("+x+","+y+")";
		
	}
}
