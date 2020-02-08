package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.util.List;
import java.util.Optional;

public class Crew {

    List<Marine> marins;

    Crew(List<Marine> marins){
        this.marins=marins;
    }

    Optional<Marine> getMarinById(int id){
        return marins.stream().filter(m->m.getId()==id).findFirst();
    }
    
    List<Marine> toList(){
        return marins;
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
    
}