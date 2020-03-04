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
	@BeforeEach
	public void setup() {
		Rectangle r= new Rectangle(10.0,10.0,0.0);
		s=new Recif(new Position(0,0),r);
		depart = new Position(-15, 0);
		destination = new Position(15, 0);
	}
	
	@Test
	public void avoidHitTest() {
		List<IPoint> list=s.avoidHit(depart, destination);
		list.add(0,depart);
		list.add(destination);
		for(int i=0;i<list.size()-1;i++) {
			LineSegment2D l= new LineSegment2D(list.get(i),list.get(i+1));
			assertFalse(s.intersectsWith(l));
		}
		
		//à completer avec tous les cas
	}
}
