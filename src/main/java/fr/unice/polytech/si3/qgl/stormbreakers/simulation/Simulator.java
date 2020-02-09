package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.Cockpit;
/**
 * Classe de gestion de la simulation
 */
class Simulator {

    Crew crew;
    String init;
    String round1;
    EquipmentManager equipmentManager;
    Boat boat;
    InputParser ip = new InputParser();

    public static void main(String[] args) throws IOException {

        Simulator sim = new Simulator();
        sim.setUpSimulation();
        sim.startSimulation();
        /*
         * 
         * sim.launchGame(c); System.out.println(sim.firstNextRound(c));
         */

    }

    Simulator() throws IOException {
        this.init = new String(this.getClass().getResourceAsStream("/simul/init5.json").readAllBytes());
        this.round1 = new String(this.getClass().getResourceAsStream("/simul/round1.json").readAllBytes());
    }

    /**
     * Fonction pour créer le Boat 'virtuel' pour executer les actions des marins
     * 
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws IOException
     */

    void setUpSimulation() throws JsonMappingException, JsonProcessingException, IOException {
        this.crew = new Crew(ip.fetchAllSailors(init));
        this.equipmentManager = new EquipmentManager(ip.fetchAllOars(init), ip.fetchWidth(init));
        boat = new Boat();
        this.boat.setCrew(this.crew);
        this.boat.setEquipmentManager(this.equipmentManager);
        System.out.println("Crew 0:" + this.crew);

    }

    /**
     * fonction pour créer le cokpit lui passer les données d'initialisation et lui
     * renvoyer les nextRound
     * 
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    void startSimulation() throws JsonMappingException, JsonProcessingException {
        Cockpit c = new Cockpit();
        launchGame(c);
        String actions = firstNextRound(c);
        List<MoveAction> moves = ip.fetchMoves(actions);
        List<OarAction> oarActions = ip.fetchOarActions(actions);
        System.out.println(moves);
        this.crew.executeMoves(moves);
        this.crew.executeActions(oarActions);
        System.out.println("Rotation: "+this.angleRotation());
        //System.out.println("\n Crew 1:" + this.crew.toList());
    }

    void launchGame(ICockpit ic) {
        ic.initGame(init);
    }

    String firstNextRound(ICockpit ic) {
        return ic.nextRound(round1);
    }

    double angleRotation() {
        double radialSpeed = 0;
        double rotationSpeedPerOar = (Math.PI/2)/this.equipmentManager.nbOars();
        // OARS
        for (Oar oar : this.equipmentManager.oars()) {
            if (oar.isUsed()) {
                if (this.equipmentManager.allRightOars().contains(oar)) {
                    radialSpeed += rotationSpeedPerOar;
                } else if (this.equipmentManager.allLeftOars().contains(oar)) {
                    radialSpeed -= rotationSpeedPerOar;
                }
            }
        }

        return radialSpeed;
    }

}