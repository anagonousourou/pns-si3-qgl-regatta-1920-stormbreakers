package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.exceptions.DegeneratedLine2DException;

/* File LineSegment2D.java 
 *
 * Project : Java Geometry Library
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */

// package

/**
 * Line segment, defined as the set of points located between the two end
 * points.
 */
public class LineSegment2D {

	/**
	 * Coordinates of starting point of the line
	 */
	protected double x0;
	protected double  y0;

	/**
	 * Direction vector of the line. dx and dy should not be both zero.
	 */
	protected double dx;
	protected double  dy;

	public static final  double ACCURACY = 1e-12;


	/**
	 * Checks if two line segment intersect. Uses the Point2D.ccw() method, which is
	 * based on Sedgewick algorithm.
	 * 
	 * 
	 * @param edge2 a line segment
	 * @return true if the 2 line segments intersect
	 */
	public boolean intersects( LineSegment2D edge2) {
		Point2D e1p1 = this.firstPoint();
		Point2D e1p2 = this.lastPoint();
		Point2D e2p1 = edge2.firstPoint();
		Point2D e2p2 = edge2.lastPoint();

		boolean b1 = Point2D.ccw(e1p1, e1p2, e2p1) * Point2D.ccw(e1p1, e1p2, e2p2) <= 0;
		boolean b2 = Point2D.ccw(e2p1, e2p2, e1p1) * Point2D.ccw(e2p1, e2p2, e1p2) <= 0;
		return b1 && b2;
	}

	/**
     * Checks if two line segment intersect. Uses the Point2D.ccw() method,
     * which is based on Sedgewick algorithm.
     * 
     * @param edge1 a line segment
     * @param edge2 a line segment
     * @return true if the 2 line segments intersect
     */
	public static boolean intersects(LineSegment2D edge1, LineSegment2D edge2) {
		Point2D e1p1 = edge1.firstPoint();
		Point2D e1p2 = edge1.lastPoint();
		Point2D e2p1 = edge2.firstPoint();
		Point2D e2p2 = edge2.lastPoint();

		boolean b1 = Point2D.ccw(e1p1, e1p2, e2p1)
				* Point2D.ccw(e1p1, e1p2, e2p2) <= 0;
		boolean b2 = Point2D.ccw(e2p1, e2p2, e1p1)
				* Point2D.ccw(e2p1, e2p2, e1p2) <= 0;
		return b1 &&b2;
    }

	/**
     * Returns the unique intersection of two straight objects. If the intersection
     * doesn't exist (parallel lines, short edge), return null.
     */
    public Point2D intersection( LineSegment2D line2) {
        // Compute denominator, and tests its validity
        double denom = this.dx * line2.dy - this.dy * line2.dx;
        if (Math.abs(denom) < LineSegment2D.ACCURACY)
            return null;

        // Compute position of intersection point
        double t = ((this.y0 - line2.y0) * line2.dx - (this.x0 - line2.x0) * line2.dy) / denom;
		Point2D point= new Point2D(this.x0 + t * this.dx, this.y0 + t * this.dy);
		
		// check if point is inside the bounds of the object
        if (this.contains(point) && line2.contains(point))
            return point;
        return null;
    }

	// ===================================================================
	// constructors

	/** Defines a new Edge with two extremities. */
	public LineSegment2D(IPoint point1, IPoint point2) {
		this(point1.x(), point1.y(), point2.x(), point2.y());
	}

	/** Defines a new Edge with two extremities. */
	public LineSegment2D(double x1, double y1, double x2, double y2) {
		this.x0 = x1;
		this.y0 = y1;
		this.dx = x2 - x1;
		this.dy = y2 - y1;

	}

	// ===================================================================
	// Methods specific to LineSegment2D

	/**
	 * Returns the opposite vertex of the edge.
	 * 
	 * @param point one of the vertices of the edge
	 * @return the other vertex, or null if point is nor a vertex of the edge
	 */
	public Point2D opposite(Point2D point) {
		if (point.equals(new Point2D(x0, y0)))
			return new Point2D(x0 + dx, y0 + dy);
		if (point.equals(new Point2D(x0 + dx, y0 + dy)))
			return new Point2D(x0, y0);
		return null;
	}

	// ===================================================================
	// methods implementing the CirculinearCurve2D interface

	/**
	 * Returns the length of the line segment.
	 */

