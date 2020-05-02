package fr.unice.polytech.si3.qgl.stormbreakers.math.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import fr.unice.polytech.si3.qgl.stormbreakers.io.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw.Drawable;

/**
 * Classe representant une forme geometrique Prend son centre comme origine
 * (0,0) dans ses operations
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Circle.class, name = "circle"),
        @JsonSubTypes.Type(value = Rectangle.class, name = "rectangle"),
        @JsonSubTypes.Type(value = Polygon.class, name = "polygon") })
public abstract class Shape implements Logable, CanCollide, Drawable {
    protected static Point2D origin = new Point2D(0, 0); // For internal context
    protected Position anchor; // For external context
    private String type;

    Shape(String type, Position anchor) {
        this.type = type;
        this.anchor = anchor;
    }

    @JsonCreator
    public Shape(@JsonProperty("type") String type) {
        this.type = type;
        this.anchor = new Position(0, 0, 0);
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * Changes the shape's anchor to a given one
     */
    public void setAnchor(Position newAnchor) {
        this.anchor = newAnchor;
    }

    public Position getAnchor() {
        return anchor;
    }

    public Point2D getAnchorPoint() {
        return anchor.getPoint2D();
    }

    public double getAnchorOrientation() {
        return anchor.getOrientation();
    }

    public abstract ShapeType getTypeEnum();

    /**
     * Renvoie si oui ou non, les coordonnees passees en paramettre sont a
     * l'interieur de la forme NB : Les coordonnees doivent etre données par rapport
     * au CENTRE de la forme
     * 
     * @return true if (x,y) is inside this shape, false if not
     */
    public abstract boolean isPtInside(IPoint pt);

    public abstract boolean isInsideOpenShape(IPoint pt);

    public abstract IPoint intersectionPoint(IPoint depart, IPoint arrive);

    /**
     * Renvoie une forme plus étendue d'une marge donnée
     * 
     * @return la forme étendue
     */
    public abstract Shape wrappingShape(double margin);

   
}
