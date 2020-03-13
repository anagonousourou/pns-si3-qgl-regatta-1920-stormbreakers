package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;



import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;

public class shortestPathCalculator {

	HashMap<Sommet, Double> distanceToSource= new HashMap<>();
	HashMap<Sommet, Sommet> PreviousSommet =  new HashMap<>(); 
	
	public List<Sommet> shortestPathFromBoatPos(Sommet boatSommet, Sommet checkpointSommet,List<Recif> recifs,Checkpoint nextCheckpoint) {

		List<Sommet> selectSommet= new ArrayList<>(); 
		List<Sommet> notSelectSommet= new ArrayList<>(); 
		notSelectSommet.add(boatSommet);
		distanceToSource.put(boatSommet,0.0);
		
		while(!selectSommet.contains(checkpointSommet)) {
			Sommet nextSelectSommet = getLowestDistanceFromSource(notSelectSommet);
			notSelectSommet.remove(nextSelectSommet);
			selectSommet.add(nextSelectSommet);
			List<Arrete> arretesPartantDeSommet= new ListeDAdjacence(boatSommet.getPoint(), nextSelectSommet.getPoint(), recifs, nextCheckpoint).getArreteAdjacente();
			
			for(Arrete a: arretesPartantDeSommet) {
				calculateMinimumDistance(a);
				if(!notSelectSommet.contains(a.getArrive())) {
					notSelectSommet.add(a.getArrive());
				}
			}
		}
		List<Sommet> trajetCheckpointBoat= new ArrayList<>();
		trajetCheckpointBoat.add(checkpointSommet);
		
		for(int i=0;i<PreviousSommet.size();i++) {
			trajetCheckpointBoat.add(PreviousSommet.get(trajetCheckpointBoat.get(0)));
		}
		Collections.reverse(trajetCheckpointBoat);//on transforme le trajet checkpoint bateau en  un trajet Bateau checkpoint
		return trajetCheckpointBoat;
	}

	private void calculateMinimumDistance(Arrete a) {
		double distanceFromBoat= distanceToSource.get(a.getDepart());
		if(distanceFromBoat+a.getPoid()< distanceToSource.get(a.getArrive())) {
			distanceToSource.put(a.getArrive(),distanceFromBoat+a.getPoid());
			PreviousSommet.put(a.getArrive(), a.getDepart());
		}
	}

	private Sommet getLowestDistanceFromSource(List<Sommet> notSelectSommet) {
		// TODO Auto-generated method stub
		if(notSelectSommet.isEmpty()) {
			return null;
		}
		Sommet nearestSommet =notSelectSommet.get(0);
		double lowestDistance= distanceToSource.get(nearestSommet);
		for(int i=1;i<notSelectSommet.size();i++) {
			if(distanceToSource.get(notSelectSommet.get(i))<lowestDistance) {
				nearestSommet =notSelectSommet.get(i);
				lowestDistance= distanceToSource.get(nearestSommet);
			}
		}
		return nearestSommet;
	}
	
	
}
