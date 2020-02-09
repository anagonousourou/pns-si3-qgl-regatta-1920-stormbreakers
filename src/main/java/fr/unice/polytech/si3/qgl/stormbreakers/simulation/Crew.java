package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Crew {

    private List<Marine> marins;

    public Crew(List<Marine> marins){
        this.marins=marins;
    }

    Optional<Marine> getMarinById(int id){
        return marins.stream().filter(m->m.getId()==id).findFirst();
    }
    

    public void addListener(PropertyChangeListener propertyChangeListener){
        this.marins.forEach(marin -> marin.addPropertyChangeListener(propertyChangeListener));
    }

    public void executeMoves(List<MoveAction> moves){
        for(MoveAction m:moves){
            var optMarin=this.getMarinById(m.getId());
            if(optMarin.isPresent()){
                optMarin.get().requestMove(m);
            }
        }
    }
    public void executeActions(List<OarAction> actions){
        for(OarAction oa:actions){
            var optMarin=this.getMarinById(oa.getId());
            if(optMarin.isPresent()){
                optMarin.get().requestOarAction(oa);
            }
        }
    }
    /**
     * Fonction qui renvoie les marins présents sur des rames à gauche
     * @param equimentManager
     * @return
     */
    public List<Marine> leftMarinsOnOars(EquipmentManager equimentManager){
        return marins.stream().filter(m->equimentManager.oarPresentAt(m.getPosition())).collect(Collectors.toList());

    }
    /**
     * Fonction qui renvoie les marins présents sur des rames à droite
     * @param equimentManager
     * @return
     */
    public List<Marine> rightMarinsOnOars(EquipmentManager equimentManager){
        //TODO
        return null;
    }


    @Override
    public String toString() {
        return marins.toString();
    }

    
    
}