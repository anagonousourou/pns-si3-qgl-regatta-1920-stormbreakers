package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;

public class SurfaceTest {
	private Surface s;
	private Surface s2;
	private Position depart;
	private Position destination;
	private Position d1;
	private Position a1;
	private Position d2;
	private Position d3;
	private Rectangle rectangle;
	private Circle cercle;

	@BeforeEach
	public void setUp() {
		rectangle = new Rectangle(1000.0, 800.0, 0.0);
		cercle = new Circle(50);
		s = new Recif(new Position(1000, 500.0), rectangle);
		s2 = new Recif(new Position(0, 0), cercle);
		depart = new Position(0, 0);
		destination = new Position(1200.0, 1200.0);
		d1 = new Position(-70, 20);
		a1 = new Position(90, -40);
		d2 = new Position(-7, 7);
		d3 = new Position(7, -7);
	}

	@Test
	public void avoidHitRectangleTest() {
		// Test Rectangle
		LineSegment2D l =  new LineSegment2D(new Position(0, 1000),new Position(1200,1200));
		assertFalse(s.intersectsWith(l));
		assertTrue(avoidHitRectangleTest(depart, destination));
		//assertTrue(avoidHitRectangleTest(destination, depart));
		//assertTrue(avoidHitRectangleTest(d1, a1));
		//assertTrue(avoidHitRectangleTest(d2, a1));
		//assertTrue(avoidHitRectangleTest(a1, d2));
		///assertTrue(avoidHitRectangleTest(d3, a1));
		//assertEquals(1, s.avoidHit(destination, depart).size());
		//assertEquals(1, s.avoidHit(depart, destination).size());
		//assertEquals(1, s.avoidHit(d2, a1).size());

		// à completer avec tous les cas

		// test Circle
		//System.out.println(s2.avoidHit(d1, a1));
	}

	private boolean avoidHitRectangleTest(Position depart, Position destination) {
		List<IPoint> list = s.avoidHit(depart, destination);
		IPoint thisPoint = new Point2D(s.x(), s.y());
		list.add(0, depart);
		list.add(destination);
		System.out.println("-----\n"+list+"\n-----");
		for (int i = 0; i < list.size() - 1; i++) {
			LineSegment2D l =  new LineSegment2D(list.get(i), list.get(i + 1));
			if (s.intersectsWith(l)) {
				System.out.println(l);
				return false;
			}

		}
		return true;
	}

	private boolean avoidHitCircleTest(Position depart, Position destination) {
		IPoint thisPoint = new Point2D(s2.x(), s2.y());
		List<IPoint> list = s2.avoidHit(depart, destination);
		list.add(0, depart);
		list.add(destination);
		for (int i = 0; i < list.size() - 1; i++) {
			LineSegment2D l =  s2.getSegmentLineTranslation(list.get(i), list.get(i + 1),thisPoint);
			if (s.intersectsWith(l)) {
				return false;
			}
		}
		return true;
	}
	
	@Test
	public void testgetSegmentLineTranslation() {
		IPoint depart = new Point2D(5,5);
		IPoint destination = new Point2D(-10,-10);
		IPoint thisPoint = new Point2D(s.x(), s.y());
		assertEquals(s.getSegmentLineTranslation(depart, destination, thisPoint).firstPoint().x(),(-995));
		assertEquals(s.getSegmentLineTranslation(depart, destination, thisPoint).firstPoint().y(),(-495));

		assertEquals(s.getSegmentLineTranslation(depart, destination, thisPoint).lastPoint().x(),(-1010));
		assertEquals(s.getSegmentLineTranslation(depart, destination, thisPoint).lastPoint().y(),(-510));
	}
}
