package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.ObservableData;

public class ObservableDataTest {

    ObservableData observableData = new ObservableData();
    String gameInit;
    String round;

    void setUp() throws IOException {
        gameInit=new String(this.getClass().getResourceAsStream("/observabletest/init5.json").readAllBytes());
        round=new String(this.getClass().getResourceAsStream("/observabletest/round1.json").readAllBytes());
    }

    @Test
    void gettersSettersTest(){
        observableData.setValue("Bienvenue");
        assertEquals("Bienvenue", observableData.getValue());
    }

    
}