package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Oar;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OutputBuilderTest {

    private OutputBuilder outputBuilder;

    @BeforeEach
    void setUp() {
        outputBuilder = new OutputBuilder();
    }


    @Test
    void testWriteActionsEmpty() {
        String out = outputBuilder.writeActions(new ArrayList<SailorAction>());
        assertEquals("[]", out);
    }

    @Test
    void testWriteActionsForSmallBoatExample() {
        List<SailorAction> actions = new ArrayList<>();

        actions.add(new Oar(0));
        actions.add(new Oar(1));

        String expected = "[{\"sailorId\": 0,\"type\": \"OAR\"},{\"sailorId\": 1,\"type\": \"OAR\"}]";
        expected = expected.replace(" ","");
        String out = outputBuilder.writeActions(actions);
        assertEquals(expected, out);
    }

}