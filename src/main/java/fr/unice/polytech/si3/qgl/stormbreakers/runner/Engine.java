package fr.unice.polytech.si3.qgl.stormbreakers.runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.stormbreakers.Cockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.*;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.*;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;
import fr.unice.polytech.si3.qgl.stormbreakers.runner.game.GameConfig;
import fr.unice.polytech.si3.qgl.stormbreakers.runner.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.runner.game.NextRound;
import fr.unice.polytech.si3.qgl.stormbreakers.runner.serializing.Ship;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CrewManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.Displayer;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.PosDrawing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Engine {

    private static final int MID_VISION = 1000;
    private static final int NB_STEP = 10;
    private static final double WIND = 0;

    private static final String RAW_PATH = "/raw.runner";
    private String initGameJson;
    private InitGame initGame;
    private GameConfig game;
    private NextRound nextRound;
    private ObjectMapper mapper;

    private final Boat boat;
    private Ship ship;
    private Cockpit mjollnir;
    private Displayer displayer;

    private CrewManager crewManager;
    private EquipmentsManager equipmentsManager;

    Engine(Displayer displayer) throws IOException {
        this.displayer = displayer;

        setupGameObj();

        ship = initGame.getShip();
        boat = ship.buildBoat();

        nextRound = new NextRound(initGame.getShip(),new Wind(0,WIND), visibleEntitiesInRadius(ship.getPosition(),MID_VISION));

        crewManager = new CrewManager(initGame.getSailors());
        equipmentsManager = new EquipmentsManager(initGame.getEquipments(),boat.getDeckwidth(),null);

        mjollnir = new Cockpit();
    }

    private void setupGameObj() throws IOException {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(MapperFeature.AUTO_DETECT_GETTERS);


        initGameJson = new String(Engine.class.getResourceAsStream(RAW_PATH + "/initW9fise.json").readAllBytes());
        initGame = mapper.readValue(initGameJson, InitGame.class);

        String gameJson = new String(Engine.class.getResourceAsStream(RAW_PATH + "/gameW9fise.json").readAllBytes());
        game = mapper.readValue( gameJson, GameConfig.class );
    }

    public static void main(String[] args) throws IOException {
        Displayer displayer = new Displayer();

        Engine engine = new Engine(displayer);
        engine.runInitGame();

        engine.updateNextRound();

        System.out.println(engine.ship.getPosition());
        for (int i=0; i<200; i++) {
            engine.runNextRound();
            System.out.println(engine.ship.getPosition());
        }

        displayer.setShipShape(engine.boat.getShape());
        displayer.setReefs(engine.game.getReefs());
        displayer.setStreams(engine.game.getStreams());
        displayer.setCheckpoints(engine.game.getCheckpoints());

        System.out.println(engine.mjollnir.getLogs());
        displayer.disp();
    }

    private List<OceanEntity> visibleEntitiesInRadius(Position position, int radius) {
        // TODO: 02/04/2020 Add Higher radius support
        return game.getEntities().stream().filter(ent -> position.distanceTo(ent.getPosition()) <= radius)
                .collect(Collectors.toList());
    }

    private void updateNextRound() {
        Position positionInit = new Position(ship.getPosition());
        int nbOarsRightActive = equipmentsManager.nbRightOars() - equipmentsManager.unusedRightOars().size();
        int nbOarsLeftActive  = equipmentsManager.nbLeftOars() - equipmentsManager.unusedLeftOars().size();
        int nbOars = equipmentsManager.nbOars();
        Gouvernail rudder = (Gouvernail) equipmentsManager.equipmentAt(equipmentsManager.rudderPosition()).orElse(null);
        Wind wind = nextRound.getWind();
        List<Courant> streams = game.getStreams();
        int nbsail = equipmentsManager.nbSails();
        int nbSailOpenned = equipmentsManager.nbOpennedSails();
        Shape shipShape = boat.getShape();
        List<OceanEntity> reefs = game.getEntities();

        Position newPos = EngineUtils.nextPosition(positionInit,nbOarsRightActive,nbOarsLeftActive,nbOars,
                rudder,wind,streams,nbsail,nbSailOpenned,shipShape,reefs,NB_STEP,displayer);

        if(displayer!=null) displayer.addDrawing(new PosDrawing(newPos));


        nextRound.setShipPos(newPos);
         nextRound.setVisibleEntities(visibleEntitiesInRadius(ship.getPosition(),MID_VISION));
    }

    void runInitGame() {
        mjollnir.initGame(initGameJson);
    }

    void runNextRound() {
        String nextRoundJson;
        List<SailorAction> crewActions;
        resetEquipment();
        try {
            nextRoundJson = mapper.writer().writeValueAsString(nextRound);
            String crewActionsJson =  mjollnir.nextRound(nextRoundJson);
            crewActions = mapper.readValue(crewActionsJson, new TypeReference<List<SailorAction>>(){});
        } catch (JsonProcessingException e) {
            crewActions = new ArrayList<>();
            e.printStackTrace();
        }
        updateInfo(crewActions);
    }

    private void resetEquipment() {
        equipmentsManager.resetUsedStatus();
    }

    private void updateInfo(List<SailorAction> actions) {
        // Move sailors around
        crewManager.executeMovingsInSailorAction(actions);

        for (SailorAction action : actions) {
            // Voiles
            if (ActionType.LIFTSAIL.actionCode.equals(action.getType())
                    || ActionType.LOWERSAIL.actionCode.equals(action.getType())) {
                updateSailsState(action);
            }
            // Gouvernail
            if (ActionType.TURN.actionCode.equals(action.getType())) {
                updateRudderOrientation((Turn) action);
            }

            if (ActionType.OAR.actionCode.equals(action.getType())) {
                updateOarsState((OarAction) action);
            }
        }

        updateNextRound();
    }

    private void updateOarsState(OarAction oarAction) {
        IntPosition marinPos = crewManager.getMarinById(oarAction.getSailorId())
                .map(Sailor::getPosition).orElse(new IntPosition(0,0));
        Oar oar = (Oar) equipmentsManager.equipmentAt(marinPos).orElse(new Oar(0,0));
        oar.setUsed(true);
    }

    private void updateRudderOrientation(Turn turnAction) {
        double newRotation = turnAction.getRotation();
        IntPosition marinPos = crewManager.getMarinById(turnAction.getSailorId())
                .map(Sailor::getPosition).orElse(new IntPosition(0,0));
        Gouvernail rudder = (Gouvernail) equipmentsManager.equipmentAt(marinPos).orElse(new Gouvernail(0,0));
        rudder.setOrientation(newRotation);
    }

    private void updateSailsState(SailorAction actionWithSail) {
        if (ActionType.LIFTSAIL.actionCode.equals(actionWithSail.getType())) {
            LiftSail liftSail = (LiftSail) actionWithSail;
            IntPosition marinPos = crewManager.getMarinById(liftSail.getSailorId())
                    .map(Sailor::getPosition).orElse(new IntPosition(0,0));
            Sail sail = (Sail) equipmentsManager.equipmentAt(marinPos).orElse(new Sail(0,0));
            sail.setOpenned(true);
        } else if (ActionType.LOWERSAIL.actionCode.equals(actionWithSail.getType())) {
            LiftSail liftSail = (LiftSail) actionWithSail;
            IntPosition marinPos = crewManager.getMarinById(liftSail.getSailorId())
                    .map(Sailor::getPosition).orElse(new IntPosition(0,0));
            Sail sail = (Sail) equipmentsManager.equipmentAt(marinPos).orElse(new Sail(0,0));
            sail.setOpenned(false);
        }
    }


}




