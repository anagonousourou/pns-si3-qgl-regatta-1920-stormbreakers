package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    

    public double distanceTo(Position pos){
        return Math.sqrt((pos.x-this.x)*(pos.x-this.x)+(pos.y-this.y)*(pos.y-this.y));
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double thetaTo(Position other){
        return Math.atan2(other.getY(),other.getX())
        - Math.atan2(this.getY(),this.getX());
    }
}
