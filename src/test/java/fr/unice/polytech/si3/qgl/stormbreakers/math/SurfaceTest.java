package fr.unice.polytech.si3.qgl.stormbreakers.math;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
public class SurfaceTest {
	Surface s;
	Position depart;
	Position destination;
	Position d1;
	Position a1;
	
	@BeforeEach
	public void setup() {
		Rectangle r= new Rectangle(10.0,10.0,0.0);
		s=new Recif(new Position(0,0),r);
		depart = new Position(-8, 0);
		destination = new Position(8, 0);
		d1 = new Position(-7, 2);
		a1 = new Position(9, -4);
	}
	
	@Test
	public void avoidHitTest() {
		assertTrue(avoidHitTest(depart,destination));
		assertTrue(avoidHitTest(destination,depart));
		assertTrue(avoidHitTest(d1,a1));
		assertEquals(2,s.avoidHit(destination,depart).size());
		assertEquals(2,s.avoidHit(depart, destination).size());
		//à completer avec tous les cas
	}

	private boolean avoidHitTest(Position depart, Position destination) {
		List<IPoint> list=s.avoidHit(depart, destination);
		list.add(0,depart);
		list.add(destination);
		System.out.println("DEBUT");
		for(int i=0;i<list.size()-1;i++) {
			LineSegment2D l= new LineSegment2D(list.get(i),list.get(i+1));
			if(s.intersectsWith(l)) {
				return false;
			}
		}
		System.out.println("FIN");
		return true;
	}
}
