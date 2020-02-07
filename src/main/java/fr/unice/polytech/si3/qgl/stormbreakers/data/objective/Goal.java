package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.unice.polytech.si3.qgl.stormbreakers.Logable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "mode")
@JsonSubTypes({ @JsonSubTypes.Type(value = RegattaGoal.class, name = "REGATTA"),
        @JsonSubTypes.Type(value = BattleGoal.class, name = "BATTLE") })

public abstract class Goal implements Logable {
    private String mode;

    @JsonCreator
    Goal(@JsonProperty("mode") String mode) {
        this.mode = mode;
    }

    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }


}
