package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.Objects;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;

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

    public static boolean areCollinear(Vector vector1, Vector vector2) {
        // Z component of cross product
        double crossZ = vector1.dx * vector2.dy - vector2.dx * vector1.dy;
        return Utils.almostEquals(0,crossZ);
    }

    public double norm() {
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double squaredNorm() {
        return this.scal(this);
    }

    public double scal(Vector other) {
        return this.dx * other.dx + this.dy * other.dy;
    }

    public double scal(IPoint startOtherVector,IPoint endOtherVector){
        Vector otherVector=new Vector(startOtherVector, endOtherVector);
        return this.scal(otherVector);
    }

    /**
     * Renvoie la valeur absolue de l'angle non orienté entre les deux vecteurs
     * comprise entre [0,Pi]
     * 
     * @param other 2e vecteur
     * @return double entre 0 et Pi
     */
    public double nonOrientedAngleWith(Vector other) {
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

    /**
     * Returns vector orientation in radians
     * Bounds : [0,2Pi]
     * @return the angle from the x unitVector
     */
    public double getOrientation() {
        double orientedAngle = Math.atan2(dy,dx);
        if (orientedAngle<0) {
            orientedAngle += 2*Math.PI;
        }
        return orientedAngle;
    }

    public Vector getRotatedBy(double angle) {
        double magnitude = this.norm();
        double previousAngle = this.getOrientation();
        Vector newUnitRotatedVector = Vector.createUnitVector(previousAngle+angle);
        return newUnitRotatedVector.scaleVector(magnitude);
    }

    public Vector normalize() {
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

    @Override
    public String toString() {
        return String.format("%s(x: %f,y: %f)", this.getClass().getSimpleName(),this.dx,this.dy);
    }


    /**
     * Extends a vector by a given length
     * @param margin lenght to extend by
     * @return extended vector
     */
    public Vector extendBy(double margin) {
        if (Utils.almostEquals(0.0,this.norm())) throw new UnsupportedOperationException("Cannot extend a vector of norm 0");
        double currentMag = norm();
        return this.setScaleTo(currentMag+margin);
    }


    /**
     * Changes the vectors scale
     * if the new scale is negative, the vector direction will be flipped
     * @param newScale new scale to give to the vector
     * @return the rescaled vector
     */
    public Vector setScaleTo(double newScale) {
        if (Utils.almostEquals(0.0,this.norm())) throw new UnsupportedOperationException("Cannot set scale for a vector of norm 0");
        return this.normalize().scaleVector(newScale);
    }

    /**
     * Rotates the vector by 90° in the Clockwise direction
     */
    public Vector rotateBy90CW() {
        return new Vector(dy,-dx);
    }

    /**
     * Rotates the vector by 90° in the Counter Clockwise direction
     */
    public Vector rotateBy90CCW() {
        return new Vector(-dy,dx);
    }
}
