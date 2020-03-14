package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.ShapeType;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.RectanglePositioned;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Vector;

public class Courant extends OceanEntity {
    private double strength;

    public Courant(@JsonProperty("position") Position position, @JsonProperty("shape") Shape shape,
            @JsonProperty("strength") double strength) {
        super("stream", position, shape);
        this.strength = strength;
    }

    public double getStrength() {
        return strength;
    }

    @Override
    public String toString() {
        return String.format("%s(strength= %f, position= %s, shape= %s)", this.getClass().getSimpleName(), strength,
                position.toString(), shape.toString());
    }

    public Point2D closestPointTo(IPoint point2d) {
        if (this.shape.getTypeEnum() == ShapeType.RECTANGLE) {
            var tmp = new RectanglePositioned((Rectangle) this.shape, this.position).closestPointTo(point2d);
            if (tmp.isPresent()) {
                return tmp.get();
            }
        } else if (this.shape.getTypeEnum() == ShapeType.POLYGON) {
            Logger.getInstance().log(this.shape.toLogs());
            Polygon poly = (Polygon) this.shape;
            var point = poly.generateBordersInThePlan(this).stream().map(l -> l.closestPointTo(point2d))
                    .min((p, pother) -> Double.compare(p.distanceTo(point2d), pother.distanceTo(point2d)));
            if (point.isPresent()) {
                return point.get();
            }
        }
        //

        // LATER add support for circle
        return null;
    }

    /**
     * Dis si le courant est compatible avec le traject défini par les parametres
     * 
     * @param depart
     * @param destination
     * @return
     */
    public boolean isCompatibleWith(IPoint depart, IPoint destination) {
        Vector courantVector = Vector.createUnitVector(this.getPosition().getOrientation());

        Vector trajectoirVector = new Vector(depart, destination);

        double helpness = courantVector.scal(trajectoirVector);
        return helpness > Utils.EPSILON;
    }

    /**
     * 
     * @param depart
     * @param destination
     * @return
     */
    public boolean isCompletelyCompatibleWith(IPoint depart, IPoint destination) {
        Vector courantVector = Vector.createUnitVector(this.getPosition().getOrientation());
        Vector courantComposantx = new Vector(courantVector.getDeltaX(), 0);
        Vector courantComposanty = new Vector(0, courantVector.getDeltaY());

        Vector trajectoirVector = new Vector(depart, destination);
        double helpx = courantComposantx.scal(trajectoirVector);
        double helpy = courantComposanty.scal(trajectoirVector);
        if (Utils.within(helpx, Utils.EPSILON)) {
            return helpy > Utils.EPSILON;
        } else if (Utils.within(helpy, Utils.EPSILON)) {
            return helpx > Utils.EPSILON;
        }
        return helpx > Utils.EPSILON && helpy > Utils.EPSILON;

    }

    /**
     * 
     * @param depart
     * @param destination
     * @return
     */
    public boolean isPartiallyCompatibleWith(IPoint depart, IPoint destination) {
        Vector courantVector = Vector.createUnitVector(this.getPosition().getOrientation());
        Vector courantComposantx = new Vector(courantVector.getDeltaX(), 0);
        Vector courantComposanty = new Vector(0, courantVector.getDeltaY());

        Vector trajectoirVector = new Vector(depart, destination);

        return courantComposantx.scal(trajectoirVector) > Utils.EPSILON
                || courantComposanty.scal(trajectoirVector) > Utils.EPSILON;

    }

    /**
     * LATER test
     * 
     * @param point
     * @return
     */
    public Optional<IPoint> getAwayPoint(IPoint point) {
        Rectangle biggerRectangle;
        if (this.shape.getTypeEnum() == ShapeType.RECTANGLE) {
            Rectangle current = (Rectangle) this.shape;
            biggerRectangle = new Rectangle(current.getWidth() + Utils.TAILLE_BATEAU,
                    current.getHeight() + Utils.TAILLE_BATEAU, current.getOrientation());

        }

        else if (this.shape.getTypeEnum() == ShapeType.POLYGON) {
            var poly = (Polygon) this.shape;
            double r = poly.getMaxRadius();
            biggerRectangle = new Rectangle(r * 2 + Utils.TAILLE_BATEAU, r * 2 + Utils.TAILLE_BATEAU, 0);
        } else {// circle
            var circle = (Circle) this.shape;
            double r = circle.getRadius();
            biggerRectangle = new Rectangle(r * 2 + Utils.TAILLE_BATEAU, r * 2 + Utils.TAILLE_BATEAU, 0);
        }

        var rectanglePositioned = new RectanglePositioned(biggerRectangle, this.position);

        List<IPoint> points = rectanglePositioned.pointsOfRectangle(0.01);
        return points.stream().filter(p -> this.isCompatibleWith(point, p))
                .min((p1, p2) -> Double.compare(p1.distanceTo(point), p2.distanceTo(point)));

    }

    @Override
    public double getOrientation() {
        return this.position.getOrientation();
    }

    public double speedProvided(IPoint depart, IPoint destination) {
        return Math.cos(this.getOrientation() - new Vector(depart, destination).getOrientation()) * this.getStrength();
    }

    /**
     * 
     * @param depart
     * @param destination
     * @param courant
     * @param surface
     * @return
     */
    public IPoint maximalPointToStay(IPoint depart, IPoint destination) {
        Point2D current = new Point2D(depart.x(),depart.y());
        Point2D prev = current;
        Vector courantVector = Vector.createUnitVector(this.getPosition().getOrientation());
        Vector biggerStreamVector = courantVector.scaleVector(2);
        while (helpness(current, destination) > 0 && this.isPtInside(current)) {

            prev = current;

            current = current.getTranslatedBy(biggerStreamVector);

        }
        return prev;

    }

    public double helpness(IPoint depart, IPoint destination) {
        Vector courantVector = Vector.createUnitVector(this.getPosition().getOrientation());
        Vector trajectVector = new Vector(depart, destination);
        return courantVector.scal(trajectVector);
    }

}
