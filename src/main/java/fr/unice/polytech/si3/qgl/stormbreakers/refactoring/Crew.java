package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;

public class Crew {

    private List<Marine> marins;

    public Crew(List<Marine> marins) {
        this.marins = marins;
    }

    Optional<Marine> getMarinById(int id) {
        return marins.stream().filter(m -> m.getId() == id).findFirst();
    }

    public void addListener(PropertyChangeListener propertyChangeListener) {
        this.marins.forEach(marin -> marin.addPropertyChangeListener(propertyChangeListener));
    }

    /**
     * fait executer aux marin concernés les MoveAction contenus dans le param moves
     * 
     * @param moves
     */
    public void executeMovingsInSailorAction(List<SailorAction> actions) {
        for (SailorAction m : actions) {
            // TODO complete
        }
    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à gauche
     * 
     * @param equimentManager
     * @return
     */
    public List<Marine> leftMarinsOnOars(EquipmentManager equimentManager) {
        return marins.stream().filter(m -> equimentManager.oarPresentAt(m.getPosition())).collect(Collectors.toList());

    }

    /**
     * Fonction qui renvoie les marins présents sur des rames à droite
     * 
     * @param equimentManager
     * @return
     */
    public List<Marine> rightMarinsOnOars(EquipmentManager equimentManager) {
        // TODO
        return null;
    }

    @Override
    public String toString() {
        return marins.toString();
    }

}