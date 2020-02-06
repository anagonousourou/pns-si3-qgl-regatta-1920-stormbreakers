package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    Vector(Cartesian start, Cartesian end) {
        this.x = end.getX() - start.getX();
        this.y = end.getY() - start.getY();
    }

    public Vector(Position start, Position end) {
        this(
                new Cartesian(start.getX(),start.getY()),
                new Cartesian(end.getX(),end.getY())
        );
    }

    public double norm() {
        return Math.sqrt(x*x+y*y);
    }

    double scal(Vector other) {
        return this.x*other.x+this.y*other.y;
    }

    public double angleBetween(Vector other) {
        return Math.acos( this.scal(other) / (this.norm() * other.norm()) );
    }



}
