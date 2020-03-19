package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;

public class ShortestPathCalculator {

	HashMap<Sommet, Double> distanceToSource= new HashMap<>();//contient la distance Sommet vers boatPosition
	HashMap<Sommet, Sommet> PreviousSommet =  new HashMap<>(); //pour un Sommet s retourne le point précedent pour obtenir son plus court chemin  
	
	/**
	 * retourne la liste des points qui font partie du plus court chemin
	 * @param boatSommet
	 * @param recifs
	 * @param nextCheckpoint
	 * @return
	 */
	public List<Sommet> shortestPathFromBoatPos(Sommet boatSommet,List<Recif> recifs,Checkpoint nextCheckpoint) {

		List<Sommet> selectSommet= new ArrayList<>(); 
		List<Sommet> notSelectSommet= new ArrayList<>(); 
		notSelectSommet.add(boatSommet);
		distanceToSource.put(boatSommet,0.0);
		
		Sommet checkpointSommet = new Sommet(nextCheckpoint.getPosition());
		Sommet nextSelectSommet = boatSommet;
		while(!selectSommet.contains(checkpointSommet)) {
			Sommet nextSommetTmp=getLowestDistanceFromSource(notSelectSommet);
			//System.out.println("nextSommet:"+nextSelectSommet);
			if(nextSommetTmp==null) {
				return getSortestPathForNode(nextSelectSommet,boatSommet);
			}
			nextSelectSommet = getLowestDistanceFromSource(notSelectSommet);
			notSelectSommet.remove(nextSelectSommet);
			selectSommet.add(nextSelectSommet);
			
			List<Arrete> arretesPartantDeSommet= new ArrayList<Arrete>();
			ListeDAdjacence adj = new ListeDAdjacence(boatSommet.getPoint(), nextSelectSommet.getPoint(), recifs, nextCheckpoint);//classe qui s'enfout des paramêtres
			arretesPartantDeSommet.addAll(adj.getArreteAdjacente());

			for(Arrete a: arretesPartantDeSommet) {
			//	System.out.println("a:"+a);
				if(!notSelectSommet.contains(a.getArrive())&& !selectSommet.contains(a.getArrive())) {
					notSelectSommet.add(a.getArrive());
					distanceToSource.put(a.getArrive(),Double.POSITIVE_INFINITY);
				}
				calculateMinimumDistance(a);
			}
		}
		return getSortestPathForNode(checkpointSommet, boatSommet);
	}
	

	
	private List<Sommet> getSortestPathForNode(Sommet current,Sommet boatSommet){
		List<Sommet> trajetCurrentBoat= new ArrayList<>();
		trajetCurrentBoat.add(current);
		int i=0;
		Sommet s;
		do{
			s= PreviousSommet.get(trajetCurrentBoat.get(i));
			trajetCurrentBoat.add(s);
			i++;
		}while(!s.equals(boatSommet)); 
		
		//System.out.println(trajetCheckpointBoat);
		Collections.reverse(trajetCurrentBoat);//on transforme le trajet checkpoint bateau en  un trajet Bateau checkpoint
		return trajetCurrentBoat;
	}
	
	/**
	 * change le point précedent et la distance si depuis un autre point le trajet est plus court
	 * @param a
	 */
	private void calculateMinimumDistance(Arrete a) {
		double distanceFromBoat= distanceToSource.get(a.getDepart());
		if(distanceFromBoat+a.getPoid()< distanceToSource.get(a.getArrive())) {
			distanceToSource.put(a.getArrive(),distanceFromBoat+a.getPoid());
			PreviousSommet.put(a.getArrive(), a.getDepart());
		}
	}

	/**
	 * retourne le sommet qui à la plus petite distance en partant de boatPosition
	 * @param notSelectSommet
	 * @return
	 */
	private Sommet getLowestDistanceFromSource(List<Sommet> notSelectSommet) {
		if(notSelectSommet.size()==0) {
			return null;
		}
		Sommet nearestSommet =notSelectSommet.get(0);
		double lowestDistance= distanceToSource.get(nearestSommet);
		//System.out.println("notSelectSommet"+notSelectSommet.size());
		for(int i=1;i<notSelectSommet.size();i++) {
			if(distanceToSource.get(notSelectSommet.get(i))<lowestDistance) {
				nearestSommet =notSelectSommet.get(i);
				lowestDistance= distanceToSource.get(nearestSommet);
			}
		}
		return nearestSommet;
	}
	
	public void RemoveUselessNode(List<Sommet> listToCp){
		
		for(int i=1;i<listToCp.size()-1;i++) {
			if(orientation(listToCp.get(i-1), listToCp.get(i)).equals(orientation(listToCp.get(i), listToCp.get(i+1)))) {
				listToCp.remove(i);
				i--;
			}
		}
	}
	
	private String orientation(Sommet prec, Sommet current) {
		if(current.getX()>prec.getX()) {
			if(current.getY()>prec.getY()) {
				return "HD"; //deplacement en haut à gauche par rapport a prec
			}else if(current.getY()==prec.getY()) {
				return "H"; //deplacement en haut par rapport a prec
			}else {
				return "HG";//deplacement en haut a gauche par rapport a prec
			}
		}else {
			if(current.getY()>prec.getY()) {
				return "BD"; 
			}else if(current.getY()==prec.getY()) {
				return "B"; 
			}else {
				return "BG";
			}
		}
		
	}
}
