package fr.unice.polytech.si3.qgl.stormbreakers.math;

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
		slope =(y2-y1)/(x2-x1);
		EquationDroite e= new EquationDroite(-slope, y1);
		yIntercept = e.evalY(x1);
	}

	/**
	 * Returns y = f(x)
	 * @param x f input
	 */
	double evalY(double x) {
		return this.slope *x+this.yIntercept;
	}

	/**
	 * Returns the perpendicular
	 * going through P
	 * @param P the base point
	 */
	EquationDroite findEqPerpendicularLineByPos(Point2D P) {
		double lineA= -(1/ slope);
		EquationDroite e= new EquationDroite(-lineA, P.y());
		double lineB=e.evalY(P.x());
		return new EquationDroite(lineA, lineB);
	}

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

    double getSlope() {
        return slope;
    }
    double getY_Intercept() {
        return yIntercept;
    }
}
