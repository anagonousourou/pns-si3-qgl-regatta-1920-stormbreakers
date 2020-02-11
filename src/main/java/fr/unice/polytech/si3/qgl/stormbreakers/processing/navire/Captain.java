package fr.unice.polytech.si3.qgl.stormbreakers.processing.navire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;

/**
 * Classe pour contenir les fonctions purement algorithmique de la direction du
 * navire afin de faciliter les tests des comportements du navire
 */
public class Captain {
	/**
	 * 
	 * @param oars
	 * @param nbToActivate
	 * @param marinUtilise
	 * @param allsailors
	 * @return List<sailorAction> -une liste des actions marin et rame associé d'un seul côté gauche ou droite
	 */
    public List<SailorAction> activateNbOars(List<Equipment> oars, int nbToActivate, List<Marin> marinUtilise,
                                             List<Marin> allsailors) {
        List<SailorAction> result = new ArrayList<>();
        List<Marin> yetBusy = marinUtilise;
        int compteur = 0;
        Map<Equipment, List<Marin>> correspondances = this.marinsDisponibles(oars, allsailors);
        for (Equipment oar : oars) {
            if (correspondances.get(oar) != null) {
                for (Marin m : correspondances.get(oar)) {
                    if (!yetBusy.contains(m)) {
                        yetBusy.add(m);
                        result.add(new OarAction(m.getId()));
                        result.add(m.howToGoTo(oar.getX(), oar.getY()));
                        compteur++;
                        break;
                    }
                }
            }
            if (compteur == nbToActivate) {
                break;
            }

        }
        return result;
    }
    
	public List<SailorAction> activateRudder(List<Marin> marinsUtilise, List<Marin> marins, Equipment gouvernail, double angle) {

        List<SailorAction> result = new ArrayList<>();
		result.add(marins.get(0).howToGoTo(gouvernail.getX(), gouvernail.getY()));
		if(angle<=Math.PI/4 && angle>=-Math.PI/4) {
			result.add(new Turn(marins.get(0).getId(),angle));
		}else if(angle>Math.PI/4){
			result.add(new Turn(marins.get(0).getId(),Math.PI/4));
		}else {
			result.add(new Turn(marins.get(0).getId(),-Math.PI/4));
		}
		marinsUtilise.add(marins.get(0));
		return result;		
	}

    /**
     * methode pour activer autant de rames à droite 
     * qu'à gauche pour permettre le deplacement en ligne droite
     * 
     * @param oarsLeft
     * @param oarsRight
     * @param marinUtilise
     * @param allsailors
     * @return List<sailorAction> -une liste des actions à rame et marin associé pour aller tout droit
     */
    public List<SailorAction> toActivate(List<Equipment> oarsLeft, List<Equipment> oarsRight, List<Marin> marinUtilise,
            List<Marin> allsailors) {
        int sizeListMin = Math.min(oarsLeft.size(), oarsRight.size());
        List<SailorAction> result = new ArrayList<>();
        List<Marin> areBusyList = marinUtilise;
        List<SailorAction> leftOarsActivated = this.activateNbOars(oarsLeft, sizeListMin, areBusyList, allsailors);
        List<SailorAction> rightOarsActivated = this.activateNbOars(oarsRight, leftOarsActivated.size(), areBusyList,
                allsailors);
        if (leftOarsActivated.size() == rightOarsActivated.size()) {
            result.addAll(leftOarsActivated);
            result.addAll(rightOarsActivated);
            return result;
        } else if (leftOarsActivated.size() > rightOarsActivated.size()) {
            result.addAll(leftOarsActivated.subList(0, rightOarsActivated.size()));
            result.addAll(rightOarsActivated);
            return result;
        } else {
            result.addAll(leftOarsActivated);
            result.addAll(rightOarsActivated.subList(0, leftOarsActivated.size()));
            return result;
        }

    }
    
    public Map<Equipment, List<Marin>> marinsDisponibles(List<Equipment> rames, List<Marin> marins) {
        HashMap<Equipment, List<Marin>> results = new HashMap<>();
        rames.forEach(oar -> results.put(oar,
                marins.stream().filter(e -> (Math.abs(e.getX() - oar.getX()) + Math.abs(e.getY() - oar.getY())) <= 5)
                        .collect(Collectors.toList())));
        return results;
    }
    
    public List<Marin> marinsProcheGouvernail(Equipment gouvernail,List<Marin> marins){
    	 ArrayList<Marin> marinsProche= new ArrayList<>();
    	 marinsProche.addAll(
    			 marins.stream().filter(e -> (Math.abs(e.getX() - gouvernail.getX()) + Math.abs(e.getY() - gouvernail.getY())) <= 5)  
                 .collect(Collectors.toList()));
    	 
    	 return marinsProche;
    }

    public List<SailorAction> minRepartition(List<Equipment> rightOars, List<Equipment> leftOars, int diffToCatch,
            List<Marin> marinUtilise, List<Marin> allsailors) {
        if (diffToCatch > 0) {
            return this.activateNbOars(rightOars, diffToCatch, marinUtilise, allsailors);
        } else {
            return this.activateNbOars(leftOars, -diffToCatch, marinUtilise, allsailors);
        }
    }

    /**
     * @return correspondances marin et rames qui leur sont accessibles
     */
    public Map<Marin, List<Equipment>> ramesAccessibles(List<Marin> theSailors, List<Equipment> rames) {
        HashMap<Marin, List<Equipment>> results = new HashMap<>();
        theSailors.forEach(m -> results.put(m,
                rames.stream().filter(e -> (Math.abs(e.getX() - m.getX()) + Math.abs(e.getY() - m.getY())) <= 5)
                        .collect(Collectors.toList())));
        return results;
    }




}