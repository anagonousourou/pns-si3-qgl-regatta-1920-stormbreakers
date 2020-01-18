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
@JsonSubTypes.Type(value = AutreBateau.class, name="ship"), // Example doc
})

public abstract class OceanEntity {
    private String type;
    private Position position;
    private Shape shape;
    
    @JsonCreator
    OceanEntity(
    		@JsonProperty("type") String type,
    		@JsonProperty("postion") Position position,
    		@JsonProperty("shape") Shape shape) {
        this.type = type;
        this.position = position;
        this.shape = shape;
    }
    
    @JsonProperty("type")
    public String getType() {
		return type;
	}

    @JsonProperty("position")
    public Position getPosition() {
		return position;
	}
    
    @JsonProperty("shape")
    public Shape getShape() {
		return shape;
	}
}
