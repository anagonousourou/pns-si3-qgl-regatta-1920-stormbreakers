package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Rectangle extends Shape {
    private double width;
    private double height;
    private double orientation;

    @JsonCreator
    public Rectangle(
            @JsonProperty("width") double width,
            @JsonProperty("height") double height,
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
    public boolean isPosInside(double x, double y) {
        // On ramene le plan pour que les cotes du rectangle soient sur les axes du repere
        if (orientation != 0) {
            Position pos = new Position(x,y,orientation);
            pos = pos.getRotatedBy(-orientation);
            x = pos.getX();
            y = pos.getY();
        }

        double eps = Math.pow(10,-10);

        boolean xOk = between(x,-height/2,height/2,eps);
        boolean yOk = between(y,-width/2,width/2,eps);
        return xOk && yOk;
    }

    /**
     * true if min <= value <= max, false if not
     * @param value value to test
     * @param min minimum value
     * @param max maximum value
     * @param epsilon error correction
     * @return true if min <= value <= max, false if not
     */
    private boolean between(double value,double min,double max, double epsilon) {
        return simpleBetween(value,min,max)
                || simpleBetween(value-epsilon, min, max)
                || simpleBetween(value+epsilon, min, max)
                ;
    }

    private boolean simpleBetween(double value,double min,double max) {
        return (min<=value)&&(value<=max);
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (!(obj instanceof Rectangle)) return false;
        Rectangle other = (Rectangle) obj;
        return other.width == width
                && other.height == height
                && other.orientation == orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width,height,orientation);
    }

    @Override
    public String toString() {
        return "R"+"("+width+"|"+height+"|"+orientation+")";
    }
}
