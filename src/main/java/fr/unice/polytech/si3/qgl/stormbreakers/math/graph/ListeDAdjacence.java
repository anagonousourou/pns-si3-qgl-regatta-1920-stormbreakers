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
	private static int VISIBLITEMAX = 1000;
	Map<IPoint, List<Arrete>> MapAdjacence;

	public ListeDAdjacence(IPoint boatPosition, List<Recif> recifs, Checkpoint nextCheckpoint) {
		MapAdjacence = new HashMap<IPoint, List<Arrete>>();
		for (double i = boatPosition.x() - 500; i <= boatPosition.x() + 500; i = i + ecart) {
			for (double j = boatPosition.x() - 500; j <= boatPosition.x() + 500; j = j + ecart) {
				MapAdjacence.put(new Point2D(i,j), listArrete(boatPosition, i, j,recifs));
			}
		}
	}


	private List<Arrete> listArrete(IPoint boatPosition,double i, double j,List<Recif> recifs) {
		IPoint depart = new Point2D(i, j);
		List<Arrete> list = new ArrayList<Arrete>();
		if (isPointOnEdge(boatPosition, i, j)) {
			//list.addAll(this.listArrete(i,j));
			list.addAll(listArretePointOnedge(boatPosition, depart, recifs));
		} else {
			for (double x = -ecart; x <= ecart; x = x + ecart) {
				for (double y = -ecart; y <= ecart; y = y + ecart) {
					IPoint arrive = new Point2D(x, y);
					if (!arreteIntersectAShape(depart, arrive, recifs)) {
						list.add(new Arrete(depart, arrive));
					}
				}
			}
		}
		return list;
	}



	private List<Arrete> listArretePointOnedge(IPoint boatPosition,IPoint depart ,List<Recif> recifs) {
		IPoint BG = new Point2D(depart.x()-50,depart.y()-50);
		IPoint MG = new Point2D(depart.x()-50,depart.y());
		IPoint HG = new Point2D(depart.x()-50,depart.y()+50);
		
		IPoint BC = new Point2D(depart.x(),depart.y()-50);
		IPoint HC = new Point2D(depart.x(),depart.y()+50);
		
		IPoint BD = new Point2D(depart.x()+50,depart.y()-50);
		IPoint MD = new Point2D(depart.x()+50,depart.y());
		IPoint HD = new Point2D(depart.x()+50,depart.y()+50);
		
		List<Arrete> list =new ArrayList<Arrete>();
		if(depart.x()==boatPosition.x()-500 && depart.y()==boatPosition.y()-500) {
			list.addAll(List.of(
					new Arrete(depart,HC),
					new Arrete(depart,HD),
					new Arrete(depart,MD)));
		}else if(depart.x()==boatPosition.x()-500 && depart.y()==boatPosition.y()+500) {
			list.addAll(List.of(
					new Arrete(depart,BC),
					new Arrete(depart,BD),
					new Arrete(depart,MD)));
		}else if(depart.x()==boatPosition.x()+500 && depart.y()==boatPosition.y()-500) {
			list.addAll(List.of(
					new Arrete(depart,BG),
					new Arrete(depart,HC),
					new Arrete(depart,HG)));
		}else if(depart.x()==boatPosition.x()+500 && depart.y()==boatPosition.y()+500) {
			list.addAll(List.of(
					new Arrete(depart,BG)
					,new Arrete(depart,BC)
					,new Arrete(depart,MG)));
		}else if(depart.x()==boatPosition.x()-500) {
			list.addAll(List.of(
					new Arrete(depart,HC)
					,new Arrete(depart,HD)
					,new Arrete(depart,MD)
					,new Arrete(depart,BD)
					,new Arrete(depart,BC)));
		}else if(depart.x()==boatPosition.x()+500) {
			list.addAll(List.of(
					new Arrete(depart,HC)
					,new Arrete(depart,HG)
					,new Arrete(depart,MG)
					,new Arrete(depart,BG)
					,new Arrete(depart,BC)));
		}else if(depart.x()==boatPosition.y()-500) {
			list.addAll(List.of(
					new Arrete(depart,MD)
					,new Arrete(depart,MG)
					,new Arrete(depart,BG)
					,new Arrete(depart,BD)
					,new Arrete(depart,BC)));
		}else if(depart.x()==boatPosition.y()+500) {
			list.addAll(List.of(
					new Arrete(depart,HC)
					,new Arrete(depart,HG)
					,new Arrete(depart,HD)
					,new Arrete(depart,MD)
					,new Arrete(depart,MG)));
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
