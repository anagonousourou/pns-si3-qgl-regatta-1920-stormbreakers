package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;

public class Utils {
    // TODO: 05/03/2020 Test All

    public static final double EPS = 0.001;
    public static final double MAX_RUDDER_ROTATION = Math.PI / 4;
    public static final double TAILLE_BATEAU = 50;

    private Utils() {

    }

    public static final double EPSILON = Math.pow(10, -10);

    public static boolean almostEquals(double expected, double result) {
        return Math.abs(expected - result) < EPSILON;
    }

    public static boolean almostEquals(double expected, double result, double eps) {
        return Math.abs(expected - result) < eps;
    }

    public static boolean almostOrPerfectlyEquals(double expected, double result, double eps) {
        return Math.abs(expected - result) <= eps;
    }

    public static boolean almostEquals(IPoint expected, IPoint result) {
        return result.distanceTo(expected) < EPSILON;
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

}
