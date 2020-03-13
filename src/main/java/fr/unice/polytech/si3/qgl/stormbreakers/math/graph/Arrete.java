package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;

public class Arrete {
	private Sommet depart;
	private Sommet arrive;
	private double poid;
	
	public Arrete(IPoint depart, IPoint arrive) {
		// TODO Auto-generated constructor stub
		this.depart= new Sommet(depart);
		this.arrive= new Sommet(arrive);
		this.poid= depart.distanceTo(arrive);
	}
	

	
	public String toString() {
		return "("+depart.toString()+","+arrive.toString()+") poid :"+poid;	
	}
	
	public double getPoid() {
		return poid;
	}
	public Sommet getArrive() {
		return arrive;
	}
	public Sommet getDepart() {
		return depart;
	}
}
