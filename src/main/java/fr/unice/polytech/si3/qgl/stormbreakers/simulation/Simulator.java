package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.io.IOException;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.Cockpit;

class Simulator {

    public static void main(String[] args) throws IOException {
        Simulator sim=new Simulator();
        Cockpit c=new Cockpit();

        sim.launchGame(c);
        System.out.println(sim.firstNextRound(c));



    }

    void launchGame(ICockpit ic) throws IOException {

        ic.initGame(new String(this.getClass().getResourceAsStream("/simul/init5.json").readAllBytes()));

    }

    String firstNextRound(ICockpit ic) throws IOException {
        return ic.nextRound(new String(this.getClass().getResourceAsStream("/simul/round1.json").readAllBytes()));
    }

    
}