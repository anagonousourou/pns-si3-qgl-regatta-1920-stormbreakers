package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;

import java.io.IOException;
import java.util.List;
class LogsTest {

    private Logger logger = Logger.getInstance();

    private String loremIpsum;

    @BeforeEach
    void fetchFile() throws IOException {
        loremIpsum = new String(this.getClass().getResourceAsStream("/logtest/lorem_ipsum.txt").readAllBytes());
    }

    @AfterEach
    void cleanUp() {
        logger.reset();
    }

    
    
    @Test
    void testLog() {
    	cleanUp();
    	logger.log("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
    	assertEquals(1, logger.getSavedData().size());
    }

    @Test
    void logTestWhenTwoLineLog() {
        cleanUp();
        logger.log("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisiutaliquip ex ea commodo consequat.");

        List<String> logData = logger.getSavedData();
        assertEquals(2, logData.size());
        logData.forEach(l -> { assertTrue(l.length() <= 200); });
    }

    @Test
    void logTestWhenMultiLineLog() {
        cleanUp();
        logger.log(loremIpsum);
        int nbLines = (int) Math.ceil(loremIpsum.length() / 200.0);
        List<String> logData = logger.getSavedData();
        assertEquals(nbLines, logData.size());
        logData.forEach(l -> { assertTrue(l.length() <= 200); });
    }

    @Test
    void logTestWhenPrecededByMultilineLog() {
        cleanUp();
        logger.log(loremIpsum);

        String toLog = "This is a test line which will be logged after a multiline log";
        logger.log(toLog);
        List<String> logData = logger.getSavedData();
        int nbLines = (int) Math.ceil( (loremIpsum.length() + toLog.length()) / 200.0);
        assertEquals(nbLines,logData.size());
        logData.forEach(l -> { assertTrue(l.length() <= 200); });
    }

}