package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.RectanglePositioned;

public class Rectangle extends Shape {
    private double width;
    private double height;
    private double orientation;

    @JsonCreator
    public Rectangle(@JsonProperty("width") double width, @JsonProperty("height") double height,
            @JsonProperty("orientation") double orientation) {
        super("rectangle");
        this.width = width;
        this.height = height;
        this.orientation = orientation;
    }

    @JsonProperty("width")
    public double getWidth() {
        return width;
    }

    @JsonProperty("height")
    public double getHeight() {
        return height;
    }

    @JsonProperty("orientation")
    public double getOrientation() {
        return orientation;
    }

    @Override
    public boolean isPtInside(Point2D pt) {
        Point2D point2D = new Point2D(pt);
        // On ramene le plan pour que les cotes du rectangle soient sur les axes du
        // repere
        if (orientation != 0) {
            point2D = pt.getRotatedBy(-orientation);
        }
        return isPtInRectangle0(point2D);
    }

    /**
     * Vérifie si le point est dans le rectangle d'orientation 0
     * 
     * @param pt point à tester
     * @return true s'il est dedans, false sinon
     */
    private boolean isPtInRectangle0(IPoint pt) {
        double eps = Math.pow(10, -10);

        boolean xOk = between(pt.x(), -height / 2, height / 2, eps);
        boolean yOk = between(pt.y(), -width / 2, width / 2, eps);
        return xOk && yOk;
    }

    /**
     * true if min <= value <= max, false if not
     * 
     * @param value   value to test
     * @param min     minimum value
     * @param max     maximum value
     * @param epsilon error correction
     * @return true if min <= value <= max, false if not
     */
    private boolean between(double value, double min, double max, double epsilon) {
        return simpleBetween(value, min, max) || simpleBetween(value - epsilon, min, max)
                || simpleBetween(value + epsilon, min, max);
    }

    private boolean simpleBetween(double value, double min, double max) {
        return (min <= value) && (value <= max);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Rectangle))
            return false;
        Rectangle other = (Rectangle) obj;
        return other.width == width && other.height == height && other.orientation == orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, orientation);
    }

    @Override
    public String toString() {
        return getType() + ": (" + width + " " + height + " " + orientation + ")";
    }

    @Override
    public String toLogs() {
        return "R" + "(" + width + "|" + height + "|" + orientation + ")";
    }

    public Point2D findPointNearestToPosition(Position other, Position rectangle) {
        // TODO Auto-generated method stub
        if (Math.abs(orientation - Math.PI / 2) < 0.0001 || Math.abs(orientation - (2 * Math.PI / 2)) < 0.0001) {
            if (Math.abs(other.y() - rectangle.y()) < (height / 2)) {
                return new Point2D(rectangle.x(), other.y());
            } else if (other.x() - rectangle.y() < (-height / 2)) {
                return new Point2D(rectangle.x(), rectangle.y() - (height / 2));
            } else {
                return new Point2D(rectangle.x(), rectangle.y() + (height / 2));
            }
        } else if (Math.abs(orientation) < 0.0001 || Math.abs(orientation) - (2 * Math.PI) < 0.0001) {

            if (Math.abs(other.x() - rectangle.x()) < (height / 2)) {
                return new Point2D(other.x(), rectangle.y());
            } else if (other.x() - rectangle.x() < (-height / 2)) {
                return new Point2D(rectangle.x() - (height / 2), rectangle.y());
            } else {
                return new Point2D(rectangle.x() + (height / 2), rectangle.y());
            }

        } else {
            /*
             * EquationDroite droiteRect= new
             * EquationDroite(Math.cos(orientation)*(rectangle.x()+(this.height/2)),
             * Math.sin(orientation)*rectangle.y()+(this.width/2),
             * Math.cos(orientation)*other.x()-(this.height/2),
             * Math.sin(orientation)*rectangle.y()-(this.width/2));
             */

            // droiteRect.findPointIntersectPerpendicularLineByPos(other);
            RectanglePositioned rect = new RectanglePositioned(this, rectangle);
            /*
             * if(this.isPtInRectangle0(p)) { return p; }else if(other.x()<rectangle.x() ||
             * other.y()<)
             */
            Point2D p = new Point2D(other.x(), other.y());
            return rect.closestPointTo(p).get();
        }

    }

    public boolean haveGoodOrientation(Checkpoint cp, IPoint rectanglePosition, IPoint boatposition) {
        // TODO add comments and doc and change the type of parameter Checkpoint
        if (0 <= orientation && orientation < (Math.PI) / 2) {
            if (cp.getPosition().y() > rectanglePosition.y() - Math.sin(orientation) * width / 4
                    && cp.getPosition().x() - Math.cos(orientation) * height / 4 >= rectanglePosition.x()
                    && boatposition.x() < cp.getPosition().x() && boatposition.y() < cp.getPosition().y()) {
                return true;
            } else {
                return false;
            }
        } else if ((Math.PI / 2) <= orientation && orientation < Math.PI) {
            if (cp.getPosition().y() > rectanglePosition.y() - Math.sin(orientation) * width / 4
                    && cp.getPosition().x() - Math.cos(orientation) * height / 4 <= rectanglePosition.x()
                    && boatposition.x() < cp.getPosition().x() && boatposition.y() > cp.getPosition().y()) {
                return true;
            } else {
                return false;
            }
        } else if ((Math.PI) <= orientation && orientation < ((3 * Math.PI) / 2)) {
            if (cp.getPosition().x() < rectanglePosition.x() - Math.cos(orientation) * height / 4
                    && cp.getPosition().x() - Math.cos(orientation) * height / 4 <= rectanglePosition.x()
                    && boatposition.x() > cp.getPosition().x() && boatposition.y() > cp.getPosition().y()) {
                return true;
            } else {
                return false;
            }
        } else {
            if (cp.getPosition().x() > rectanglePosition.x() - Math.cos(orientation) * height / 4
                    && cp.getPosition().x() - Math.cos(orientation) * height / 4 >= rectanglePosition.x()
                    && boatposition.x() > cp.getPosition().x() && boatposition.y() < cp.getPosition().y()) {
                return true;
            } else {
                return false;
            }
        }
    }

}
