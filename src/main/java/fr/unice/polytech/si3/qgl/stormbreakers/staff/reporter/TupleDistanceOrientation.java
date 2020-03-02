package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

public class TupleDistanceOrientation {
    private final double distance;
    private final double orientation;
    TupleDistanceOrientation(double distance,double orientation){
        this.distance=distance;
        this.orientation=orientation;
    }

    public double getDistance() {
        return distance;
    }

    public double getOrientation() {
        return orientation;
    }
}