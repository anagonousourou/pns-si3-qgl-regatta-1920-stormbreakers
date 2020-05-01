package fr.unice.polytech.si3.qgl.stormbreakers.data.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logable;

/**
 * Represents any action a Sailor can Do
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = MoveAction.class, name = "MOVING"),
        @JsonSubTypes.Type(value = LiftSail.class, name = "LIFT_SAIL"),
        @JsonSubTypes.Type(value = LowerSail.class, name = "LOWER_SAIL"),
        @JsonSubTypes.Type(value = Turn.class, name = "TURN"),
        @JsonSubTypes.Type(value = OarAction.class, name = "OAR"),
        @JsonSubTypes.Type(value = UseWatch.class, name = "USE_WATCH") })
public abstract class SailorAction implements Logable {
    protected int sailorId;
    private String type;

    @JsonCreator
    public SailorAction(@JsonProperty("sailorId") int sailorId, @JsonProperty("type") String type) {
        this.sailorId = sailorId;
        this.type = type;
    }

    @JsonProperty("sailorId")
    public int getSailorId() {
        return sailorId;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public abstract String compatibleEquipmentType();
}
