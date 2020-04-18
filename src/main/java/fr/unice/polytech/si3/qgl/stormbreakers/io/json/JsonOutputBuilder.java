package fr.unice.polytech.si3.qgl.stormbreakers.io.json;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.io.OutputBuilder;

/**
 * This class is used to create the JSON formatted String
 */

public class JsonOutputBuilder implements OutputBuilder {

    /**
     * Genere la chaine JSON a partir d'une liste d'action
     * 
     * @param actions liste d'actions a traduire
     * @return la chaine formattee en JSON
     */
    public String writeActions(List<SailorAction> actions) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOutput = "[]";

        try {
            jsonOutput = objectMapper.writeValueAsString(actions);

        } catch (IOException e) {
            Logger.getInstance().log("Error writing actions");
            Logger.getInstance().logErrorMsg(e);
        }

        return jsonOutput;

    }

}
