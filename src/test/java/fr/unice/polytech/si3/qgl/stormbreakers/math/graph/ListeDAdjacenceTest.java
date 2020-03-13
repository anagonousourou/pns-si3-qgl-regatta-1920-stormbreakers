package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

public class ListeDAdjacenceTest {
	ListeDAdjacence adj;
	Point2D bateau;
	List<Recif> recifs;
	Checkpoint cp;
	
	@BeforeEach 
	void setup() {
		bateau= new Point2D(500.5,500.5);
		IPoint other= new Point2D(550.5,550.5);
		recifs= new ArrayList<Recif>();
		cp = new Checkpoint(new Position(1200, 1200), new Circle(50));
		adj= new ListeDAdjacence(bateau, other,recifs, cp);
	}
	
	@Test
	void isPointOnEdgeTest() {
		assertFalse(adj.isPointOnEdge(bateau, 1, 1));
		assertTrue(adj.isPointOnEdge(bateau, 0.5, 0.5));
		assertTrue(adj.isPointOnEdge(bateau, 1000.5, 0.5));
		assertTrue(adj.isPointOnEdge(bateau, 0.5, 1000.5));
		assertTrue(adj.isPointOnEdge(bateau, 1000.5, 10));
		assertTrue(adj.isPointOnEdge(bateau, 100, 1000.50));
		
	}
}
