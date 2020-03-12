package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

public class Sommet {
	private IPoint point ;
	
	 public Sommet(IPoint pt) {
		point =new Point2D(pt);
	}
	 public Sommet(double x, double y) {
			point =new Point2D(x,y);
	}
	public IPoint getPoint() {
		return point;
	}
	public String toString() {
		return point.toString();
	}


		
	
}
