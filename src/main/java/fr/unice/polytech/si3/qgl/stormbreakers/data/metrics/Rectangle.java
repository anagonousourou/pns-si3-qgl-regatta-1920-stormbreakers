package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.EquationDroite;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

import java.util.Objects;

public class Rectangle extends Shape {
    private double width;
    private double height;
    private double orientation;

    @JsonCreator
    public Rectangle(@JsonProperty("width") double width, @JsonProperty("height") double height,
            @JsonProperty("orientation") double orientation) {
        super("rectangle");
        this.width = width;
        this.height = height;
        this.orientation = orientation;
    }

    @JsonProperty("width")
    public double getWidth() {
        return width;
    }

    @JsonProperty("height")
    public double getHeight() {
        return height;
    }

    @JsonProperty("orientation")
    public double getOrientation() {
        return orientation;
    }

    @Override
    public boolean isPtInside(Point2D pt) {
        Point2D point2D = new Point2D(pt);
        // On ramene le plan pour que les cotes du rectangle soient sur les axes du
        // repere
        if (orientation != 0) {
            point2D = pt.getRotatedBy(-orientation);
        }
        return isPtInRectangle0(point2D);
    }

    /**
     * Vérifie si le point est dans le rectangle d'orientation 0
     * 
     * @param pt point à tester
     * @return true s'il est dedans, false sinon
     */
    private boolean isPtInRectangle0(Point2D pt) {
        double eps = Math.pow(10, -10);

        boolean xOk = between(pt.getX(), -height / 2, height / 2, eps);
        boolean yOk = between(pt.getY(), -width / 2, width / 2, eps);
        return xOk && yOk;
    }

    /**
     * true if min <= value <= max, false if not
     * 
     * @param value   value to test
     * @param min     minimum value
     * @param max     maximum value
     * @param epsilon error correction
     * @return true if min <= value <= max, false if not
     */
    private boolean between(double value, double min, double max, double epsilon) {
        return simpleBetween(value, min, max) || simpleBetween(value - epsilon, min, max)
                || simpleBetween(value + epsilon, min, max);
    }

    private boolean simpleBetween(double value, double min, double max) {
        return (min <= value) && (value <= max);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Rectangle))
            return false;
        Rectangle other = (Rectangle) obj;
        return other.width == width && other.height == height && other.orientation == orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, orientation);
    }

    @Override
    public String toString() {
        return getType() + ": (" + width + " " + height + " " + orientation + ")";
    }

    @Override
    public String toLogs() {
        return "R" + "(" + width + "|" + height + "|" + orientation + ")";
    }

	public Point2D findPointNearestToPosition(Position other,Position rectangle) {
		// TODO Auto-generated method stub
		if(orientation-Math.PI/2<0.0001||orientation-(2*Math.PI/2)<0.0001) {
			return new Point2D( rectangle.getX(),other.getY());
		}else if(orientation<0.0001||orientation-(2*Math.PI)<0.0001) {
			return new Point2D(other.getX(), rectangle.getY());
		}else {
			/*LineSegment2D sRect= new LineSegment2D(
					Math.cos(orientation)*(rectangle.getX()+(this.height/2)),
					Math.sin(orientation)*rectangle.getY()+(this.width/2),
					Math.cos(orientation)*other.getX()-(this.height/2),
					Math.sin(orientation)*rectangle.getY()-(this.width/2));*/
			
			EquationDroite droiteRect= new EquationDroite(Math.cos(orientation)*(rectangle.getX()+(this.height/2)),
					Math.sin(orientation)*rectangle.getY()+(this.width/2),
					Math.cos(orientation)*other.getX()-(this.height/2),
					Math.sin(orientation)*rectangle.getY()-(this.width/2));
			
			return null;//sRect.getIntersection(other);
		}
		
	}


}
