package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.Objects;

/**
 * Un point 2D dans une espace reel
 */

public class Point2D {
    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Vector fromOrigin) {
        this.x = fromOrigin.getDeltaX();
        this.y = fromOrigin.getDeltaY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }



    /**
     * Donne l'angle entre l'axe x et le vecteur position
     * retourne null si (0,0)
     * @return une angle entre ]-Pi,Pi]
     * @throws RuntimeException if point is 0,0
     */
    public double getAngleFromXAxis() {
        Vector unitX = new Vector(1,0);
        Vector toPoint = new Vector(getX(),getY());
        // Renvoie un angle entre [0,Pi]
        double unorientedAngle = unitX.angleBetween(toPoint);

        if (x==0 && y==0) {
            // L'angle n'est pas définit
            throw new RuntimeException("Cannot define angle for 0,0");
        } else if (y==0)  { // x!=0 et y==0
            // Si x > 0 l'angle est 0
            if (x > 0) return 0;
                // Si x < 0 l'angle est Pi
            else return Math.PI;
        } else { // y!=0
            // Si y>0 l'angle est bien entre ]0,Pi[
            if (y>=0) return unorientedAngle;
                // Si y<0 l'angle est entre ]-Pi,0[
            else return -unorientedAngle;
        }





    }

    /**
     * Fait trourner le point d'un angle orienté theta autour de l'origine du repère
     * @return le point post-rotation
     */
    public Point2D getRotatedBy(double theta) {

        double newX = x * Math.cos(theta) - y * Math.sin(theta);
        double newY = x * Math.sin(theta) + y * Math.cos(theta);

        // On corrige des erreurs d'arrondis autour de 0
        if (Utils.almostEquals(newX,0)) newX = 0;
        if (Utils.almostEquals(newY,0)) newY = 0;

        return new Point2D(newX,newY);
    }

    /**
     * Fait une tranlation du point avec les données en parametre
     * @param deltaX translation selon X
     * @param deltaY translation selon Y
     * @return le point post-translation
     */
    public Point2D getTranslatedBy(double deltaX, double deltaY) {
        return new Point2D(x+deltaX,y+deltaY);
    }

    /**
     * Renvoie le vecteur duquel déplacer ce point pour obtenir celui passé en parametre
     * @param other vecteur cible
     * @return Vector le vecteur
     */
    public Vector getVectorTo(Point2D other) {
        return new Vector(x-other.x,y-other.y);
    }

    /**
     * Renvoie la distance de ce point à un point passé en parametre
     * @param other autre point
     * @return double la distance
     */
    public double getDistanceTo(Point2D other) {
        return getVectorTo(other).norm();
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (!(obj instanceof Point2D)) return false;
        Point2D other = (Point2D) obj;
        return  other.x==x
                && other.y==y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }
}
