package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.Objects;

public class Fraction {
    private Integer numerator;
    private Integer denominator;

    Fraction(int numerator, int denominator) {
        if (denominator==0) throw new IllegalArgumentException("Fraction denominator cannot be 0");
        this.numerator = numerator;
        this.denominator = denominator;
    }

    double eval() {
        return (float)numerator / denominator;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (!(obj instanceof Fraction)) return false;
        Fraction other = (Fraction) obj;
        if (eval()==other.eval()) return true;
        return numerator.equals(other.numerator)
                && denominator.equals(other.numerator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator,denominator);
    }
}