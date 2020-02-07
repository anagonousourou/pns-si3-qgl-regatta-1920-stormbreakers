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
    
}