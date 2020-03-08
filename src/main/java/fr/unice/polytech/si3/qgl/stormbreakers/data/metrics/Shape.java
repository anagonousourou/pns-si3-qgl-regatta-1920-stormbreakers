package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

/**
 * Classe representant une forme geometrique
 * Prend son centre comme origine (0,0) dans ses operations
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Circle.class, name="circle"),
        @JsonSubTypes.Type(value = Rectangle.class, name="rectangle"),
        @JsonSubTypes.Type(value = Polygon.class, name="polygon")
})
public abstract class Shape implements Logable {
    protected static Point2D origin = new Point2D(0,0); // For internal context
    protected static Position anchor; // For external context
    private String type;

    @JsonCreator
    Shape(@JsonProperty("type") String type) {
        this.type = type;
    }
    @JsonProperty("type")
	public String getType() {
		return type;
	}

    /**
     * Renvoie si oui ou non, les coordonnees passees en paramettre
     * sont a l'interieur de la forme
     * NB : Les coordonnees doivent etre données par rapport au CENTRE de la forme
     * @return true if (x,y) is inside this shape, false if not
     */
    public abstract boolean isPtInside(Point2D pt);
    
    

}
