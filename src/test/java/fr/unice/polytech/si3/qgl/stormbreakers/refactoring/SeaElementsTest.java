package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;

public class SeaElementsTest {
    

    SeaElements seaElements;
    @BeforeEach
    void setUp(){
        seaElements=new SeaElements(null, null, null);
    }

    @Test
    void additionalSpeedExistsTest(){
        this.seaElements = new SeaElements(null, null, null);
        assertFalse(this.seaElements.additionalSpeedExists());

        this.seaElements = new SeaElements(new Wind(1.253, 200), null, null);
        assertTrue(this.seaElements.additionalSpeedExists());
    }


    @Test
    void currentExternalSpeedTest(){
        assertEquals(0.0,this.seaElements.currentExternalSpeed(),"Par défaut la vitesse est 0.0");
    }
}