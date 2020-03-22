package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;

public class SurfaceTest {
	private Surface s;
	private Surface surfaceCircle;
	private Surface orientedSurface;
	private Position depart;
	private Position destination;
	private Position d1;
	private Position a1;
	private Position d2;
	private Position d3;
	private Position a2;
	
	
	private Rectangle rectangle;
	private Rectangle rectangleOriented;
	private Circle cercle;

	private Surface carre;
	private Surface triangle;
	private LineSegment2D line1 = new LineSegment2D(new Point2D(0, 0), new Point2D(300, 300));
	private LineSegment2D line2 = new LineSegment2D(new Point2D(0, 100), new Point2D(100, 0));
	private LineSegment2D line3 = new LineSegment2D(new Point2D(50, 350), new Point2D(500, 350));

	private LineSegment2D lineTriangle1 = new LineSegment2D(new Point2D(50, 50), new Point2D(900, 50));
	private LineSegment2D lineTriangle2 = new LineSegment2D(new Point2D(5, 210), new Point2D(1000, 210));
	private LineSegment2D lineTriangle3 = new LineSegment2D(new Point2D(850, -10), new Point2D(860, 300));

	@BeforeEach
	public void setUp() {
		rectangle = new Rectangle(1000.0, 800.0, 0.0);
		rectangleOriented=new Rectangle(500, 500, 0.78539);
		orientedSurface= new Recif(new Position(1000, 500), rectangleOriented );
		cercle = new Circle(300);
		s = new Recif(new Position(1000, 500.0), rectangle);
		surfaceCircle = new Recif(new Position(1000, 500), cercle);
		depart = new Position(0, 0);
		destination = new Position(1200.0, 1200.0);
		d1 = new Position(400, 900);
		a1 = new Position(1500, 900);
		d2 = new Position(300, 500);
		d3 = new Position(1300, 800);
		a2 = new Position(1800, 0);

		triangle = new Surface() {

            @Override
            public double getOrientation() {
                return 0;
            }

            @Override
            public double y() {
                return 100;
            }

            @Override
            public double x() {
                return 800;
            }

            @Override
            public Shape getShape() {
                return new Polygon(0,
						List.of(new Point2D(100, 100), new Point2D(0, -100), new Point2D(100, -100)),
						new Position(x(),y())
				);
            }
		};
		
		carre = new Surface() {

            @Override
            public double getOrientation() {
                return 0;
            }

            @Override
            public double y() {
                return 300;
            }

            @Override
            public double x() {
                return 300;
            }

            @Override
            public Shape getShape() {
                return new Polygon(0,
						List.of(new Point2D(100, 100), new Point2D(-100, 100),
								new Point2D(-100, -100), new Point2D(100, -100)),
						new Position(x(),y())
                );
            }
        };

	

	}

	@Test
	void intersectsWithTest() {
		assertTrue(carre.intersectsWith(line1));
		assertFalse(carre.intersectsWith(line2));
		assertTrue(carre.intersectsWith(line3));
	}

	@Test
	void intersectsWithTriangleTest() {
		assertTrue(triangle.intersectsWith(lineTriangle1));
		assertFalse(triangle.intersectsWith(lineTriangle2));
		assertTrue(triangle.intersectsWith(lineTriangle3));
	}

	@Test
	public void avoidHitTest() {
		// Test Rectangle
		
		assertTrue(avoidHitRectangleHelper(s,depart, destination));
		assertTrue(avoidHitRectangleHelper(s,d1, a1));
		assertTrue(avoidHitRectangleHelper(s,a1, d1));
		assertTrue(avoidHitRectangleHelper(s,a2, d2));
		assertTrue(avoidHitRectangleHelper(s,d2, a2));
		assertTrue(avoidHitRectangleHelper(s,a2, d2));
		
		assertFalse(orientedSurface.isPtInside(d2));
		assertFalse(orientedSurface.isPtInside(a2));
		var segment = new LineSegment2D(a2, d2);
		assertTrue(orientedSurface.intersectsWith(segment));

		assertTrue(avoidHitRectangleHelper(orientedSurface, d2, a2));

		// test Circle
        // LATER: 16/03/2020 Rethink this test
		// assertTrue(avoidHitRectangleHelper(surfaceCircle, depart, d3));

	}

	private boolean avoidHitRectangleHelper(Surface surface, Position depart, Position destination) {
		List<IPoint> list = surface.avoidHit(depart, destination);
		
		for (int i = 0; i < list.size() - 1; i++) {
			LineSegment2D l = new LineSegment2D(list.get(i), list.get(i + 1));
			if (surface.intersectsWith(l)) {
				
				return false;
			}

		}
		return true;
	}

	
}
