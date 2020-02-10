package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.Cockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;

/**
 * Classe de gestion de la simulation
 */
class Simulator {

    Crew crew;
    EquipmentManager equipmentManager;
    Boat boat;
    InputParser ip = new InputParser();
    private static final int NBSTEP = 100;
    private ICockpit cockpit;
    private InputProvider inputProvider;
    private Calculator calculator;

    public static void main(String[] args) throws IOException {

        Simulator sim = new Simulator(new Cockpit(), new InputProvider() {

            @Override
            public String provideInit() throws IOException {
                return new String(this.getClass().getResourceAsStream("/simul/init5.json").readAllBytes());
            }

            @Override
            public String provideFirstRound() throws IOException {
                return new String(this.getClass().getResourceAsStream("/simul/round1.json").readAllBytes());
            }
        });
        sim.setUpSimulation();
        sim.startSimulation();

    }

    Simulator(ICockpit cockpit, InputProvider inputProvider){
        this.cockpit = cockpit;
        this.inputProvider = inputProvider;
        this.calculator=new Calculator();

    }

    /**
     * Fonction pour créer le Boat 'virtuel' pour executer les actions des marins
     * 
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws IOException
     */

    void setUpSimulation() throws JsonProcessingException, IOException {
        this.crew = new Crew(ip.fetchAllSailors(inputProvider.provideInit()));
        this.equipmentManager = new EquipmentManager(ip.fetchEquipments(inputProvider.provideInit()),
                ip.fetchWidth(inputProvider.provideInit()));
        boat = new Boat(this.crew,this.equipmentManager);

    }

    /**
     * fonction pour créer le cokpit lui passer les données d'initialisation et lui
     * renvoyer les nextRound
     * 
     * @throws IOException
     */
    void startSimulation() throws IOException {
        this.cockpit.initGame(inputProvider.provideInit());
        String actions = this.cockpit.nextRound(inputProvider.provideFirstRound());
        List<MoveAction> moves = ip.fetchMoves(actions);
        List<SailorAction> sailorActions = ip.fetchActionsExceptMoveAction(actions);
        this.crew.executeMoves(moves);
        this.crew.executeActions(sailorActions);
        
    }

    double angleRotation() {
        double radialSpeed = 0;
        double rotationSpeedPerOar = (Math.PI) / this.equipmentManager.nbOars();
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

        radialSpeed+=this.equipmentManager.rudderRotation();

        return radialSpeed;
    }

    double speed(){
        return (165*this.equipmentManager.nbUsedOars())/(double)this.equipmentManager.nbOars();
    }

    

}