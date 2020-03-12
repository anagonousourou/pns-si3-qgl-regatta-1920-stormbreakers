package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {


    private Utils(){

    }
    public static final double EPS = 0.001;
    public static final double MAX_RUDDER_ROTATION = Math.PI / 4;
    public static final double TAILLE_BATEAU = 50;

    public static final double EPSILON = Math.pow(10, -10);
    public static final double EPSILON_COLLISION = 2 * Math.pow(10, -2);
    public static boolean almostEquals(double expected, double result) {
        return Math.abs(expected - result) < EPSILON;
    }

    /**
     * Boolean function analog to JUnit <code>assertEquals(double expected, double actual, double delta)</code>
     * without allowing bounds
     * Bounds : actual-delta, actual+delta
     * @param expected expected value
     * @param result actual value
     * @param eps delta
     * @return true if almost equal, false if not
     */
    // LATER: 08/03/2020 Replace all tests usage by : assertEquals(double expected, double actual, double delta)
    public static boolean almostEquals(double expected, double result, double eps) {
        return Math.abs(expected - result) < (Math.abs(eps) - EPSILON);
    }


    /**
     * Boolean function analog to JUnit <code>assertEquals(double expected, double actual, double delta)</code>
     * allowing bounds
     * Bounds : actual-delta, actual+delta
     * @param expected expected value
     * @param result actual value
     * @param eps delta
     * @return true if almost equal, false if not
     */
    // LATER: 08/03/2020 Replace all tests usage by : assertEquals(double expected, double actual, double delta)
    public static boolean almostEqualsBoundsIncluded(double expected, double result, double eps) {
        return Math.abs(expected - result) <= eps;
    }

    public static boolean almostEqualsBoundsIncluded(double expected, double result) {
        return Math.abs(expected - result) <= EPSILON;
    }

    public static boolean almostEquals(Point2D p1, Point2D p2) {
        return p1.distanceTo(p2) < EPSILON;
    }
    
    public static boolean almostEquals(Point2D p1, Point2D p2, double delta) {
    	return p1.distanceTo(p2) < (Math.abs(delta) - EPSILON);
    }

    /**
     * Check if -bound <= value <= bound
     * @param value to test
     * @param bound expected to be positive 
     * @return true if condition is verified
     */
    public static boolean within(double value,double bound){
        return Math.abs(value)<=bound;
    }

    /** Generic function to concatenate 2 lists in Java */
    public static <T> List<T> concatenate(List<T> list1, List<T> list2) {
        return Stream.of(list1, list2).flatMap(List::stream).collect(Collectors.toList());
    }

    /** Generic function to concatenate 3 lists in Java */
    public static <T> List<T> concatenate(List<T> list1, List<T> list2, List<T> list3) {
        return Stream.of(list1, list2, list3).flatMap(List::stream).collect(Collectors.toList());
    }

    public static <T> List<T> concatenate(List<T> pointsOfSegment, List<T> pointsOfSegment2, List<T> pointsOfSegment3,
            List<T> pointsOfSegment4) {
        return Stream.of(pointsOfSegment, pointsOfSegment2, pointsOfSegment3, pointsOfSegment4).flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static double clamp(double value,double min,double max){
        if(value<=min){
            return min;
        }
        if(value >=max){
            return max;
        }
        return value;
        //Math.min(Math.max(value, min), max)
    }

}
