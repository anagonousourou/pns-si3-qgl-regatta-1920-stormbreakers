package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UseWatch extends SailorAction {

    @JsonCreator
    UseWatch(@JsonProperty("sailorId")  int sailorId) {
        super(sailorId,"USE_WATCH");
    }

}
