package fr.unice.polytech.si3.qgl.stormbreakers.math;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

public class EquationDroite {
	//ax+b
	private double slope; // a
	private double yIntercept; // b

    public EquationDroite(Point2D p1, Point2D p2) {
        this(p1.x(),p1.y(), p2.x(),p2.y());
    }

	public EquationDroite(double slope, double yIntercept) {
		this.slope = slope;
		this.yIntercept = yIntercept;
	}

	public EquationDroite(double x1, double y1, double x2, double y2) {
		this.slope =(y2-y1)/(x2-x1);
		this.yIntercept=y1-this.slope*x1;
	}
  
  // CHANGE: REMOVED resolutionValeurB ?
  /*
  private double resolutionValeurB(double c) {
		return this.slope*c+this.yIntercept;
	}
  */
  
  public double orientationDroite() {
		return Math.atan(slope);
	}

  //CHANGE: REMOVED OLD foundValueX | YOUR foundLeadingCoefficient
  /*
  public double foundLeadingCoefficient() {
		//LATER Trouver meilleur nom
		return -(this.yIntercept)/this.slope;
	}
  */
  
	/**
	 * Returns y = f(x)
	 * @param x f input
	 */
 
  //CHANGE: YOUR resolutionValY
	double evalY(double x) {
		return this.slope *x+this.yIntercept;
	}
  
  /**
	 * trouve la valeur de x pour une valeur de y fixée
	 * @return
	 */
	public double calculateValueX(double y) {
		return (y/slope)-yIntercept;
	}

	/**
	 * Returns the perpendicular
	 * going through P
	 * @param P the base point
	 */
	EquationDroite findEqPerpendicularLineByPos(IPoint P) {
		double lineA= -(1/ slope);
		EquationDroite e= new EquationDroite(-lineA, P.y());
		double lineB=e.evalY(P.x());
		return new EquationDroite(lineA, lineB);
	}
  
  // CHANGE: REMOVED findPointIntersectPerpendicularLineByPos
  /*
  public Point2D findPointIntersectPerpendicularLineByPos(Position p) {
		EquationDroite perpendicular =findEqPerpendicularLineByPos(p);
		EquationDroite intersectPerpAndThis = new EquationDroite(perpendicular.getSlope()-this.getSlope(),perpendicular.getY_Intercept()-this.getY_Intercept());
		double x= intersectPerpAndThis.foundLeadingCoefficient();
		double y= intersectPerpAndThis.resolutionValeurB(x);
		return new Point2D(x, y);
	}
  */
  

    /**
     * Finds x solution of y1(x)=y2(x)
     * where y1(x) is this equation
     * @param other y2(x)
     * @return x the common solution
     */
    public double findCommonSolution(EquationDroite other) {
        // On cherche x t.q. : y1(x) = y2(x)
        //  soit : a1*x+b1 = a2*x+b2
        //  d'où : (a1-a2) * x = (b2-b1)
        // On obtiens : x = (b2-b1)/(a1-a2)
        return (other.yIntercept - this.yIntercept) / (this.slope - other.slope );
    }

    // CHANGE: OLD getA
    double getSlope() {
        return slope;
    }
  
    // CHANGE: OLD getB
    double getY_Intercept() {
        return yIntercept;
    }
}
