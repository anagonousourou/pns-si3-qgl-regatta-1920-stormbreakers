package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Moving;

public class Marin implements Deckable {
	private static final int MAX_DIST = 5;

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

	/**
	 * Deplace le marin selon les distances passees en parametre
	 * @param xdistance distance selon l'axe X
	 * @param ydistance distance selon l'axe Y
	 */
    public void move(int xdistance, int ydistance) {
    	if (Math.abs(xdistance) + Math.abs(ydistance) <= MAX_DIST) {
    		// Le deplacement respecte la contraite de distance
			x += xdistance;
			y += ydistance;
		}
	}

	public void move(Moving mvt){
		x+=mvt.getXdistance();
		y+=mvt.getYdistance();
	}
	
	public Moving howToGoTo(int xpos,int ypos){
		return new Moving(this.id, xpos-this.x, ypos-this.y);
	}
}
