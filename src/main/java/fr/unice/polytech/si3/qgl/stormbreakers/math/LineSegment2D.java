package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.DoubleStream;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;

/**
 * Line segment, defined as the set of points located between the two end
 * points.
 */
// TODO: 16/04/2020 Remove @authors
public class LineSegment2D {

	/**
	 * Segment bounding points
	 */
	private final Point2D startPoint;
	private final Point2D endPoint;

	private final double length;
	public static final double ACCURACY = 1e-12;

	// ===================================================================
	// constructors

	/** Defines a new Edge with two extremities. */
	public LineSegment2D(IPoint point1, IPoint point2) {
		this(point1.x(), point1.y(), point2.x(), point2.y());
	}

	/** Defines a new Edge with two extremities. */
	public LineSegment2D(Point2D startPoint, Point2D endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;

		this.length = startPoint.distanceTo(endPoint);
		if (Utils.almostEquals(0.0, length))
			throw new DegeneratedLine2DException("Cannot create LineSegment of length zero !");
	}

	/** Defines a new Edge with two extremities. */
	public LineSegment2D(double x1, double y1, double x2, double y2) {
		this(new Point2D(x1, y1), new Point2D(x2, y2));
	}

	/**
	 * Checks if two line segment intersect. Uses the Point2D.ccw() method, which is
	 * based on Sedgewick algorithm.
	 * 
	 * 
	 * @param edge2 a line segment
	 * @return true if the 2 line segments intersect
	 */
	public boolean intersects(LineSegment2D edge2) {

		Point2D e1p1 = this.firstPoint();
		Point2D e1p2 = this.lastPoint();
		Point2D e2p1 = edge2.firstPoint();
		Point2D e2p2 = edge2.lastPoint();

		boolean b1 = IPoint.ccw(e1p1, e1p2, e2p1) * IPoint.ccw(e1p1, e1p2, e2p2) <= 0;
		boolean b2 = IPoint.ccw(e2p1, e2p2, e1p1) * IPoint.ccw(e2p1, e2p2, e1p2) <= 0;

		return b1 && b2;
	}

	/**
	 * Checks if two line segment intersect. Uses the Point2D.ccw() method, which is
	 * based on Sedgewick algorithm.
	 * 
	 * @param edge1 a line segment
	 * @param edge2 a line segment
	 * @return true if the 2 line segments intersect
	 */
	public static boolean intersects(LineSegment2D edge1, LineSegment2D edge2) {
		// TODO: 07/03/2020 Rework if time
		// TODO: 23/04/2020  Add lots of test cases HIGH PRIORITY
		Point2D e1p1 = edge1.firstPoint();
		Point2D e1p2 = edge1.lastPoint();
		Point2D e2p1 = edge2.firstPoint();
		Point2D e2p2 = edge2.lastPoint();

		boolean b1 = IPoint.ccw(e1p1, e1p2, e2p1) * IPoint.ccw(e1p1, e1p2, e2p2) <= 0;
		boolean b2 = IPoint.ccw(e2p1, e2p2, e1p1) * IPoint.ccw(e2p1, e2p2, e1p2) <= 0;
		return b1 && b2;
	}

	/**
	 * Returns the intersection with a given lineSegment
	 * 
	 * @return if exists the intersection point
	 * @author David Lebrisse - Stormbreakers
	 */
	public Optional<Point2D> intersection(LineSegment2D other) {
		Line2D thisSupport = this.getSupportingLine();
		Line2D otherSupport = other.getSupportingLine();

		Optional<Point2D> lineIntersectionOpt = thisSupport.intersect(otherSupport);

		if (lineIntersectionOpt.isPresent()) {
			// Supporting lines collide
			Point2D lineIntersection = lineIntersectionOpt.get();

			if (this.isCollinearPointOnSegment(lineIntersection) && other.isCollinearPointOnSegment(lineIntersection)) {
				// The collision point is on this segment
				// -> collision point
				return lineIntersectionOpt;
			} else {
				// The collision point is not on this segment
				// -> no collision point
				return Optional.empty();
			}
		} else {
			// Even supporting lines don't collide
			return Optional.empty();
		}
	}

