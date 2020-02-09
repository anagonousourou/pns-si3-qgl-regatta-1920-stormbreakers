package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;

import java.util.Objects;

public class Marin implements Deckable, Logable {
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

	public void move(MoveAction mvt){
		x+=mvt.getXdistance();
		y+=mvt.getYdistance();
	}
	
	public MoveAction howToGoTo(int xpos,int ypos){
		return new MoveAction(this.id, xpos-this.x, ypos-this.y);
	}



	@Override
	public boolean equals(Object obj) {
		if (this==obj) return true;
		if (!(obj instanceof Marin)) return false;
		Marin other = (Marin) obj;
		return other.id == id
				&& other.name.equals(name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id,name);
	}

	public String toString() {
		return "le marin "+name+" d'ID "+id+" est de position ("+x+","+y+")";
	}

	@Override
	public String toLogs() {
		return id+","+x+y;
	}
}
