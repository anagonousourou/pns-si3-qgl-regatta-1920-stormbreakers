package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public interface Surface extends IPoint, Orientable {
	static int TAILLE_BATEAU = 4;

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
			
			return new RectanglePositioned((Rectangle) this.getShape(), this).intersectsWith(lineSegment2D);
		} else {
			
			return false;
		}
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
			y = this.y() - (c.getRadius() + TAILLE_BATEAU) * Math.sin(orientation);
		} else {
			y = this.y() + (c.getRadius() + TAILLE_BATEAU) * Math.sin(orientation);
		}
		x = perpTrajet.calculateValueX(y);
		IPoint pt = new Point2D(x, y);
		LineSegment2D sD = getSegmentLineTranslation(depart, pt, thisPoint);
		
		if (!c.intersect(sD).isEmpty()) {
			list.addAll(avoidHitCircle(depart, pt));
		}
		{
			System.out.println("notIntersect" + sD + "-" + c.toString());
		}
		list.add(pt);
		LineSegment2D sF =  getSegmentLineTranslation(pt, destination,thisPoint);
		if (!c.intersect(sF).isEmpty()) {
			list.addAll(avoidHitCircle(pt, destination));
		} else {
			System.out.println("notIntersect" + sF + "-" + c.toString());
		}
		return list;

	}

	public default LineSegment2D getSegmentLineTranslation(IPoint dep,IPoint dest, IPoint p) {
		Point2D depart = new Point2D(dep.x()-p.x(),dep.y()-p.y());
		Point2D destination = new Point2D(dest.x()-p.x(),dest.y()-p.y());		
		return new LineSegment2D(depart, destination);
	}
	
	public default List<IPoint> avoidHitRectangle(IPoint depart, IPoint destination) {

		List<IPoint> list = new ArrayList<IPoint>();

		Rectangle r = (Rectangle) this.getShape();
		Double heightRect = (r.getHeight() / 2);
		Double widthtRect = (r.getWidth() / 2);

		double orientation = this.getOrientation();
		Point2D ptDepart = new Point2D(depart.x(), depart.y());
		Point2D ptDest = new Point2D(destination.x(), destination.y());
		Point2D ptThis = new Point2D(this.x(), this.y());

		Point2D PT_BD = new Point2D(ptThis.x() + heightRect + TAILLE_BATEAU, ptThis.y() - widthtRect - TAILLE_BATEAU);
		Point2D PT_BG = new Point2D(ptThis.x() - heightRect - TAILLE_BATEAU, ptThis.y() - widthtRect - TAILLE_BATEAU);
		Point2D PT_HD = new Point2D(ptThis.x() + heightRect + TAILLE_BATEAU, ptThis.y() + widthtRect + TAILLE_BATEAU);
		Point2D PT_HG = new Point2D(ptThis.x() - heightRect - TAILLE_BATEAU, ptThis.y() + widthtRect + TAILLE_BATEAU);
		
		
		ptDepart = ptDepart.getRotatedBy(-orientation);
		ptDest = ptDest.getRotatedBy(-orientation);
		ptThis = ptThis.getRotatedBy(-orientation);

		if (ptThis.y() + (widthtRect) < depart.y()||ptThis.y() + (widthtRect) < destination.y()) {
			EquationDroite eq = new EquationDroite(ptDepart, ptDest);
			double xonEq= eq.calculateValueX(ptThis.y());
			if (xonEq < ptThis.x()) {
				list.add(PT_HG);
			} else {
				list.add(PT_HD);
			}
		} else if (ptThis.y() + (widthtRect) > depart.y()||ptThis.y() + (widthtRect) > destination.y())  {
			EquationDroite eq = new EquationDroite(ptDepart, ptDest);
			double xonEq= eq.calculateValueX(ptThis.y());
			if (xonEq < ptThis.x()) {
				list.add(PT_BG);
			} else {
				list.add(PT_BD);
			}
		} else {
			EquationDroite eq = new EquationDroite(ptDepart, ptDest);
			double yCroisementCentreRectEnX = eq.evalY(ptThis.x());
			if (yCroisementCentreRectEnX >= ptThis.y()) {

				if (depart.x() > destination.x()) {
					list.add(PT_HD);
					list.add(PT_HG);
				} else {
					list.add(PT_HG);
					list.add(PT_HD);
				}
			} else {
				if (depart.x() > destination.x()) {
					list.add(PT_BD);
					list.add(PT_BG);
				} else {
					list.add(PT_BG);
					list.add(PT_BD);
				}
			}
			
		}
		return list;
	}
}