	public IPoint intersectionPoint(IPoint depart, IPoint fin) {
		var optPoint = this.intersection(new LineSegment2D(depart, fin));
		if (optPoint.isPresent()) {
			return optPoint.get();
		} else {
			return null;
		}
	}

	// ===================================================================
	// Methods specific to LineSegment2D

	/**
	 * Given one of the bounding points returns the opposite one. Note: if any other
	 * value is used as input returns null
	 * 
	 * @param point one of the bounding points
	 * @return the opposite bounding point, or null if point is not a bounding point
	 * @author David Lebrisse - Stormbreakers
	 */
	public Point2D oppositeBoundingPoint(Point2D point) {
		if (point.equals(startPoint))
			return endPoint;
		if (point.equals(endPoint))
			return startPoint;
		return null;
	}

	// ===================================================================

	/**
	 * Returns the length of the line segment.
	 */
	public double length() {
		return startPoint.distanceTo(endPoint);
	}

	/**
	 * Computes this line segment moved by a given translation
	 * 
	 * @return the new line segment
	 */
	public LineSegment2D parallel(Vector translation) {
		return new LineSegment2D(startPoint.getTranslatedBy(translation), endPoint.getTranslatedBy(translation));
	}

	// ===================================================================

	/**
	 * Returns the first point of the edge.
	 */

	public Point2D firstPoint() {
		return startPoint;
	}

	/**
	 * Returns the last point of the edge.
	 */

	public Point2D lastPoint() {
		return endPoint;
	}

	/**
	 * Computes the <i>segment parameter</i> of the given point P for this line
	 * segment The parameter is the number k in [0,1] such that if the point belong
	 * to the line, its location is given by P(k)=P0+k*direction, where startPoint
	 * is P(0) and endPoint is P(1) Note: The point needs to be on the line.
	 * 
	 * @author David Lebrisse - Stormbreakers
	 */
	public double segmentParameterOf(Point2D p) {
		// TODO: 08/03/2020 Clamp k in [0,1] and corresponding tests
		Vector supportDirection = startPoint.getVectorTo(endPoint).normalize();
		Line2D arrangedSupport = new Line2D(startPoint, supportDirection);
		return arrangedSupport.lineParameterOf(p) / length;
	}

	/**
	 * Computes the coordinates of the point given by (x,y) where
	 * x=x0+segmentParameter*dx y=y0+segmentParameter*dy Where segmentParameter is
	 * in [0,1]
	 * 
	 * @return the projection
	 * @author David Lebrisse - Stormbreakers
	 */
	public Point2D point(double segmentParameter) {
		// TODO: 08/03/2020 Clamp k in [0,1] and corresponding tests
		Vector supportDirection = startPoint.getVectorTo(endPoint).normalize();
		Line2D arrangedSupport = new Line2D(startPoint, supportDirection);
		return arrangedSupport.pointFromLineParameter(segmentParameter * length);
	}

	// ===================================================================

	/**
	 * Returns true if the point (x, y) lies on the line covering the object, with
	 * precision given by ACCURACY.
	 */
	// TODO: 23/04/2020 Remove
	protected boolean supportContains(Point2D point2D) {
		return getSupportingLine().contains(point2D);
	}

	public boolean contains(IPoint point) {
		return Utils.almostEquals(0.0, this.distance(point));
		
	}

	public boolean contains(double x, double y) {
		return Utils.almostEquals(0.0, this.distance(x,y));
	}

	/**
	 * Gets the minimum distance from the point (x, y) to any point of this segment
	 * line.
	 * 
	 * @author David Lebrisse - Stormbreakers
	 */
	public double distance(double x, double y) {
		return distance(new Point2D(x, y));
	}

	/**
	 * Gets the minimum distance from the point P to any point of this segment line.
	 * 
	 * @author David Lebrisse - Stormbreakers
	 */
	public double distance(Point2D p) {
		Line2D support = this.getSupportingLine();
		Point2D projectedPOntoSupport = support.projectOnto(p);

		if (this.isCollinearPointOnSegment(projectedPOntoSupport)) {
			// The closest point to draw distance from
			// is in the segment line
			return support.distance(p);
		} else {
			// The closest point to draw distance from
			// is in one of the edges
			Point2D first = firstPoint();
			Point2D last = lastPoint();

			double distanceFromFirst = first.distanceTo(p);
			double distanceFromLast = last.distanceTo(p);
			return Math.min(distanceFromFirst, distanceFromLast);
		}
	}

