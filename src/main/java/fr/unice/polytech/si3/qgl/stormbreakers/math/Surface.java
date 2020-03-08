package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

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
					new Position(this.x(), this.y(), getOrientation())).intersectsWith(lineSegment2D);
		} else if (this.getShape().getType().equals("polygon")) {
			Polygon shape = (Polygon) this.getShape();
			return shape.generateBordersInThePlan(this).stream().anyMatch(lineSegment2D::intersects);
		}

		else {
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
	 * @param depart
	 * @param destination
	 * @return
	 */
	public default IPoint avoidPoint(IPoint depart, IPoint destination) {
		// LATER
		return null;
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
			return this.avoidHitRectangle(depart, destination);
		} else if (this.getShape().getType().equals("circle")) {
			return this.avoidHitCircle(depart, destination);
		} else {
			IPoint thisPoint = new Position(this.x(), this.y());
			return getShape().avoidPoint(depart, destination, thisPoint);
		}
	}

	public default List<IPoint> avoidHitCircle(IPoint depart, IPoint destination) {
		List<IPoint> list = new ArrayList<>();

		EquationDroite trajet = new EquationDroite(depart.x(), depart.y(), destination.x(), destination.y());

		IPoint thisPoint = new Point2D(this.x(), this.y());
		double intersectionCentreCercleEnX = trajet.evalY(this.x());
		EquationDroite perpTrajet = trajet.findEqPerpendicularLineByPos(thisPoint);
		double orientation = perpTrajet.orientationDroite();

		Circle c = (Circle) this.getShape();
		double y;
		double x;
		if (intersectionCentreCercleEnX <= this.y()) {
			y = this.y() - (c.getRadius() + Utils.TAILLE_BATEAU) * Math.sin(orientation);
		} else {
			y = this.y() + (c.getRadius() + Utils.TAILLE_BATEAU) * Math.sin(orientation);
		}
		x = perpTrajet.calculateValueX(y);
		IPoint pt = new Point2D(x, y);
		LineSegment2D sD = getSegmentLineTranslation(depart, pt, thisPoint);

		if (!c.intersect(sD).isEmpty()) {
			list.addAll(avoidHitCircle(depart, pt));
		}
		
		list.add(pt);
		LineSegment2D sF = getSegmentLineTranslation(pt, destination, thisPoint);
		if (!c.intersect(sF).isEmpty()) {
			list.addAll(avoidHitCircle(pt, destination));
		} else {
			System.out.println("notIntersect" + sF + "-" + c.toString());
		}
		list.add(0,depart);
		list.add(destination);
		return list;

	}

	public default LineSegment2D getSegmentLineTranslation(IPoint dep, IPoint dest, IPoint p) {
		Point2D depart = new Point2D(dep.x() - p.x(), dep.y() - p.y());
		Point2D destination = new Point2D(dest.x() - p.x(), dest.y() - p.y());
		return new LineSegment2D(depart, destination);
	}

	public default List<IPoint> avoidHitRectangle(IPoint depart, IPoint destination) {

		Point2D ptDest = new Point2D(destination);
		Point2D ptDepart = new Point2D(depart);
		Rectangle r = (Rectangle) this.getShape();

		Position ptThis = new Position(this.x(), this.y(), this.getOrientation());

		RectanglePositioned rectPos = new RectanglePositioned(
				new Rectangle(r.getWidth() + Utils.TAILLE_BATEAU, r.getHeight() + Utils.TAILLE_BATEAU, r.getOrientation()), ptThis);

		Vector trajetVector = new Vector(depart, destination);
		Optional<Point2D> firstPt = rectPos.corners().stream().filter(corner -> !this.intersectsWith(depart, corner))
				.max((a, b) -> Double.compare(trajetVector.scal(depart, a), trajetVector.scal(depart, b)));

		if (firstPt.isPresent()) {
			Point2D fpt = firstPt.get();
			LineSegment2D ls1 = new LineSegment2D(destination, fpt);

			if (this.intersectsWith(ls1)) {
				var secondPt = rectPos.corners().stream()
						.filter(corner -> !this.intersectsWith(corner, destination)
								&& Point2D.ccw(ptDepart, fpt, ptDest) == Point2D.ccw(fpt, corner, ptDest))
						.findAny();
				if (secondPt.isPresent()) {
					return List.of(depart, fpt, secondPt.get(), destination);
				}
			} else {
				return List.of(depart, fpt, destination);
			}
		}

		return List.of();
	}
}
