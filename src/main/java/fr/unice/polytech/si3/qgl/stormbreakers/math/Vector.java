package fr.unice.polytech.si3.qgl.stormbreakers.math;

public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Point2D start, Point2D end) {
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
     * @param scaleFactor
     * @return le nouveau vecteur obtenu
     */
    public Vector scaleVector(double scaleFactor){
        return new Vector(this.x*scaleFactor, this.y*scaleFactor);
    }
}
