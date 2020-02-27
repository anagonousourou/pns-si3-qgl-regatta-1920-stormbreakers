package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Sailor;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Checkpoint;

class InputParserTest {

        private InputParser parser;
        private String initGameExample1, initGameExample2;
        private String streams1;
        private String streams2;
        
        private List<Sailor> sailors;
        private List<Oar> oars;
        private List<Checkpoint> checkpoints;
        private List<Equipment> equipments;
        
        @BeforeEach
        void setUp() throws JsonProcessingException,IOException{
                parser = new InputParser();
                
                initGameExample1 = "{\"goal\": {\"mode\": \"REGATTA\",\"checkpoints\": [{\"position\": {\"x\": 1000,\"y\": 0,\"orientation\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 50}}]},\"shipCount\": 1,\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 2,\"length\": 1},\"entities\": [{\"x\": 0,\"y\": 0,\"type\": \"oar\"},{\"x\": 1,\"y\": 0,\"type\": \"oar\"}],\"shape\": {\"type\": \"rectangle\",\"width\": 2,\"height\": 3,\"orientation\": 0}},\"sailors\": [{\"x\": 0,\"y\": 0,\"id\": 0,\"name\": \"Edward Teach\"},{\"x\": 0,\"y\": 1,\"id\": 1,\"name\": \"Tom Pouce\"}]}";
                initGameExample2 = "{\"goal\": {\"mode\": \"REGATTA\",\"checkpoints\": [{\"position\": {\"x\": 1000,\"y\": 0,\"orientation\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 50}},{\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"shape\": {\"type\": \"circle\",\"radius\": 50}}]},\"shipCount\": 1,\"ship\": {\"type\": \"ship\",\"life\": 100,\"position\": {\"x\": 0,\"y\": 0,\"orientation\": 0},\"name\": \"Les copaings d'abord!\",\"deck\": {\"width\": 2,\"length\": 2},\"entities\": [{\"x\": 0,\"y\": 0,\"type\": \"oar\"},{\"x\": 1,\"y\": 0,\"type\": \"oar\"},{\"x\": 1,\"y\": 1,\"type\": \"rudder\"}],\"shape\": {\"type\": \"rectangle\",\"width\": 2,\"height\": 3,\"orientation\": 0}},\"sailors\": [{\"x\": 0,\"y\": 0,\"id\": 0,\"name\": \"Edward Teach\"},{\"x\": 0,\"y\": 1,\"id\": 1,\"name\": \"Tom Pouce\"}]}";

                sailors = parser.fetchAllSailors(initGameExample1);
                oars = parser.fetchAllOars(initGameExample1);
                checkpoints = parser.fetchCheckpoints(initGameExample2);
                equipments = parser.fetchEquipments(initGameExample2);

                streams1=new String(this.getClass().getResourceAsStream("/inputtest/test1.json").readAllBytes());

        }

        @Test
        void testFetchAllSailors(){	 
        	assertEquals(0, sailors.get(0).getId());
        	assertEquals(0, sailors.get(0).getPosition().getX());
        	assertEquals(0, sailors.get(0).getPosition().getY());
        	
        	assertEquals(1, sailors.get(1).getId());
        	assertEquals(0, sailors.get(1).getPosition().getX());
        	assertEquals(1, sailors.get(1).getPosition().getY());
        }
        
        @Test
        void testFetchAllOars() {
        	assertEquals(0, oars.get(0).getPosition().getX());
        	assertEquals(0, oars.get(0).getPosition().getY());
        	
        	assertEquals(1, oars.get(1).getPosition().getX());
        	assertEquals(0, oars.get(1).getPosition().getY());
        }
        
        @Test
        void testFetchCheckpoints() {
        	assertEquals(1000, checkpoints.get(0).getPosition().getX());
        	assertEquals(0, checkpoints.get(0).getPosition().getY());
        	assertEquals("circle", checkpoints.get(0).getShape().getType());
        	
        	assertEquals(0, checkpoints.get(1).getPosition().getX());
        	assertEquals(0, checkpoints.get(1).getPosition().getY());
        	assertEquals("circle", checkpoints.get(1).getShape().getType());
        }
        
        @Test
        void testFetchEquipments() {
        	assertEquals(0, equipments.get(0).getPosition().getX());
        	assertEquals(0, equipments.get(0).getPosition().getY());
        	assertEquals("oar", equipments.get(0).getType());
        	
        	assertEquals(1, equipments.get(1).getPosition().getX());
        	assertEquals(0, equipments.get(1).getPosition().getY());
        	assertEquals("oar", equipments.get(1).getType());
        	
        	assertEquals(1, equipments.get(2).getPosition().getX());
        	assertEquals(1, equipments.get(2).getPosition().getY());
        	assertEquals("rudder", equipments.get(2).getType());
        }

        @Disabled
        @Test
        void fetchStreamsTest() throws JsonProcessingException {
               var result= parser.fetchStreams(streams1);
                
               assertEquals(1, result.size(), "Un seul courant dans l'input");

               assertEquals(40,result.get(0).getStrength(), "should be 40"); 
               
        }
        
}