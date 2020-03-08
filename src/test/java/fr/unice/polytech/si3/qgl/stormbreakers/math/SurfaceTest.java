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
	private Surface orientedSurface;
	private Position depart;
	private Position destination;
	private Position d1;
	private Position a1;
	private Position d2;
	private Position a2;
	private Position d3;
	private Position a3;
	
	private Rectangle rectangle;
	private Rectangle rectangleOriented;
	private Circle cercle;

	@BeforeEach
	public void setUp() {
		rectangle = new Rectangle(1000.0, 800.0, 0.0);
		rectangleOriented=new Rectangle(500, 500, 0.78539);
		orientedSurface= new Recif(new Position(1000, 500), rectangleOriented );
		cercle = new Circle(50);
		s = new Recif(new Position(1000, 500.0), rectangle);
		s2 = new Recif(new Position(500, 0), cercle);
		depart = new Position(0, 0);
		destination = new Position(1200.0, 1200.0);
		d1 = new Position(400, 900);
		a1 = new Position(1500, 900);
		d2 = new Position(300, 500);
		a2 = new Position(1800, 0);

		d3 = new Position(0, -750);
		a3 = new Position(1500, 200);
	}

	@Test
	public void avoidHitRectangleTest() {
		// Test Rectangle
		
	/*	assertTrue(avoidHitRectangleHelper(s,depart, destination));
		assertTrue(avoidHitRectangleHelper(s,d1, a1));
		assertTrue(avoidHitRectangleHelper(s,a1, d1));
		assertTrue(avoidHitRectangleHelper(s,a2, d2));
		assertTrue(avoidHitRectangleHelper(s,d2, a2));
		assertTrue(avoidHitRectangleHelper(s,a2, d2));
		assertTrue(avoidHitRectangleHelper(s, d3, a3));*/
		assertFalse(orientedSurface.isPtInside(d2));
		assertFalse(orientedSurface.isPtInside(a2));
		var segment=new LineSegment2D(a2, d2);
		assertTrue(orientedSurface.intersectsWith(segment));
		
		assertTrue(avoidHitRectangleHelper(orientedSurface,d2, a2));
		
		

		

		// test Circle
		
	}

	private boolean avoidHitRectangleHelper(Surface surface, Position depart, Position destination) {
		List<IPoint> list = surface.avoidHit(depart, destination);
		list.add(0, depart);
		list.add(destination);
		System.out.println();
		for (int i = 0; i < list.size() - 1; i++) {
			LineSegment2D l =  new LineSegment2D(list.get(i), list.get(i + 1));
			if (surface.intersectsWith(l)) {
				System.out.println(l);
				return false;
			}

		}
		return true;
	}

	private boolean avoidHitCircleHelper(Position depart, Position destination) {
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
