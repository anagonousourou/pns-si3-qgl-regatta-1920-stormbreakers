package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
include = JsonTypeInfo.As.PROPERTY,
property = "type")
@JsonSubTypes({
@JsonSubTypes.Type(value = Bateau.class, name="ship"), // Example doc
})

public class AutreBateau extends OceanEntity {
    private int life;
    
    @JsonCreator
    AutreBateau(@JsonProperty("position") Position position,
                @JsonProperty("shape") Shape shape,
                @JsonProperty("life") int life) {
        super("ship", position,shape);
        this.life=life;
    }
    
    @JsonProperty("life")
    public int getLife() {
        return life;
    }
}
