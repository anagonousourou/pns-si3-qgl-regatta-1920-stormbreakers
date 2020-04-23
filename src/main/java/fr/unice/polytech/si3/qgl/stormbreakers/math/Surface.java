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
	 * This methode must return the point to aim for so that you can go from depart
	 * to destination such that you will avoid Surface of course an effort must be
	 * made to reduce the distance to go round the surface We assume that the
	 * SegmentLine formed by [depart,destination] intersects With the surface
	 * 
	 * @param depart * @param destination
	 * @return
	 */
	default List<IPoint> avoidHit(IPoint depart, IPoint destination) {
        // TODO: 23/04/2020 Finish/Add tests for circle and polygon
		if (this.getShape().getType().equals("rectangle")) {
			return Surface.avoidHitRectangle(this, depart, destination);
		} else if (this.getShape().getType().equals("circle")) {
			Position position = new Position(this.x(), this.y());
			Circle c = (Circle) this.getShape();
			Recif recif = new Recif(position, new Rectangle(c.getRadius() * 2, c.getRadius() * 2, 0));
			return Surface.avoidHitRectangle(recif, depart, destination);
		} else {
			Polygon polygon = (Polygon) this.getShape();
			double r = polygon.getMaxRadius();
			Position position = new Position(this.x(), this.y());
			Recif recif = new Recif(position, new Rectangle(r * 2, r * 2, 0));
			return Surface.avoidHitRectangle(recif, depart, destination);
		}
	}

	private static List<IPoint> avoidHitRectangle(Surface self, IPoint depart, IPoint destination) {

		Point2D ptDest = new Point2D(destination);
		Point2D ptDepart = new Point2D(depart);
		Rectangle r = (Rectangle) self.getShape();

		Position ptThis = new Position(self.x(), self.y(), self.getOrientation());

		RectanglePositioned rectPos = new RectanglePositioned(new Rectangle(r.getWidth() + Utils.TAILLE_BATEAU,
				r.getHeight() + Utils.TAILLE_BATEAU, r.getOrientation()), ptThis);

		Vector trajetVector = new Vector(depart, destination);
		Optional<Point2D> firstPt = rectPos.corners().stream().filter(corner -> !self.intersectsWith(depart, corner))
				.max((a, b) -> Double.compare(trajetVector.scal(depart, a), trajetVector.scal(depart, b)));

		if (firstPt.isPresent()) {
			Point2D fpt = firstPt.get();
			LineSegment2D ls1 = new LineSegment2D(destination, fpt);

			if (self.intersectsWith(ls1)) {
				var secondPt = rectPos.corners().stream().filter(corner -> !self.intersectsWith(corner, destination)
						&& IPoint.ccw(ptDepart, fpt, ptDest) == IPoint.ccw(fpt, corner, ptDest)).findAny();
				if (secondPt.isPresent()) {
					return List.of(depart, fpt, secondPt.get(), destination);
				}
			} else {
				// TODO: 23/04/2020 Add test case
				return List.of(depart, fpt, destination);
			}
		}
		// TODO: 23/04/2020 Add test case
		return List.of(depart, destination);
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
