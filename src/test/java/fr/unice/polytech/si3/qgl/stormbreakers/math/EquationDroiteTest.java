package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

public class EquationDroiteTest {
	private EquationDroite eqD1, eqD2, eqD3;
	private Position a, b, c;

	@BeforeEach
	void setUp(){
		a = new Position(8, 4, 3 );
	    b = new Position(-2, 6, 3 );
        c = new Position(3, -1.0, 3 );
        eqD1 = new EquationDroite(a, b);
        eqD2 = new EquationDroite(b.getX(), b.getY(), c.getX(), c.getY());
        eqD3 = new EquationDroite(1, 2);
    }

	@Test
	void findEqPerpendicularLineByPosTest() {
		System.out.println("1 : (" + eqD1.getA() + ", " + eqD1.getB() + ")");
		System.out.println("2 : (" + eqD2.getA() + ", " + eqD2.getB() + ")");
		System.out.println("3 : (" + eqD3.getA() + ", " + eqD3.getB() + ")");
		
		EquationDroite tmp = new EquationDroite(5, -8.5);
		EquationDroite result = eqD1.findEqPerpendicularLineByPos(c);
		assertEquals(tmp.getA(), result.getA());
		assertEquals(tmp.getB(), result.getB());

		tmp = new EquationDroite(0.7142857142857, -1.714285714286);
		result = eqD2.findEqPerpendicularLineByPos(a);
		assertTrue(Math.abs(result.getA() - tmp.getA()) <= 0.000001);
		assertTrue(Math.abs(result.getB() - tmp.getB()) <= 0.000001);
		
		tmp = new EquationDroite(-1, 4);
		result = eqD3.findEqPerpendicularLineByPos(b);
		assertEquals(tmp.getA(), result.getA());
		assertEquals(tmp.getB(), result.getB());
	}
}
