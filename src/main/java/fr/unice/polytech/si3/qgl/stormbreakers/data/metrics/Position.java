package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.complex.Cartesian;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.complex.Polar;

public class Position {
    private double x;
    private double y;
    private double orientation;
    
    @JsonCreator
    public Position(@JsonProperty("x") double x,@JsonProperty("y") double y, @JsonProperty("orientation") double orientation) {
    	this.x=x;
    	this.y=y;
        this.orientation=orientation;
    }

    public Position(double x, double y) {
        this(x,y,0);
    }
    
    @JsonProperty("x")
    public double getX() {
        return x;
    }

    @JsonProperty("y")
    public double getY() {
        return y;
    }

    public double thetaTo(Position other){
        return Math.atan2(other.getY(),other.getX())
        - Math.atan2(this.getY(),this.getX());
    }

    public double distanceTo(Position pos){
        return Math.sqrt((pos.x-this.x)*(pos.x-this.x)+(pos.y-this.y)*(pos.y-this.y));
    }

	public double getOrientation() {
		return this.orientation;
	}

    public Position getRotatedBy(double theta) {
        Polar oldPolar = new Cartesian(x,y).toPolar();
        Cartesian rotatedCartesian = new Polar(oldPolar.getR(),oldPolar.getTheta() + theta).toCartesian();
        return new Position(rotatedCartesian.getX(),rotatedCartesian.getY(),oldPolar.getTheta() + theta);
    }
}
