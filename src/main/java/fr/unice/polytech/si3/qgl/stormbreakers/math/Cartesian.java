package fr.unice.polytech.si3.qgl.stormbreakers.math;

public class Cartesian implements Coord {

    private final Complex complex;

    public Cartesian(double x, double y) {
        complex = new Complex(x,y);
    }

    Cartesian(Complex complex) {
        this.complex=complex;
    }

    @Override
    public Cartesian toCartesian() {
        return this;
    }

    @Override
    public Polar toPolar() {
        double r = Math.sqrt(complex.getRe()*complex.getRe()+complex.getIm()*complex.getIm());
        double theta = Math.atan2(complex.getIm(),complex.getRe());
        return new Polar(r,theta);
    }

    
    public Cartesian add(Coord c) {
        Cartesian other = c.toCartesian();
        return new Cartesian(complex.add(other.complex));
    }

    public Cartesian mult(Coord c) {
        Cartesian other = c.toCartesian();
        return new Cartesian(complex.mult(other.complex));
    }
    

    public double getX() {
        return complex.getRe();
    }

    public double getY() {
        return complex.getIm();
    }

    @Override
    public String toString() {
        return "Cartesian " + super.toString();
    }
}
