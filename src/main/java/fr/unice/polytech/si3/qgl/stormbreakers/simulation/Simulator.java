package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.Cockpit;

class Simulator {

    Crew crew;
    String init;
    String round1;
    EquimentManager equimentManager;
    Boat boat;
    InputParser ip=new InputParser();
    public static void main(String[] args) throws IOException {

        Simulator sim = new Simulator();
        sim.startSimulation();
        /*

        sim.launchGame(c);
        System.out.println(sim.firstNextRound(c));*/

    }

    Simulator() throws IOException {
        this.init=new String(this.getClass().getResourceAsStream("/simul/init5.json").readAllBytes());
        this.round1=new String(this.getClass().getResourceAsStream("/simul/round1.json").readAllBytes());
    }

    void setUpSimulation() throws JsonMappingException, JsonProcessingException, IOException {
       

        this.crew= new Crew( ip.fetchAllSailors(init));

        this.equimentManager = new EquimentManager(ip.fetchAllOars(init), ip.fetchWidth(init));
        boat=new Boat();
        this.boat.setCrew(this.crew);
        System.out.println(this.crew.toList());
        System.out.println(this.equimentManager.toList());

    }

    void startSimulation() throws JsonMappingException, JsonProcessingException {
        Cockpit c = new Cockpit();
        launchGame(c);
        String actions= firstNextRound(c);
        List<MoveAction> moves=ip.fetchMoves(actions);




    }

    void launchGame(ICockpit ic){
        ic.initGame(init);
    }

    String firstNextRound(ICockpit ic){
        return ic.nextRound(round1);
    }

    
}