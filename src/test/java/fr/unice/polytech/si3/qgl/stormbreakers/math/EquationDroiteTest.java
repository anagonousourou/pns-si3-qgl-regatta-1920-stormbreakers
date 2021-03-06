package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EquationDroiteTest {
	private EquationDroite eqD1, eqD2, eqD3;
	private Point2D a, b, c;

	@BeforeEach
	void setUp(){
		a = new Point2D(8, 4);
	    b = new Point2D(-2, 6);
        c = new Point2D(3, -1.0);
        eqD1 = new EquationDroite(a, b);
        eqD2 = new EquationDroite(b.x(), b.y(), c.x(), c.y());
        eqD3 = new EquationDroite(1, 2);
	}
	
	@Test
	public void constructorsTest(){
		assertEquals((double)-7/5, eqD2.getSlope() , 1e-3);
		assertEquals((double)16/5, eqD2.getYIntercept(), 1e-3);
		
	} 

	@Test
	void findEqPerpendicularLineByPosTest() {
		EquationDroite tmp = new EquationDroite(5, -16);
		EquationDroite result = eqD1.findEqPerpendicularLineByPos(c);
		assertEquals(tmp.getSlope(), result.getSlope());
		assertEquals(tmp.getYIntercept(), result.getYIntercept());

		tmp = new EquationDroite(0.7142857142857, -1.714285714286);
		result = eqD2.findEqPerpendicularLineByPos(a);
		assertTrue(Math.abs(result.getSlope() - tmp.getSlope()) <= 0.000001);
		assertTrue(Math.abs(result.getYIntercept() - tmp.getYIntercept()) <= 0.000001);
		
		tmp = new EquationDroite(-1, 4);
		result = eqD3.findEqPerpendicularLineByPos(b);
		assertEquals(tmp.getSlope(), result.getSlope());
		assertEquals(tmp.getYIntercept(), result.getYIntercept());
	}

    @Test
    void findCommonSolutionTest() {
		EquationDroite equationDroite1 = new EquationDroite(-3,5);
		EquationDroite equationDroite2 = new EquationDroite(1.0/3.0,3);
		assertEquals(3.0/5.0, equationDroite1.findCommonSolution(equationDroite2));


		EquationDroite equationDroite3 = new EquationDroite(8,3);
		EquationDroite equationDroite4 = new EquationDroite(12,3);
		assertEquals(0.0, equationDroite3.findCommonSolution(equationDroite4),Utils.EPSILON);

		EquationDroite equationDroite5 = new EquationDroite(0,3);
		EquationDroite equationDroite6 = new EquationDroite(12,0);
		assertEquals(0.25, equationDroite5.findCommonSolution(equationDroite6),Utils.EPSILON);
		assertEquals(0.25, equationDroite6.findCommonSolution(equationDroite5),Utils.EPSILON);

		EquationDroite equationDroite7 = new EquationDroite(8,3);
		EquationDroite equationDroite8 = new EquationDroite(0,0);
		assertEquals(-0.38, equationDroite7.findCommonSolution(equationDroite8),Utils.EPSILON_COLLISION);
		assertEquals(-0.38, equationDroite8.findCommonSolution(equationDroite7),Utils.EPSILON_COLLISION);

	}
}