	public double length() {
		return Math.hypot(dx, dy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 */
	public LineSegment2D parallel(double d) {
		// Checks line segment has a valid length
		double d2 = Math.hypot(dx, dy);
		if (Math.abs(d2) < LineSegment2D.ACCURACY) {
			throw new DegeneratedLine2DException("Can not compute parallel of degenerated edge");
		}

		// compute parallel line segment
		d2 = d / d2;
		return new LineSegment2D(x0 + dy * d2, y0 - dx * d2, x0 + dx + dy * d2, y0 + dy - dx * d2);
	}

	// ===================================================================
	// Methods implementing the Curve2D interface

	/**
	 * Returns the first point of the edge.
	 * 
	 * @return the first point of the edge
	 */

	public Point2D firstPoint() {
		return new Point2D(x0, y0);
	}

	/**
	 * Returns the last point of the edge.
	 * 
	 * @return the last point of the edge
	 */

	public Point2D lastPoint() {
		return new Point2D(x0 + dx, y0 + dy);
	}

	

	public Point2D point(double t) {
		t = Math.min(Math.max(t, 0), 1);
		return new Point2D(x0 + dx * t, y0 + dy * t);
	}

	/**
	 * Returns the LineSegment which start from last point of this line segment, and
	 * which ends at the fist point of this last segment.
	 */
	public LineSegment2D reverse() {
		return new LineSegment2D(x0 + dx, y0 + dy, x0, y0);
	}

	// ===================================================================
	// Methods implementing the Shape2D interface

	/**
	 * Returns true
	 */
	public boolean isBounded() {
		return true;
	}

	/**
	 * Returns true if the point (x, y) lies on the line covering the object, with
	 * precision given by ACCURACY.
	 */
	protected boolean supportContains(double x, double y) {
		double denom = Math.hypot(dx, dy);
		if (denom < LineSegment2D.ACCURACY) {
			throw new DegeneratedLine2DException("Erreur dans supportContains");
		}
		return (Math.abs((x - x0) * dy - (y - y0) * dx) / (denom * denom) < LineSegment2D.ACCURACY);
	}

	public boolean contains(double xp, double yp) {
		if (!this.supportContains(xp, yp))
			return false;

		// compute position on the line
		double t = positionOnLine(xp, yp);

		if (t < -ACCURACY)
			return false;

		else if (t - 1 > ACCURACY)
			return false;
		else{
			return true;
		}
		
	}

	public boolean contains(IPoint point){
		return this.contains(point.x(),point.y());
	}

	public double positionOnLine(double x, double y) {
        double denom = dx * dx + dy * dy;
        if (Math.abs(denom) < LineSegment2D.ACCURACY)
            throw new DegeneratedLine2DException("");
        return ((y - y0) * dy + (x - x0) * dx) / denom;
    }

	

	/**
	 * Get the distance of the point (x, y) to this edge.
	 */

	public double distance(double x, double y) {
		// In case of line segment with same extremities, computes distance to initial point 
        if (length() < LineSegment2D.ACCURACY)
        {
			return Math.hypot(this.x0-x, this.y0-y);
        }
        
        // compute position on the supporting line
    	Line2D line = this.supportingLine();
        double t = line.lineParametorOf(x, y);

        // clamp with parameterization bounds of edge
		t = Math.max(Math.min(t, 1), 0);
		
		// compute position of projected point on the edge
		Point2D proj = line.point(t);
		
		// return distance to projected point
		return proj.getDistanceTo(new Point2D(x, y));
	}

	double distance(IPoint point){
		return this.distance(point.x(),point.y());
	}

	private Line2D supportingLine() {
		return new Line2D(x0,y0, dx,dy);
	}

	public boolean almostEquals(double a, double b) {
		return Math.abs(a - b) <= 0.001;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LineSegment2D))
			return false;
		LineSegment2D that = (LineSegment2D) obj;

		// Compare each field
		if (!this.almostEquals(this.x0, that.x0))
			return false;
		if (!this.almostEquals(this.y0, that.y0))
			return false;
		if (!this.almostEquals(this.dx, that.dx))
			return false;
		if (!this.almostEquals(this.dy, that.dy))
			return false;

		return true;
	}

	// =================================
	// Methods implementing the Shape interface

	

	// ===================================================================
	// Methods implementing the Object interface

	@Override
	public String toString() {
		return "LineSegment2D[(" + x0 + "," + y0 + ")-(" + (x0 + dx) + "," + (y0 + dy) + ")]";
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + Double.valueOf(this.x0).hashCode();
		hash = hash * 31 + Double.valueOf(this.y0).hashCode();
		hash = hash * 31 + Double.valueOf(this.dx).hashCode();
		hash = hash * 31 + Double.valueOf(this.dy).hashCode();
		return hash;
	}
	/**
	 * Renvoie le point du segment de droite qui est le plus proche 
	 * @author Patrick
	 * @param point2d
	 * @return
	 */
	public Point2D closestPointTo(IPoint point2d){
		List<Point2D> points=new ArrayList<>();
		DoubleStream.iterate(0, d-> d <= 1.0, d-> d+0.05).forEach(d->
			points.add( this.point(d) )
		);
		var tmp=points.stream().min((p,pother)-> Double.compare(p.distanceTo(point2d),pother.distanceTo(point2d) ));
		if(tmp.isPresent()){
			return tmp.get();
		}
		//should never happen
		return null;
		
	}

	

	
	
}
