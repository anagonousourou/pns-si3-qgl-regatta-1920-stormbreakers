package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;

/**
 * Represents entities that have a positioned shape
 */

public interface Surface extends IPoint, Orientable {

	Shape getShape();

	public default boolean isInsideOpenSurface(IPoint point) {

		return this.getShape().isInsideOpenShape(point);
	}

	default boolean isPtInside(IPoint point) {
		return getShape().isPtInside(point);
	}

	default boolean intersectsWith(LineSegment2D lineSegment2D) {
		return getShape().collidesWith(lineSegment2D);
	}

	default boolean intersectsWithWrappingSurface(double margin, LineSegment2D lineSegment2D) {
		return this.getShape().wrappingShape(margin).collidesWith(lineSegment2D);
	}

	default boolean collidesWith(Surface other) {
		return this.getShape().collidesWith(other.getShape());
	}

	default boolean intersectsWith(IPoint fp, IPoint sp) {
		LineSegment2D segment2d = new LineSegment2D(fp, sp);
		return this.intersectsWith(segment2d);
	}

	

	/**
	 * Give the intersection of segment [depart,arrive] if arrivee is outside the
	 * Surface otherwise return arrive if arrive is inside , we assume depart is
	 * inside the Surface
	 * 
	 * @param depart
	 * @param arrivee
	 * @return
	 */
	public default IPoint limitToSurface(IPoint depart, IPoint arrivee) {

		if (this.isInsideOpenSurface(arrivee)) {
			// TODO: 23/04/2020 Test this case
			return arrivee;
		}

		else {
			return this.getShape().intersectionPoint(depart, arrivee);
		}
	}

	public default boolean isInsideWrappingSurface(double margin, IPoint point) {
		return this.getShape().wrappingShape(margin).isPtInside(point);
	}
}