	double distance(IPoint point) {
		return this.distance(point.x(), point.y());
	}

	public Line2D supportingLine() {
		return new Line2D(startPoint, endPoint);
	}

	/**
	 * Renvoie le point du segment de droite qui est le plus proche du point donné
	 * Computes the line segment point closest to a given point
	 * 
	 * @param point2D the given point
	 * @author David Lebrisse - Stormbreakers
	 */
	public Point2D closestPointTo(Point2D point2D) {
		Line2D support = this.getSupportingLine();
		Point2D projectedPOntoSupport = support.projectOnto(point2D);

		if (this.isCollinearPointOnSegment(projectedPOntoSupport)) {
			// The closest point to draw distance from
			// is in the segment line
			return projectedPOntoSupport;
		} else {
			// The closest point to draw distance from
			// is in one of the edges
			Point2D first = firstPoint();
			Point2D last = lastPoint();

			double distanceFromFirst = first.distanceTo(point2D);
			double distanceFromLast = last.distanceTo(point2D);
			double min = Math.min(distanceFromFirst, distanceFromLast);

			if (Utils.almostEquals(min, distanceFromFirst))
				// TODO: 23/04/2020 Test this case
				return first;
			else
				return last;
		}

	}

	/**
	 * Computes the line supporting this line segment
	 * 
	 * @return supporting line
	 * @author David Lebrisse - Stormbreakers
	 */
	public Line2D getSupportingLine() {
		return new Line2D(this.firstPoint(), this.lastPoint());
	}

	/**
	 * Computes the line supporting this line segment
	 * 
	 * @return supporting line
	 * @author David Lebrisse - Stormbreakers
	 */
	public Point2D getMiddle() {
		Point2D from = firstPoint();
		Point2D to = lastPoint();
		return new Point2D(0.5 * (from.x() + to.x()), 0.5 * (from.y() + to.y()));
	}

	/**
	 * Checks if the given collinearPoint is in this line segment
	 * 
	 * @return true if it is the case, false if not
	 * @author David Lebrisse - Stormbreakers
	 */
	public boolean isCollinearPointOnSegment(Point2D collinearPoint) {
		Point2D from = firstPoint();
		Point2D to = lastPoint();
		double totalDistance = from.distanceTo(collinearPoint) + to.distanceTo(collinearPoint);
		return Utils.almostEqualsBoundsIncluded(this.length(), totalDistance);
	}

	// ===================================================================
	// Methods implementing the Object interface

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof LineSegment2D)) return false;
		LineSegment2D that = (LineSegment2D) obj;

		// Compare each field
		return this.startPoint.equals(that.startPoint) && this.endPoint.equals(that.endPoint);
	}

	@Override
	public String toString() {
		return "LineSegment2D[(" + startPoint.x() + "," + startPoint.y() + ")-(" + endPoint.x() + "," + endPoint.y()
				+ ")]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(startPoint, endPoint);
	}

	/**
	 * Renvoie le point du segment de droite qui est le plus proche
	 * 
	 * @author Patrick
	 * @param point2d
	 * @return
	 */
	// TODO: 23/04/2020 Remove
	public Point2D closestPointTo(IPoint point2d) {
		List<Point2D> points = new ArrayList<>(20);
		DoubleStream.iterate(0, d -> d <= 1.0, d -> d + 0.05).forEach(d -> points.add(this.point(d)));
		var tmp = points.stream().min((p, pother) -> Double.compare(p.distanceTo(point2d), pother.distanceTo(point2d)));
		if (tmp.isPresent()) {
			return tmp.get();
		}
		// should never happen
		return null;

	}

	/**
	 * 
	 * @param step must be within 0 and 1
	 * @return
	 */
	public List<IPoint> pointsOfSegment(double step) {
		List<IPoint> points = new ArrayList<>(20);
		DoubleStream.iterate(0, d -> d <= 1.0, d -> d + step).forEach(d -> points.add(this.point(d)));

		return points;
	}

}
