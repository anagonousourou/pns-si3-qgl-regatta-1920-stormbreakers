package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	List<Arrete> arretes;
	
	public Graph() {
		arretes=new ArrayList<Arrete>();
	}
	
	public void ajouterArrete(Arrete a) {
		arretes.add(a);
	}
	
}
