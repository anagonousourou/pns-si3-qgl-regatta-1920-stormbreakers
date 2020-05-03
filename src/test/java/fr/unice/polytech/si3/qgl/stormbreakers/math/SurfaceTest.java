package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Reef;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;

public class SurfaceTest {
	private Surface s;
	private Surface surfaceCircle;
	private Surface orientedSurface;
	private Surface polygonsurface;
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
	private Polygon polygon;
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
		orientedSurface= new Reef(new Position(1000, 500), rectangleOriented );
		cercle = new Circle(300);
		s = new Reef(new Position(1000, 500.0), rectangle);
		surfaceCircle = new Reef(new Position(1000, 500), cercle);

		polygon = new Polygon(0.0,
				List.of(new Point2D(-150,-150),
						new Point2D(250,150),
						new Point2D(-200,200)),
				new Position(0, 0));
		polygonsurface = new Reef(new Position(1000, 500), polygon);
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


	
}
