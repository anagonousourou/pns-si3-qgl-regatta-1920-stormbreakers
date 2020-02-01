package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.Objects;

public class Fraction {
    private Integer numerator;
    private Integer denominator;

    Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    double eval() {
        return (float) numerator / denominator;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof Fraction)) return false;
        Fraction other = (Fraction) obj;
        return numerator.equals(other.numerator)
                && denominator.equals(other.numerator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator,denominator);
    }
}