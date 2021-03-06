package fr.unice.polytech.si3.qgl.stormbreakers.tools.runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.stormbreakers.Cockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.*;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.*;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Utils;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.*;
import fr.unice.polytech.si3.qgl.stormbreakers.math.IntPosition;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.runner.game.GameConfig;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.runner.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.runner.game.NextRound;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.runner.serializing.Ship;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.CrewManager;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.EquipmentsManager;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw.Displayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Engine {

    public static final int WEEK_NUM = 8; // CHANGE ME : [1,12]
    private static final int ITERATIONS = 500;

    private static final String RAW_PATH = "/tools/runner";

    private static final int MID_VISION = 1000;
    private static final int NB_STEP = 10;
    private static final double WIND = 0;


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

        int weekNb = (int) Utils.clamp(WEEK_NUM,1,12);
        if (weekNb==8) weekNb--; // Week 7 == Week 8

        String weekInputsPath = RAW_PATH + "\\week" + weekNb;

        initGameJson = new String(Engine.class.getResourceAsStream( weekInputsPath+ "/initgame.json").readAllBytes());
        initGame = mapper.readValue(initGameJson, InitGame.class);

        String gameJson = new String(Engine.class.getResourceAsStream(weekInputsPath + "/game.json").readAllBytes());
        game = mapper.readValue( gameJson, GameConfig.class );
    }

    public static void main(String[] args) throws IOException {
        Displayer displayer = new Displayer();

        Engine engine = new Engine(displayer);
        engine.runInitGame();

        engine.updateNextRound();

        for (int i=0; i<ITERATIONS; i++) {
            engine.runNextRound();
        }

        displayer.setShipShape(engine.boat.getShape());
        displayer.setReefs(engine.game.getReefs());
        displayer.setStreams(engine.game.getStreams());
        displayer.setCheckpoints(engine.game.getCheckpoints());

        displayer.showIndexingFor(new ArrayList<>(engine.game.getCheckpoints()));
        displayer.showWrappingShapes(engine.game.getReefs(), null, engine.boat.securityMargin());

        displayer.disp();
    }

    private List<OceanEntity> visibleEntitiesInRadius(Position position, int radius) {
        Circle visibleArea = new Circle(radius,position);
        return game.getEntities().stream().filter(ent -> ent.getShape().getBoundingCircle().collidesWith(visibleArea))
                .collect(Collectors.toList());
    }

    private void updateNextRound() {
        Position positionInit = new Position(ship.getPosition());
        int nbOarsRightActive = equipmentsManager.nbRightOars() - equipmentsManager.unusedRightOars().size();
        int nbOarsLeftActive  = equipmentsManager.nbLeftOars() - equipmentsManager.unusedLeftOars().size();
        int nbOars = equipmentsManager.nbOars();
        Rudder rudder = null;
        if (equipmentsManager.rudderIsPresent()) {
            Optional<Equipment> rudderOpt = equipmentsManager.equipmentAt(equipmentsManager.rudderPosition());
            if (rudderOpt.isPresent()) rudder = (Rudder) rudderOpt.get();
        }
        Wind wind = nextRound.getWind();
        List<Stream> streams = game.getStreams();
        int nbsail = equipmentsManager.nbSails();
        int nbSailOpenned = equipmentsManager.nbOpennedSails();
        Shape shipShape = boat.getShape();
        List<Reef> reefs = game.getReefs();

        Position newPos = EngineUtils.nextPosition(positionInit,nbOarsRightActive,nbOarsLeftActive,nbOars,
                rudder,wind,streams,nbsail,nbSailOpenned,shipShape,reefs,NB_STEP,displayer);

        if(displayer!=null) displayer.addPosition(newPos);


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
        IntPosition marinPos = crewManager.getSailorById(oarAction.getSailorId())
                .map(Sailor::getPosition).orElse(new IntPosition(0,0));
        Oar oar = (Oar) equipmentsManager.equipmentAt(marinPos).orElse(new Oar(0,0));
        oar.setUsed(true);
    }

    private void updateRudderOrientation(Turn turnAction) {
        double newRotation = turnAction.getRotation();
        IntPosition marinPos = crewManager.getSailorById(turnAction.getSailorId())
                .map(Sailor::getPosition).orElse(new IntPosition(0,0));
        Rudder rudder = (Rudder) equipmentsManager.equipmentAt(marinPos).orElse(new Sail(0,0));
        rudder.setOrientation(newRotation);
    }

    private void updateSailsState(SailorAction actionWithSail) {
        if (ActionType.LIFTSAIL.actionCode.equals(actionWithSail.getType())) {
            LiftSail liftSail = (LiftSail) actionWithSail;
            IntPosition marinPos = crewManager.getSailorById(liftSail.getSailorId())
                    .map(Sailor::getPosition).orElse(new IntPosition(0,0));
            Sail sail = (Sail) equipmentsManager.equipmentAt(marinPos).orElse(new Sail(0,0));
            sail.setOpenned(true);
        } else if (ActionType.LOWERSAIL.actionCode.equals(actionWithSail.getType())) {
            LiftSail liftSail = (LiftSail) actionWithSail;
            IntPosition marinPos = crewManager.getSailorById(liftSail.getSailorId())
                    .map(Sailor::getPosition).orElse(new IntPosition(0,0));
            Sail sail = (Sail) equipmentsManager.equipmentAt(marinPos).orElse(new Sail(0,0));
            sail.setOpenned(false);
        }
    }


}




