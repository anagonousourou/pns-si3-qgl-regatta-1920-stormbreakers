package fr.unice.polytech.si3.qgl.stormbreakers.math;

public class Utils {

    private Utils(){
        
    }

    private static final double EPSILON = Math.pow(10,-10);

    public static boolean almostEquals(double expected, double result) {
        return Math.abs(expected-result) < EPSILON;
    }

    public static boolean almostEquals(Point2D expected, Point2D result) {
        return result.getDistanceTo(expected) < EPSILON;
    }

}
