package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.Objects;


public class Fraction {
    

    private int numerator;

    private int denominator;

    /**
     * 
     * Create a fraction with the value 0.
     * 
     */

    public Fraction() {
        setValue(0, 1);
    }

    /**
     * 
     * Create a new fraction.
     * 
     * @param numerator   the numerator
     * 
     * @param denominator the denominator
     * 
     */

    public Fraction(int numerator, int denominator) {

        setValue(numerator, denominator);

    }

   
    public void setValue(int numerator, int denominator) {

        // Support negative numbers.

        if (denominator == 0) {

            throw new ArithmeticException("Denominator cannot be zero.");

        } else if (denominator < 0) {

            // put - sign to numerator

            numerator = -numerator;

            denominator = -denominator;

        }

        // All fractions have to be simplified. To do that, we need to find the GCD of
        // two numbers and divide.

        int gcd = Fraction.gcd(Math.abs(numerator), Math.abs(denominator));

        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    

    

    
    /**
     * 
     * Return this fraction as a string.
     * 
     * @return numerator/denominator
     * 
     */

    @Override

    public String toString() {

        return toString(numerator, denominator);

    }

    /**
     * 
     * Return a fraction of numerator/denominator as a string.
     * 
     * @param numerator   The numerator.
     * 
     * @param denominator The denominator.
     * 
     * @return numerator/denominator
     * 
     */

    private String toString(int numerator, int denominator) {

        return String.format("%d/%d", numerator, denominator);

    }

    

    /**
     * Determine the Greatest Common Divisor (GCD) of two integers (Euclid’s
     * Algorithm)
     * 
     * @param a First integer
     * 
     * @param b Second integer
     * 
     * @return GCD(a, b)
     * 
     */

    public static int gcd(int a, int b) {

        int r;

        while (b > 0) {

            r = a % b;

            a = b;

            b = r;

        }

        return a;

    }

    

    public double eval() {
        return ((double) numerator) / denominator;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Fraction))
            return false;
        Fraction other = (Fraction) obj;
        return this.numerator == other.numerator && this.denominator == other.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }
}