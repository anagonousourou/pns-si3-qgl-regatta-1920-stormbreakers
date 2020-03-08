package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.Objects;
import static java.lang.Math.hypot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.ImpossibleAngleError;

/**
 * Un point 2D dans une espace reel
 */

public class Point2D implements Logable, IPoint{
    private double x;
    private double y;
    

    @JsonCreator
    public Point2D(@JsonProperty("x") double x, @JsonProperty("y") double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(IPoint point) {
        this.x = point.x();
        this.y = point.y();
    }

    public Point2D(Vector fromOrigin) {
        this.x = fromOrigin.getDeltaX();
        this.y = fromOrigin.getDeltaY();
    }

    

    /**
     * Donne l'angle entre l'axe x et le vecteur position retourne null si (0,0)
     * 
     * @return une angle entre ]-Pi,Pi]
     * @throws ImpossibleAngleError if point is 0,0
     */
    public double getAngleFromXAxis() {
        Vector unitX = new Vector(1, 0);
        Vector toPoint = new Vector(this.x, this.y);
        // Renvoie un angle entre [0,Pi]
        double unorientedAngle = unitX.nonOrientedAngleWith(toPoint);

        if (x == 0 && y == 0) {
            // L'angle n'est pas définit
            throw new ImpossibleAngleError("Cannot define angle for 0,0");
        } else if (y == 0) { // x!=0 et y==0
            // Si x > 0 l'angle est 0
            if (x > 0)
                return 0;
            // Si x < 0 l'angle est Pi
            else
                return Math.PI;
        } else { // y!=0
            // Si y>0 l'angle est bien entre ]0,Pi[
            if (y >= 0)
                return unorientedAngle;
            // Si y<0 l'angle est entre ]-Pi,0[
            else
                return -unorientedAngle;
        }

    }

    /**
     * Fait trourner le point d'un angle orienté theta autour de l'origine du repère
     * 
     * @return le point post-rotation
     */
    public Point2D getRotatedBy(double theta) {

        double newX = x * Math.cos(theta) - y * Math.sin(theta);
        double newY = x * Math.sin(theta) + y * Math.cos(theta);

        // On corrige des erreurs d'arrondis autour de 0
        if (Utils.almostEquals(newX, 0))
            newX = 0;
        if (Utils.almostEquals(newY, 0))
            newY = 0;

        return new Point2D(newX, newY);
    }

    /**
     * Fait une tranlation du point avec les données en parametre
     * 
     * @param deltaX translation selon X
     * @param deltaY translation selon Y
     * @return le point post-translation
     */
    public Point2D getTranslatedBy(double deltaX, double deltaY) {
        return new Point2D(x + deltaX, y + deltaY);
    }

    /**
     * Fait une tranlation du point avec le vecteur
     * 
     * 
     * @return le point post-translation
     */
    public Point2D getTranslatedBy(Vector vector) {
        return new Point2D(x + vector.getDeltaX(), y + vector.getDeltaY());
    }

    /**
     * Renvoie le vecteur duquel déplacer ce point pour obtenir celui passé en
     * parametre
     * 
     * @param other vecteur cible
     * @return Vector le vecteur
     */
    public Vector getVectorTo(Point2D other) {
        return new Vector(x - other.x, y - other.y);
    }

    

    @Override
    public String toString() {
        return String.format("%s(x: %f, y: %f)", this.getClass().getSimpleName(),x,y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Point2D))
            return false;
        Point2D other = (Point2D) obj;
        return Utils.almostEqualsBoundsIncluded(other.x,this.x,Utils.EPSILON_COLLISION) && Utils.almostEqualsBoundsIncluded(other.y,this.y,Utils.EPSILON_COLLISION);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Computes the orientation of the 3 points: returns +1 is the path P0->P1->P2
     * turns Counter-Clockwise, -1 if the path turns Clockwise, and 0 if the point
     * P2 is located on the line segment [P0 P1]. Algorithm taken from Sedgewick.
     * 
     * @param p0 the initial point
     * @param p1 the middle point
     * @param p2 the last point
     * @return +1, 0 or -1, depending on the relative position of the points
     */
    public static int ccw(Point2D p0, Point2D p1, Point2D p2) {
        double x0 = p0.x;
        double y0 = p0.y;
        double dx1 = p1.x - x0;
        double dy1 = p1.y - y0;
        double dx2 = p2.x - x0;
        double dy2 = p2.y - y0;

        if (dx1 * dy2 > dy1 * dx2)
            return +1;
        if (dx1 * dy2 < dy1 * dx2)
            return -1;
        if ((dx1 * dx2 < 0) || (dy1 * dy2 < 0))
            return -1;
        if (hypot(dx1, dy1) < hypot(dx2, dy2))
            return +1;
        return 0;
    }

    @Override @JsonProperty("y")
    public double y() {
        return y;
    }
    @Override @JsonProperty("x")
    public double x() {
        return x;
    }

    @Override
    public String toLogs() {
        return String.format("%f %f",x,y);
    }
}
