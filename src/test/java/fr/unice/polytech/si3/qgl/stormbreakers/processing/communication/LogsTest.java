package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

import fr.unice.polytech.si3.qgl.stormbreakers.Cockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.game.InitGame;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Deck;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Goal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.RegattaGoal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogsTest {

    private Logger logger = Logger.getInstance();
    private String initGameExample;
    private String nextRoundExample;
    private Cockpit cockpit;

    /* --------------------------------
                   InitGame
       -------------------------------- */

    private InitGame initGameForLog = genInitGame(4,8,10);

    private InitGame genInitGame(int largeurBateau, int longueurBateau, int nbCheckpoints) {
        Goal goal = genGoal(nbCheckpoints);
        Bateau ship = genShip(largeurBateau,longueurBateau,1);
        List<Marin> sailors = genSailors(largeurBateau,longueurBateau);

        return new InitGame(goal,ship,sailors,1);
    }

    private Goal genGoal(int nbCheckpoints) {
        List<Checkpoint> checkpoints = new ArrayList<>();
        for (int i=0; i<nbCheckpoints; i++) {
            Position pos = new Position(1000+(10*i),1000-(10*i));
            checkpoints.add(new Checkpoint(pos, new Rectangle(10,10,Math.PI/3)));
        }
        return new RegattaGoal(checkpoints);
    }

    private Bateau genShip(int largeur, int longueur, int distRames) {
        Position position = new Position(0,0,Math.PI/6);
        Shape shape = new Rectangle(100,100,0);
        Deck deck = new Deck(largeur,longueur);
        List<Equipment> entities = new ArrayList<>();
        for (int i=1; i<longueur; i+=(distRames+1)) {
            entities.add(new Oar(i,0));
            entities.add(new Oar(i,largeur-1));
        }
        return new Bateau(position,shape,100,deck,entities);
    }

    private List<Marin> genSailors(int largeur, int longueur) {
        List<Marin> sailors = new ArrayList<>();
        int id = 0;
        for (int x=0; x<longueur; x+=2) {
            sailors.add(new Marin(id, x, 0, "BaBob"));
            id++;
            sailors.add(new Marin(id, x, largeur, "TriBob"));
            id++;
        }
        return sailors;
    }

    @Test
    void testInitLogsComplete() {
        String log = initGameForLog.toLogs();
        assertTrue(log.contains("CP"));
        assertTrue(log.contains("d:"));
        assertTrue(log.contains("E:"));
        assertTrue(log.contains("S:"));
    }

    @Test
    void testInitLogsNotTooLong() {
        String log = initGameForLog.toLogs();
        assertTrue(log.length() < 200);
    }



    /* --------------------------------
                  Round Logs
       -------------------------------- */

    @BeforeEach
    void setUp() {
        initGameExample = "{\"goal\": {\"mode\": \"REGATTA\",\"checkpoints\": [{\"position\": {\"x\": 1000,\"y\": 0,\"orientation\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 50}}]},\"shipCount\": 1,\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 2,\"length\": 2},\"entities\": [{\"x\": 0,\"y\": 0,\"type\": \"oar\"},{\"x\": 0,\"y\": 1,\"type\": \"oar\"}],\"shape\": {\"type\": \"rectangle\",\"width\": 2,\"height\": 3,\"orientation\": 0}},\"sailors\": [{\"x\": 0,\"y\": 0,\"id\": 0,\"name\": \"Edward Teach\"},{\"x\": 0,\"y\": 1,\"id\": 1,\"name\": \"Tom Pouce\"}]}";
        nextRoundExample = "{\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 2.05},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 2,\"length\": 1},\"entities\": [{\"x\": 0,\"y\": 0,\"type\": \"oar\"},{\"x\": 1,\"y\": 0,\"type\": \"oar\"}]},\"visibleEntities\": []}";
        cockpit = new Cockpit();
    }

    @AfterEach
    void cleanUp() {
        logger.reset();
    }

    @Test
    void testLogCompleteWhileInRound() {
        cockpit.initGame(initGameExample);
        cleanUp();

        cockpit.nextRound(nextRoundExample);

        String log = logger.getSavedData().get(0);
        assertTrue(log.contains("CP"));
        assertTrue(log.contains("A"));
        assertTrue(log.contains("a"));
    }

}