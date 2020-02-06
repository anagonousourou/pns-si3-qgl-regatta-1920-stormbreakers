package fr.unice.polytech.si3.qgl.stormbreakers.math;

public class Polar implements Coord {

    private final Complex complex;

    public Polar(double r, double theta) {
        complex = new Complex(r,theta);
    }

    Polar(Complex complex) {
        this.complex=complex;
    }

    @Override
    public Cartesian toCartesian() {
        double x = complex.getRe() * Math.cos(complex.getIm());
        double y = complex.getRe() * Math.sin(complex.getIm());
        return new Cartesian(x,y);
    }

    @Override
    public Polar toPolar() {
        return this;
    }

    
    public Polar add(Coord c) {
        Cartesian other = c.toCartesian();
        return other.add(this).toPolar();
    }

    public Polar mult(Coord c) {
        Cartesian other = c.toCartesian();
        return other.mult(this).toPolar();
    }
    

    public double getR() {
        return complex.getRe();
    }

    public double getTheta() {
        return complex.getIm();
    }

    @Override
    public String toString() {
        return "Polar " + super.toString();
    }

}
