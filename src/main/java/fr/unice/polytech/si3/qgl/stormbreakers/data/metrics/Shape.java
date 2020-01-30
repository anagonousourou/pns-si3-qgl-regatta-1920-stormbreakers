package fr.unice.polytech.si3.qgl.stormbreakers.data.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Circle.class, name="circle"),
        @JsonSubTypes.Type(value = Rectangle.class, name="rectangle")
})
public abstract class Shape {
    private String type;

    @JsonCreator
    Shape(@JsonProperty("type") String type) {
        this.type = type;
    }
    @JsonProperty("type")
	public String getType() {
		return type;
	}
}
