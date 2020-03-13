package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import static org.junit.Assert.assertTrue;

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

public class ShortPathCalculatorTest {
	//ListeDAdjacence adj;
	Sommet bateau;
	List<Recif> recifs;
	Checkpoint cp;
	Checkpoint cp2;
	shortestPathCalculator spc;
	
	@BeforeEach 
	void setup() {
		bateau= new Sommet(550.5,550.5);
		//IPoint other= new Point2D(550.5,550.5);
		recifs= new ArrayList<Recif>();
		cp = new Checkpoint(new Position(600, 600), new Circle(50));
		cp2 = new Checkpoint(new Position(800, 800), new Circle(50));
		spc= new shortestPathCalculator();
		//adj= new ListeDAdjacence(bateau, other,recifs, cp);
	}
	
	@Test
	public void shortestPathFromBoatPosTest() {
		spc.shortestPathFromBoatPos(bateau, recifs,cp);
		spc.shortestPathFromBoatPos(bateau, recifs,cp2);
	}
}
