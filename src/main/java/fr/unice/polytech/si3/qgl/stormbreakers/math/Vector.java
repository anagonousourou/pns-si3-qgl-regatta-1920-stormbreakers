package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;

import java.util.Objects;

public class Vector {

    public static final Vector UnitX = new Vector(1,0);

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(IPoint start, IPoint end) {
        this.x = end.x() - start.x();
        this.y = end.y() - start.y();
    }

    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public double scal(Vector other) {
        return this.x * other.x + this.y * other.y;
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
        return x;
    }

    public double getDeltaY() {
        return y;
    }
    /**
     * Mutiplie les coords du vecteur par le facteur passé en parametre
     * ne modifie pas le vecteur
     * @param scaleFactor facteur
     * @return le nouveau vecteur obtenu
     */
    public Vector scaleVector(double scaleFactor) {
        return new Vector(x*scaleFactor,y*scaleFactor);
    }

    public Vector getRotatedBy(double angle) {
        double magnitude = this.norm();
        double previousAngle = this.angleBetween(UnitX);
        Vector newUnitRotatedVector = Vector.createUnitVector(previousAngle+angle);
        return newUnitRotatedVector.scaleVector(magnitude);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Vector))
            return false;
        Vector other = (Vector) obj;
        return Utils.almostEquals(this.x,other.x) && Utils.almostEquals(this.y,other.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("%s(x: %f,y: %f)", this.getClass().getSimpleName(),this.x,this.y);
    }
}
