package fr.unice.polytech.si3.qgl.stormbreakers.math;

public class Complex {
    private final double re;
    private final double im;

    Complex(double a, double b) {
        re = a;
        im = b;
    }

    double getRe() {
        return re;
    }

    double getIm() {
        return im;
    }

    
    public Complex add(Complex c) {
        return new Complex(re+c.re,im+c.im);
    }

    public Complex mult(Complex c) {
        double real = re*c.re - im*c.im;
        double imag = re*c.im + im*c.re;
        return new Complex(real,imag);
    }
    

    @Override
    public String toString() {
        return "("+re+", "+im+")";
    }
}