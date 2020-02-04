package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;

/**
 * Classe pour contenir les fonctions purement algorithmique de la direction du
 * navire afin de faciliter les tests des comportements du navire
 */
public class Captain {

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
                        result.add(new Oar(m.getId()));
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

    // TODO: 04/02/2020 Complete java doc
    /**
     * methode pour permettre le deplacement en ligne droite
     * 
     * @param oars
     * @return
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

    public List<SailorAction> minRepartition(List<Equipment> rightOars, List<Equipment> leftOars, int diffToCatch,
            List<Marin> marinUtilise, List<Marin> allsailors) {
        if (diffToCatch < 0) {
            return this.activateNbOars(rightOars, -diffToCatch, marinUtilise, allsailors);
        } else {
            return this.activateNbOars(leftOars, diffToCatch, marinUtilise, allsailors);
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