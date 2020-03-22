package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ElementsConstructorTest {

    private ElementsConstructor constructor;
    private String game;
    private String round1;

    @BeforeEach
    public void setUp() throws IOException {
        game = new String(this.getClass().getResourceAsStream("/elementstest/init.json").readAllBytes());
        round1 = new String(this.getClass().getResourceAsStream("/elementstest/round1.json").readAllBytes());
        constructor = new ElementsConstructor(game);
    }

    @Test
    public void actionsTest(){
        assertDoesNotThrow(()->System.out.println(constructor.sendActions(round1)));
    }

}