package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;

public class ListeDAdjacence {
	private double ecart = 50.0;
	private int VISIBLITEMAX = 1000;
	private List<Arrete> listAdjacence;
	
	public ListeDAdjacence(IPoint boatPosition,IPoint currentSommet, List<Recif> recifs, Checkpoint nextCheckpoint) {

		//this.currentSommet= currentSommet;
		listAdjacence = new ArrayList<Arrete>();
		listAdjacence.addAll(createListArrete(boatPosition,currentSommet,recifs ,nextCheckpoint));
	}
	
	
	public List<Arrete> getArreteAdjacente() {
		return listAdjacence;
	}
	
	public List<Arrete> getNearestArrete(){
		listAdjacence.sort((a ,b) -> Double.compare(a.getPoid(),b.getPoid()));
		return listAdjacence;
	}
	
	private List<Arrete> createListArrete(IPoint boatPosition,IPoint currentSommet, List<Recif> recifs, Checkpoint nextCheckpoint) {
		List<Arrete> list = new ArrayList<Arrete>();
		if (isPointOnEdge(boatPosition, currentSommet.x(), currentSommet.y())) {
			//list.addAll(this.listArrete(i,j));
			list.addAll(listArretePointOnedge(boatPosition, currentSommet, nextCheckpoint.getPosition() ,recifs));
		} else {
			for (double x = -ecart; x <= ecart; x = x + ecart) {
				for (double y = -ecart; y <= ecart; y = y + ecart) {
					IPoint arrive = new Point2D(boatPosition.x()+x,boatPosition.y()+y);
					if(arrive.distanceTo(nextCheckpoint)<=50) {
						arrive=nextCheckpoint;
					}
					if(!(arrive.x()==currentSommet.x() && arrive.y()== currentSommet.y())) {
						if (!arreteIntersectAShape(currentSommet, arrive, recifs)) {
							list.add(new Arrete(currentSommet, arrive));
						}	
					}
						
				}
			}
		}
		return list;
	}

	

	private List<Arrete> listArretePointOnedge(IPoint boatPosition,IPoint depart,IPoint checkpoint ,List<Recif> recifs) {
		IPoint BG = new Point2D(depart.x()-ecart,depart.y()-ecart);
		IPoint MG = new Point2D(depart.x()-ecart,depart.y());
		IPoint HG = new Point2D(depart.x()-ecart,depart.y()+ecart);
		
		IPoint BC = new Point2D(depart.x(),depart.y()-ecart);
		IPoint HC = new Point2D(depart.x(),depart.y()+ecart);
		
		IPoint BD = new Point2D(depart.x()+ecart,depart.y()-ecart);
		IPoint MD = new Point2D(depart.x()+ecart,depart.y());
		IPoint HD = new Point2D(depart.x()+ecart,depart.y()+ecart);
		
		List<Arrete> list =new ArrayList<Arrete>();
		if(depart.x()==boatPosition.x()-(VISIBLITEMAX/2) && depart.y()==boatPosition.y()-(VISIBLITEMAX/2)) {
			list.addAll(List.of(
					new Arrete(depart,HC),
					new Arrete(depart,HD),
					new Arrete(depart,MD)));

		}else if(depart.x()==boatPosition.x()-(VISIBLITEMAX/2) && depart.y()==boatPosition.y()+(VISIBLITEMAX/2)) {
			list.addAll(List.of(
					new Arrete(depart,BC),
					new Arrete(depart,BD),
					new Arrete(depart,MD)));
		}else if(depart.x()==boatPosition.x()+(VISIBLITEMAX/2) && depart.y()==boatPosition.y()-(VISIBLITEMAX/2)) {
			list.addAll(List.of(
					new Arrete(depart,BG),
					new Arrete(depart,HC),
					new Arrete(depart,HG)));
		}else if(depart.x()==boatPosition.x()+(VISIBLITEMAX/2) && depart.y()==boatPosition.y()+(VISIBLITEMAX/2)) {
			list.addAll(List.of(
					new Arrete(depart,BG)
					,new Arrete(depart,BC)
					,new Arrete(depart,MG)));
		}else if(depart.x()==boatPosition.x()-(VISIBLITEMAX/2)) {
			list.addAll(List.of(
					new Arrete(depart,HC)
					,new Arrete(depart,HD)
					,new Arrete(depart,MD)
					,new Arrete(depart,BD)
					,new Arrete(depart,BC)));
		}else if(depart.x()==boatPosition.x()+(VISIBLITEMAX/2)) {
			list.addAll(List.of(
					new Arrete(depart,HC)
					,new Arrete(depart,HG)
					,new Arrete(depart,MG)
					,new Arrete(depart,BG)
					,new Arrete(depart,BC)));
		}else if(depart.y()==boatPosition.y()-(VISIBLITEMAX/2)) {
			list.addAll(List.of(
					new Arrete(depart,MD)
					,new Arrete(depart,MG)
					,new Arrete(depart,BG)
					,new Arrete(depart,BD)
					,new Arrete(depart,BC)));
		}else if(depart.y()==boatPosition.y()+(VISIBLITEMAX/2)) {
			list.addAll(List.of(
					new Arrete(depart,HC)
					,new Arrete(depart,HG)
					,new Arrete(depart,HD)
					,new Arrete(depart,MD)
					,new Arrete(depart,MG)));
		}
		if (!arreteIntersectAShape(depart, checkpoint, recifs)) {
			list.add(new Arrete(depart, checkpoint));
		}
		//System.out.println(depart+" "+list);
		return list;
	}


	protected boolean arreteIntersectAShape(IPoint depart, IPoint arrive, List<Recif> recifs) {
		LineSegment2D line = new LineSegment2D(depart, arrive);
		for (int i = 0; i < recifs.size(); i++) {
			if (recifs.get(i).intersectsWith(line)) {
				return true;
			}
		}
		return false;
	}

	protected boolean isPointOnEdge(IPoint boatPosition, double x, double y) {
		if (Utils.almostEquals(boatPosition.x() - 500, x) || Utils.almostEquals(boatPosition.x() + 500, x)
				|| Utils.almostEquals(boatPosition.y() - 500, y) || Utils.almostEquals(boatPosition.y() + 500, y)) {
			return true;
		}
		return false;
	}
	
	
}
