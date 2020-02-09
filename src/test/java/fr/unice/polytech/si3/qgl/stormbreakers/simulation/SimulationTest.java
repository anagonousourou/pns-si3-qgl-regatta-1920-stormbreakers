package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimulationTest {

    Calculator calculator;

    @BeforeEach
    void setUp() throws IOException {
        calculator = new Calculator();
    }

    @Test
    void nextXTest() {
        double delta = Math.abs(86.38 - calculator.nextX(0, 100, 10, 0, 1, 10));
        assertTrue(delta < 0.01);

    }

    @Test
    void nextYTest(){
        double delta = Math.abs(41.72 - calculator.nextY(0, 100, 10, 0, 1, 10));
        System.out.println(delta);
        assertTrue(delta < 0.01);
    }
}