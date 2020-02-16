package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

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