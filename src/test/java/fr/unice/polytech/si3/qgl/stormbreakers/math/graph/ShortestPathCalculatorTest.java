package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

public class ShortestPathCalculatorTest {
	//ListeDAdjacence adj;
	Sommet bateau;
	List<Recif> recifs;
	Recif r1, r2, r3, r4;
	Checkpoint cp, cp2, cp3, cp4, cp5;
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
		cp4 = new Checkpoint(new Position(1700, 1700), new Circle(50));
		cp5 = new Checkpoint(new Position(1250, 2600), new Circle(50));
		spc= new ShortestPathCalculator();
		//adj= new ListeDAdjacence(bateau, other,recifs, cp);
	}
	
	@Test
	public void shortestPathFromBoatPosTest() {
		spc.shortestPathFromBoatPos(bateau, recifs,cp);
		spc.shortestPathFromBoatPos(bateau, recifs,cp2);
	}
	
	@Test
	public void shortestPathFromBoatPosWithReevesTest() {
		recifs= new ArrayList<Recif>(List.of(r1, r2));
		spc.shortestPathFromBoatPos(bateau, recifs, cp3);
		spc.shortestPathFromBoatPos(bateau, recifs, cp4);
		spc.shortestPathFromBoatPos(bateau, recifs, cp5);
	}
	@Test
	public void shortestPathFromBoatPosWithManyReevesTest() {
		recifs= new ArrayList<Recif>(List.of(r1, r2, r3, r4));
		spc.shortestPathFromBoatPos(bateau, recifs, cp);
		spc.shortestPathFromBoatPos(bateau, recifs, cp2);
		spc.shortestPathFromBoatPos(bateau, recifs, cp3);
		spc.shortestPathFromBoatPos(bateau, recifs, cp4);
		spc.shortestPathFromBoatPos(bateau, recifs, cp5);
	}
}
