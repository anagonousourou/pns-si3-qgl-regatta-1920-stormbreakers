package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

public class EquationDroite {
	//ax+b
	private double a;
	private double b;
	
	public EquationDroite(double a,double b) {
		this.a=a;
		this.b=b;
	}
	public EquationDroite(Position p1, Position p2) {
		a=(p2.y()-p1.y())/(p2.x()-p1.x()); 
		EquationDroite e= new EquationDroite(-a, p1.y());
		b=e.resolutionValeurB(p1.x());
	}
	
	
	public EquationDroite(double x1, double y1, double x2, double y2) {
		a=(y2-y1)/(x2-x1); 
		EquationDroite e= new EquationDroite(-a, y1);
		b=e.resolutionValeurB(x1);
	}
	
	private double resolutionValeurB(double c) {
		System.out.println("ABC:("+a+","+b+","+c+")");
		return this.a*c+this.b;
	}
	/**
	 *
	 * @return
	 */
	public double foundValueX() {
		//TODO Trouver meilleur nom
		return -(this.b)/this.a;
	}
	
	EquationDroite findEqPerpendicularLineByPos(Position p) {
		double lineA= -(1/a);
		EquationDroite e= new EquationDroite(-lineA, p.y());
		System.out.println("AB:("+e.getA()+","+e.getB()+")");
		double lineB=e.resolutionValeurB(p.x()); 
		return new EquationDroite(lineA, lineB);
	}
	
	public Point2D findPointIntersectPerpendicularLineByPos(Position p) {
		EquationDroite perpendicular =findEqPerpendicularLineByPos(p);
		EquationDroite intersectPerpAndThis = new EquationDroite(perpendicular.getA()-this.getA(),perpendicular.getB()-this.getB());
		double x= intersectPerpAndThis.foundValueX();
		double y= intersectPerpAndThis.resolutionValeurB(x);
		return new Point2D(x, y);
	}

	double getA() {
		return a;
	}
	double getB() {
		return b;
	}
}
