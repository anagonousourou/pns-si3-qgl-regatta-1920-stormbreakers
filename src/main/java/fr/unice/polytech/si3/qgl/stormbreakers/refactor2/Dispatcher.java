package fr.unice.polytech.si3.qgl.stormbreakers.refactor2;

import fr.unice.polytech.si3.qgl.stormbreakers.simulation.MoveAction;

import java.util.List;

/**
 *  Repartit les marins a leurs postes selon une formation souhaitée
 */


public class Dispatcher {

    EquipmentManager equipmentManager;
    CrewManager crew;

    Dispatcher(CrewManager crewManager, EquipmentManager equipmentManager) {

    }

    /**
     * Repartit les marins de manière à aller tout droit
     * avec la vitesse la plus basse possible
     */
    public List<MoveAction> dispatchStraightSlowest() {

    }

    /**
     * Repartit les marins de manière à aller tout droit
     * avec une vitesse moyenne
     */
    public List<MoveAction> dispatchStraightMedium() {

    }

    /**
     * Repartit les marins de manière à aller tout droit
     * avec la vitesse la plus haute possible
     */
    public List<MoveAction> dispatchStraightFastest() {

    }


    /**
     * Repartit les marins de manière à tourner d'un angle
     * La vitesse est ici minimale
     * @param aimedAngle
     */
    public List<MoveAction> dispatchAngle( double aimedAngle ) {

    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à gauche
     * @return
     */
    public List<Marin> leftMarinsOnOars() {
        // TODO
        // return marins.stream().filter(m -> equimentManager.oarPresentAt(m.getPosition())).collect(Collectors.toList());

    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à droite
     *
     * @return
     */
    public List<Marin> rightMarinsOnOars() {
        // TODO
        return null;
    }

}
