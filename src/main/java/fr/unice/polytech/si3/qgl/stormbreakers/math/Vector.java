package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;

import java.util.Objects;

public class Vector {

    public static final Vector UnitX = new Vector(1,0);

    private double dx;
    private double dy;

    public Vector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Vector(IPoint start, IPoint end) {
        this.dx = end.x() - start.x();
        this.dy = end.y() - start.y();
    }

    public Vector(Vector toCopy) {
        this(toCopy.dx,toCopy.dy);
    }

    public double norm() {
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double squaredNorm() {
        // TODO: 05/03/2020 Tests
        return this.scal(this);
    }

    public double scal(Vector other) {
        return this.dx * other.dx + this.dy * other.dy;
    }

    /**
     * Renvoie la valeur absolue de l'angle non orienté entre les deux vecteurs
     * comprise entre [0,Pi]
     * 
     * @param other 2e vecteur
     * @return double entre 0 et Pi
     */
    public double angleBetween(Vector other) {
        return Math.acos(this.scal(other) / (this.norm() * other.norm()));
    }

    public static Vector createUnitVector(double angle) {
        return new Vector(Math.cos(angle), Math.sin(angle));
    }

    public double getDeltaX() {
        return dx;
    }

    public double getDeltaY() {
        return dy;
    }
    /**
     * Mutiplie les coords du vecteur par le facteur passé en parametre
     * ne modifie pas le vecteur
     * @param scaleFactor facteur
     * @return le nouveau vecteur obtenu
     */
    public Vector scaleVector(double scaleFactor) {
        return new Vector(dx *scaleFactor, dy *scaleFactor);
    }

    public Vector getRotatedBy(double angle) {
        double magnitude = this.norm();
        double previousAngle = this.angleBetween(UnitX);
        Vector newUnitRotatedVector = Vector.createUnitVector(previousAngle+angle);
        return newUnitRotatedVector.scaleVector(magnitude);
    }

    public Vector normalize() {
        // TODO: 05/03/2020 Tests
        double magnitude = this.norm();
        double ratio = Math.pow(magnitude,-1);
        return this.scaleVector(ratio);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Vector))
            return false;
        Vector other = (Vector) obj;
        return Utils.almostEquals(this.dx,other.dx) && Utils.almostEquals(this.dy,other.dy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dx, dy);
    }

}
