package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.io.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.ImpossibleAngleError;

/**
 * Un point 2D dans une espace reel
 */

public class Point2D implements Logable, IPoint {
    private final double x;
    private final double y;
    private static final double EPS = Utils.EPSILON_COLLISION;

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

        if (Utils.almostEquals(x, 0) && Utils.almostEquals(y, 0)) {
            // L'angle n'est pas définit
            throw new ImpossibleAngleError("Cannot define angle for 0,0");
        } else if (Utils.almostEquals(y, 0)) { // x!=0 et y==0
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
        return new Vector(other.x - this.x, other.y - this.y);
    }

    @Override
    public String toString() {
        return String.format("%s(x: %f, y: %f)", this.getClass().getSimpleName(), x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Point2D))
            return false;
        Point2D other = (Point2D) obj;
        return Utils.almostEqualsBoundsIncluded(other.x, this.x, EPS)
                && Utils.almostEqualsBoundsIncluded(other.y, this.y, EPS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    @JsonProperty("y")
    public double y() {
        return y;
    }

    @Override
    @JsonProperty("x")
    public double x() {
        return x;
    }

    @Override
    public String toLogs() {
        return String.format("%f %f", x, y);
    }

    public Point2D getRotatedAround(Point2D anchorPoint, double angle) {
        if (Utils.almostEquals(new Point2D(0, 0), anchorPoint)) {
            return getRotatedBy(angle);
        }

        Vector fromAnchorToThis = new Vector(anchorPoint, this);
        Vector rotatedVector = fromAnchorToThis.getRotatedBy(angle);
        return anchorPoint.getTranslatedBy(rotatedVector);
    }
}
