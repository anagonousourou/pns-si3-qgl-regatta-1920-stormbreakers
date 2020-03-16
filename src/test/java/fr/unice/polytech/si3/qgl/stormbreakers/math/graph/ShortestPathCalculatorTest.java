package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;

public class ShortestPathCalculatorTest {
	//ListeDAdjacence adj;
	Sommet bateau;
	List<Recif> recifs;
	Recif r1, r2, r3, r4;
	Checkpoint cp, cp2, cp3, cp4, cp5, cp6, cpRemoveUseless;
	ShortestPathCalculator spc;
	
	@BeforeEach  
	void setup() {
		bateau= new Sommet(750.5,750.5);
		//IPoint other= new Point2D(550.5,550.5);
		r1 = new Recif(new Position(500, 1500), new Rectangle(1000, 500, 0.78539816339));
		r2 = new Recif(new Position(1500, 470), new Rectangle(750, 1000, 0.78539816339));
		r3 = new Recif(new Position(2000, 2000), new Rectangle(1250, 1300, 0.78539816339));
		r4 = new Recif(new Position(1000, 3000), new Rectangle(1250, 1300, 0.78539816339));
		recifs = new ArrayList<Recif>();
		cp = new Checkpoint(new Position(600, 600), new Circle(50));
		cp2 = new Checkpoint(new Position(800, 800), new Circle(50));
		cp3 = new Checkpoint(new Position(1200, 1300), new Circle(50));
		cp4 = new Checkpoint(new Position(2000, 1000), new Circle(50));
		cp5 = new Checkpoint(new Position(1250, 2600), new Circle(50));
		cp6 = new Checkpoint(new Position(2800, 1300), new Circle(50));
		cpRemoveUseless= new Checkpoint(new Position(1750.5, 1750.5), new Circle(50));
		spc= new ShortestPathCalculator();
		//adj= new ListeDAdjacence(bateau, other,recifs, cp);
	}

	@Test
	public void shortestPathFromBoatPosTest() {
		List<Sommet> edges1 = spc.shortestPathFromBoatPos(bateau, recifs,cp);
		assertEquals(edges1.size(), edges1.stream().distinct().count());
		assertEquals(0, edges1.get(edges1.size()-1).getPoint().distanceTo(cp.getPosition()));
		
		List<Sommet> edges2 = spc.shortestPathFromBoatPos(bateau, recifs,cp2);
		assertEquals(edges2.size(), edges2.stream().distinct().count());
		assertEquals(0, edges2.get(edges2.size()-1).getPoint().distanceTo(cp2.getPosition()));
	}
	
	@Test
	public void shortestPathFromBoatPosWithReevesTest() {
		recifs = new ArrayList<Recif>(List.of(r1, r2));
		List<Sommet> edges1 = spc.shortestPathFromBoatPos(bateau, recifs, cp3);
		assertEquals(edges1.size(), edges1.stream().distinct().count());
		assertEquals(0, edges1.get(edges1.size()-1).getPoint().distanceTo(cp3.getPosition()));
		
		List<Sommet> edges2 = spc.shortestPathFromBoatPos(bateau, recifs, cp4);
		assertEquals(edges2.size(), edges2.stream().distinct().count());
		assertEquals(0, edges2.get(edges2.size()-1).getPoint().distanceTo(cp4.getPosition()));
		
		List<Sommet> edges3 = spc.shortestPathFromBoatPos(bateau, recifs, cp5);
		assertEquals(edges3.size(), edges3.stream().distinct().count());
		assertEquals(0, edges3.get(edges3.size()-1).getPoint().distanceTo(cp5.getPosition()));
		
	}
	@Test
	public void shortestPathFromBoatPosWithManyReevesTest() {
		recifs = new ArrayList<Recif>(List.of(r1, r2, r3, r4));
		List<Sommet> edges1 = spc.shortestPathFromBoatPos(bateau, recifs, cp);
		assertEquals(edges1.size(), edges1.stream().distinct().count());
		assertEquals(0, edges1.get(edges1.size()-1).getPoint().distanceTo(cp.getPosition()));
		
		List<Sommet> edges2 = spc.shortestPathFromBoatPos(bateau, recifs, cp2);
		assertEquals(edges2.size(), edges2.stream().distinct().count());
		assertEquals(0, edges2.get(edges2.size()-1).getPoint().distanceTo(cp2.getPosition()));

		List<Sommet> edges3 = spc.shortestPathFromBoatPos(bateau, recifs, cp3);
		assertEquals(edges3.size(), edges3.stream().distinct().count());
		assertEquals(0, edges3.get(edges3.size()-1).getPoint().distanceTo(cp3.getPosition()));
		
		List<Sommet> edges4 = spc.shortestPathFromBoatPos(bateau, recifs, cp4);
		assertEquals(edges4.size(), edges4.stream().distinct().count());
		assertEquals(0, edges4.get(edges4.size()-1).getPoint().distanceTo(cp4.getPosition()));
		
		List<Sommet> edges5 = spc.shortestPathFromBoatPos(bateau, recifs, cp5);
		assertEquals(edges5.size(), edges5.stream().distinct().count());
		assertEquals(0, edges5.get(edges5.size()-1).getPoint().distanceTo(cp5.getPosition()));
		
		List<Sommet> edges6 = spc.shortestPathFromBoatPos(bateau, recifs, cp6);
		assertEquals(edges6.size(), edges6.stream().distinct().count());
		assertEquals(0, edges6.get(edges6.size()-1).getPoint().distanceTo(cp6.getPosition()));
	}
	
	@Test 
	public void removeUselessNodeTest() {
		List<Sommet> list= spc.shortestPathFromBoatPos(bateau, recifs, cpRemoveUseless);
		spc.RemoveUselessNode(list);
		assertTrue(list.size()==2);
	}
}
