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
		while(!selectSommet.contains(checkpointSommet)) {
			Sommet nextSelectSommet = getLowestDistanceFromSource(notSelectSommet);
			System.out.println("nextSommet:"+nextSelectSommet);
			
			notSelectSommet.remove(nextSelectSommet);
			selectSommet.add(nextSelectSommet);
			
			List<Arrete> arretesPartantDeSommet= new ArrayList<Arrete>();
			ListeDAdjacence adj = new ListeDAdjacence(boatSommet.getPoint(), nextSelectSommet.getPoint(), recifs, nextCheckpoint);//classe qui s'enfout des paramêtres
			arretesPartantDeSommet.addAll(adj.getArreteAdjacente());

			for(Arrete a: arretesPartantDeSommet) {
				System.out.println("a:"+a);
				if(!notSelectSommet.contains(a.getArrive())&& !selectSommet.contains(a.getArrive())) {
					notSelectSommet.add(a.getArrive());
					distanceToSource.put(a.getArrive(),Double.POSITIVE_INFINITY);
				}
				calculateMinimumDistance(a);
			}
		}
		List<Sommet> trajetCheckpointBoat= new ArrayList<>();
		trajetCheckpointBoat.add(checkpointSommet);
		int i=0;
		Sommet s;
		do{
			s= PreviousSommet.get(trajetCheckpointBoat.get(i));
			trajetCheckpointBoat.add(s);
			i++;
		}while(!s.equals(boatSommet)); 
		
		System.out.println(trajetCheckpointBoat);
		Collections.reverse(trajetCheckpointBoat);//on transforme le trajet checkpoint bateau en  un trajet Bateau checkpoint
		return trajetCheckpointBoat;
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

		Sommet nearestSommet =notSelectSommet.get(0);
		double lowestDistance= distanceToSource.get(nearestSommet);
		System.out.println("notSelectSommet"+notSelectSommet.size());
		for(int i=1;i<notSelectSommet.size();i++) {
			if(distanceToSource.get(notSelectSommet.get(i))<lowestDistance) {
				nearestSommet =notSelectSommet.get(i);
				lowestDistance= distanceToSource.get(nearestSommet);
			}
		}
		return nearestSommet;
	}
	
	
}
