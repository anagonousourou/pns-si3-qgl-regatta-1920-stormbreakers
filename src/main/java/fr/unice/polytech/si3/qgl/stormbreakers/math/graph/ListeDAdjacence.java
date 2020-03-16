package fr.unice.polytech.si3.qgl.stormbreakers.math.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.StreamManager;

public class ListeDAdjacence {
	private double ecart = 50.0;// pas entre les valeur
	private int VISIBLITEMAX = 5000;// TODO a passer en parametre ou avoir variable global qui nous dit si
									// visibleMax change (ex: vigie activé augmente la visibilité)
	private List<Arrete> listAdjacence;

	/**
	 * créer la liste des arretes dont le currentSommet est le départ et l'arrivée
	 * est un sommet adjacent a currentSommet
	 * 
	 * @param boatPosition
	 * @param currentSommet
	 * @param recifs
	 * @param nextCheckpoint
	 */
	public ListeDAdjacence(IPoint boatPosition, IPoint currentSommet, List<Recif> recifs, Checkpoint nextCheckpoint) {

		listAdjacence = new ArrayList<Arrete>();
		listAdjacence.addAll(createListArrete(boatPosition, currentSommet, recifs, nextCheckpoint));
	}

	public List<Arrete> getArreteAdjacente() {
		return listAdjacence;
	}

	public List<Arrete> getNearestArrete() {
		listAdjacence.sort((a, b) -> Double.compare(a.getPoid(), b.getPoid()));
		return listAdjacence;
	}

	/**
	 * retourne la liste des arretes adjacente
	 * 
	 * @param boatPosition-permet de connaitre les points sur les extremites
	 * @param currentSommet
	 * @param recifs
	 * @param nextCheckpoint
	 * @param streamManager
	 * @return
	 */
	private List<Arrete> createListArrete(IPoint boatPosition, IPoint currentSommet, List<Recif> recifs,
			Checkpoint nextCheckpoint) {
		List<Arrete> list = new ArrayList<Arrete>();
		if (isPointOnEdge(boatPosition, currentSommet.x(), currentSommet.y())) {
			// list.addAll(this.listArrete(i,j));
			if(!(Math.abs(currentSommet.distanceTo(nextCheckpoint))<=Utils.EPS)) {
				list.addAll(listArretePointOnedge(boatPosition, currentSommet, nextCheckpoint.getPosition(), recifs));
			}
		} else {
			for (double x = -ecart; x <= ecart; x = x + ecart) {
				for (double y = -ecart; y <= ecart; y = y + ecart) {
					IPoint arrive = new Point2D(currentSommet.x() + x, currentSommet.y() + y);
					if (arrive.distanceTo(nextCheckpoint) <= ecart) {
						arrive = nextCheckpoint;
					}
					if (!(arrive.x() == currentSommet.x() && arrive.y() == currentSommet.y())) {
						if (!arreteIntersectAShape(currentSommet, arrive, recifs)) {
							list.add(new Arrete(currentSommet, arrive));
						}
					}
				}

			}
		}
		return list;
	}

	/**
	 * retourne la liste d'arrete si currentSommet se trouve sur une des extremite
	 * visible par le bateau
	 * 
	 * @param boatPosition
	 * @param currentSommet
	 * @param checkpoint
	 * @param recifs
	 * @return
	 */

