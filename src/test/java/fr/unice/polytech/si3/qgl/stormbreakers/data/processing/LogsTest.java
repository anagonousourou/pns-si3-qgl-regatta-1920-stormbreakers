package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class LogsTest {

    private Logger logger = Logger.getInstance();

    

    @AfterEach
    void cleanUp() {
        logger.reset();
    }

    
    
    @Test
    void testLog() {
    	cleanUp();
    	logger.log("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
    	assertEquals(1, logger.getSavedData().size());
    	
    	cleanUp();
    	logger.log("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisiutaliquip ex ea commodo consequat.");
    	assertEquals(2, logger.getSavedData().size());
    	logger.getSavedData().forEach(l -> { assertTrue(l.length() <= 200); });
    }

}