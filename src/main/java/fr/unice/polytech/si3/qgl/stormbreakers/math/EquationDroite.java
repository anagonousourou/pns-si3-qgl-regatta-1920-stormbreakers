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
		a=(p2.getY()-p1.getY())/(p2.getX()-p1.getX()); 
		EquationDroite e= new EquationDroite(-a, p1.getY());
		b=resolutionValeurB(p1.getX());
	}
	
	
	public EquationDroite(double x1, double y1, double x2, double y2) {
		a=(y2-y1)/(x2-x1); 
		EquationDroite e= new EquationDroite(-a, y1);
		b=resolutionValeurB(x1);
	}
	
	public double resolutionValeurB(double c) {
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
		EquationDroite e= new EquationDroite(-a, p.getY());
		double lineB=e.resolutionValeurB(p.getX()); 
		return new EquationDroite(lineA, lineB);
	}
	
	public Position findPointIntersectPerpendicularLineByPos(Position p) {
		EquationDroite perpendicular =findEqPerpendicularLineByPos(p);
		EquationDroite intersectPerpAndThis = new EquationDroite(perpendicular.getA()-this.getA(),perpendicular.getB()-this.getB());
		double x= intersectPerpAndThis.foundValueX();
		double y= intersectPerpAndThis.resolutionValeurB(x);
		return new Position(x, y);
	}

	private double getA() {
		// TODO Auto-generated method stub
		return a;
	}
	private double getB() {
		// TODO Auto-generated method stub
		return b;
	}
}
