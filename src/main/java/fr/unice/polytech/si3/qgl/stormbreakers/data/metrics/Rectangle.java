package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rectangle extends Shape {
    private double width;
    private double height;
    private double orientation;

    @JsonCreator
    Rectangle(
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
        return xOk || yOk;
    }

    /**
     * true if min <= x <= max, false if not
     * @param x value to test
     * @param min minimum value
     * @param max maximum value
     * @param epsilon error correction
     * @return true if min <= x <= max, false if not
     */
    private boolean between(double x,double min,double max, double epsilon) {
        return simpleBetween(x,min,max)
                || simpleBetween(x-epsilon, min, max)
                || simpleBetween(x+epsilon, min, max)
                ;
    }

    private boolean simpleBetween(double value,double min,double max) {
        return (min<=value)&&(value<=max);
    }
}
