package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.OarAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.UseWatch;
import fr.unice.polytech.si3.qgl.stormbreakers.io.json.JsonOutputBuilder;

class OutputBuilderTest {

    private JsonOutputBuilder outputBuilder;

    @BeforeEach
    void setUp() {
        outputBuilder = new JsonOutputBuilder();
    }


    @Test
    void testWriteActionsEmpty() {
        String out = outputBuilder.writeActions(new ArrayList<SailorAction>());
        assertEquals("[]", out);
    }

    @Test
    void testWriteActionsForSmallBoatExample() {
        List<SailorAction> actions = new ArrayList<>();

        actions.add(new OarAction(0));
        actions.add(new OarAction(1));

        String expected = "[{\"sailorId\": 0,\"type\": \"OAR\"},{\"sailorId\": 1,\"type\": \"OAR\"}]";
        expected = expected.replace(" ","");
        String out = outputBuilder.writeActions(actions);
        assertEquals(expected, out);
    }

    @Test
    void watchTest(){
        List<SailorAction> actions=new ArrayList<>();

        actions.add(new UseWatch(1));
        assertEquals("[{\"sailorId\":1,\"type\":\"USE_WATCH\"}]",outputBuilder.writeActions(actions));

    }

}