package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;

/**
 * This class is used to create the JSON formatted String
 */

public class OutputBuilder {

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
            Logger.getInstance().logLine(e.getMessage());
        }

        return jsonOutput;

    }

}
