package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;

public interface Surface extends IPoint, Orientable {

	public Shape getShape();

	// Une surface a une shape, des coordonnées x,y et une orientation
	// l'orientation ici est l'oriention de position pas celle de la shape
	public default boolean isPtInside(IPoint point) {
		// On se replace par rapport au centre de la forme

		Point2D pt = new Point2D(point.x() - this.x(), point.y() - this.y());

		double orientation = this.getOrientation();
		// On compense l'orientation du checkpoint
		if (orientation != 0)
			pt = pt.getRotatedBy(-orientation);
		return this.getShape().isPtInside(pt);
	}

	public default boolean intersectsWith(LineSegment2D lineSegment2D) {
		if (this.getShape().getType().equals("rectangle")) {

			return new RectanglePositioned((Rectangle) this.getShape(),
					new Position(this.x(), this.y(), this.getOrientation())).intersectsWith(lineSegment2D);
		} else if (this.getShape().getType().equals("polygon")) {
			Polygon shape = (Polygon) this.getShape();
			
			return shape.generateBordersInThePlan(this).stream().anyMatch(lineSegment2D::intersects);
		}

		else {
			//TODO
			return false;
		}
	}

	public default boolean intersectsWith(IPoint fp, IPoint sp) {
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
	public default List<IPoint> avoidHit(IPoint depart, IPoint destination) {
		// LATER
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
						&& Point2D.ccw(ptDepart, fpt, ptDest) == Point2D.ccw(fpt, corner, ptDest)).findAny();
				if (secondPt.isPresent()) {
					return List.of(depart, fpt, secondPt.get(), destination);
				}
			} else {
				return List.of(depart, fpt, destination);
			}
		}

		return List.of(depart,destination);
	}
}