	private List<Arrete> listArretePointOnedge(IPoint boatPosition, IPoint currentSommet, IPoint checkpoint,
			List<Recif> recifs) {
		IPoint BG = new Point2D(currentSommet.x() - ecart, currentSommet.y() - ecart);
		IPoint MG = new Point2D(currentSommet.x() - ecart, currentSommet.y());
		IPoint HG = new Point2D(currentSommet.x() - ecart, currentSommet.y() + ecart);

		IPoint BC = new Point2D(currentSommet.x(), currentSommet.y() - ecart);
		IPoint HC = new Point2D(currentSommet.x(), currentSommet.y() + ecart);

		IPoint BD = new Point2D(currentSommet.x() + ecart, currentSommet.y() - ecart);
		IPoint MD = new Point2D(currentSommet.x() + ecart, currentSommet.y());
		IPoint HD = new Point2D(currentSommet.x() + ecart, currentSommet.y() + ecart);

		List<Arrete> list = new ArrayList<Arrete>();
		if (currentSommet.x() == boatPosition.x() - (VISIBLITEMAX / 2)
				&& currentSommet.y() == boatPosition.y() - (VISIBLITEMAX / 2)) {
			list.addAll(List.of(new Arrete(currentSommet, HC), new Arrete(currentSommet, HD),
					new Arrete(currentSommet, MD)));
		} else if (currentSommet.x() == boatPosition.x() - (VISIBLITEMAX / 2)
				&& currentSommet.y() == boatPosition.y() + (VISIBLITEMAX / 2)) {
			list.addAll(List.of(new Arrete(currentSommet, BC), new Arrete(currentSommet, BD),
					new Arrete(currentSommet, MD)));
		} else if (currentSommet.x() == boatPosition.x() + (VISIBLITEMAX / 2)
				&& currentSommet.y() == boatPosition.y() - (VISIBLITEMAX / 2)) {
			list.addAll(List.of(new Arrete(currentSommet, HG), new Arrete(currentSommet, HC),
					new Arrete(currentSommet, MG)));
		} else if (currentSommet.x() == boatPosition.x() + (VISIBLITEMAX / 2)
				&& currentSommet.y() == boatPosition.y() + (VISIBLITEMAX / 2)) {
			list.addAll(List.of(new Arrete(currentSommet, BG), new Arrete(currentSommet, BC),
					new Arrete(currentSommet, MG)));
		} else if (currentSommet.x() == boatPosition.x() - (VISIBLITEMAX / 2)) {
			list.addAll(List.of(new Arrete(currentSommet, HC), new Arrete(currentSommet, HD),
					new Arrete(currentSommet, MD), new Arrete(currentSommet, BD), new Arrete(currentSommet, BC)));
		} else if (currentSommet.x() == boatPosition.x() + (VISIBLITEMAX / 2)) {
			list.addAll(List.of(new Arrete(currentSommet, HC), new Arrete(currentSommet, HG),
					new Arrete(currentSommet, MG), new Arrete(currentSommet, BG), new Arrete(currentSommet, BC)));
		} 
		
		else if (currentSommet.y() == boatPosition.y() + (VISIBLITEMAX / 2)) {
			list.addAll(List.of(new Arrete(currentSommet, MD), new Arrete(currentSommet, MG),
					new Arrete(currentSommet, BG), new Arrete(currentSommet, BD), new Arrete(currentSommet, BC)));
		} else if (currentSommet.y() == boatPosition.y() - (VISIBLITEMAX / 2)) {
			list.addAll(List.of(new Arrete(currentSommet, HC), new Arrete(currentSommet, HG),
					new Arrete(currentSommet, HD), new Arrete(currentSommet, MD), new Arrete(currentSommet, MG)));
		}
		if (!arreteIntersectAShape(currentSommet, checkpoint, recifs)) {
			
			list.add(new Arrete(currentSommet, checkpoint));
		}
		// System.out.println(depart+" "+list);
		return list;
	}

	/**
	 * Retourne si le trajet intersect au moins un récif
	 * 
	 * @param depart
	 * @param arrive
	 * @param recifs
	 * @return
	 */
	protected boolean arreteIntersectAShape(IPoint depart, IPoint arrive, List<Recif> recifs) {
		LineSegment2D line = new LineSegment2D(depart, arrive);
		for (int i = 0; i < recifs.size(); i++) {
			if (recifs.get(i).intersectsWith(line)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * retourne si le point se trouve sur une extremite visible
	 * 
	 * @param boatPosition
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean isPointOnEdge(IPoint boatPosition, double x, double y) {
		if (Utils.almostEquals(boatPosition.x() - (VISIBLITEMAX/2), x) || Utils.almostEquals(boatPosition.x() + (VISIBLITEMAX/2), x)
				|| Utils.almostEquals(boatPosition.y() - (VISIBLITEMAX/2), y) || Utils.almostEquals(boatPosition.y() + (VISIBLITEMAX/2), y)) {
			return true;
		}
		return false;
	}

}
