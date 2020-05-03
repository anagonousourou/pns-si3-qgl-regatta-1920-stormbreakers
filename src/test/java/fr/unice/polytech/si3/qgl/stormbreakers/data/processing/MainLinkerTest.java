package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainLinkerTest {

    private MainLinker constructor;
    private String gameArchipel;
    private String gameHarbour;
    private String gameSnake;
    private String roundSnake;
    private String roundHarbour;
    private String roundHarbour2;
    private String roundArchipel;

    @BeforeEach
    public void setUp() throws IOException {
        gameSnake=new String(this.getClass().getResourceAsStream("/elementstest/init_snake.json").readAllBytes());
        gameHarbour = new String(this.getClass().getResourceAsStream("/elementstest/init_harbour.json").readAllBytes());
        
        gameArchipel = new String(
                this.getClass().getResourceAsStream("/elementstest/init_archipel.json").readAllBytes());
        
        roundArchipel = new String(
                this.getClass().getResourceAsStream("/elementstest/round_archipel.json").readAllBytes());
        constructor = new MainLinker(gameArchipel);
        roundHarbour = new String(
                this.getClass().getResourceAsStream("/elementstest/round_harbour.json").readAllBytes());
                roundHarbour2 = new String(
                this.getClass().getResourceAsStream("/elementstest/round_harbour2.json").readAllBytes());
        roundSnake=new String(
            this.getClass().getResourceAsStream("/elementstest/round_snake.json").readAllBytes()
        );
    }

    @Test
    public void actionsTest() {

        assertDoesNotThrow(() -> constructor.sendActions(roundArchipel));
    }

    @Test
    public void harbourTest() {

        constructor = new MainLinker(gameHarbour);
        assertDoesNotThrow(() -> (constructor.sendActions(roundHarbour)));
    }

    @Test
    public void harbour2Test() {

        constructor = new MainLinker(gameHarbour);
        assertDoesNotThrow(() -> (constructor.sendActions(roundHarbour2)));
    }


    @Test
    public void snakeTest(){
        constructor=new MainLinker(gameSnake);
        assertDoesNotThrow(()->(constructor.sendActions(roundSnake)));
    }


}