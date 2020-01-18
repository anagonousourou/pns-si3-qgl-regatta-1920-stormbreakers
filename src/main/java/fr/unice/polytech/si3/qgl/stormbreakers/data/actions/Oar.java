package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Oar extends SailorAction {

    @JsonCreator
    public Oar(@JsonProperty("sailorId") int sailorId) {
        super(sailorId,"OAR");
    }

}
