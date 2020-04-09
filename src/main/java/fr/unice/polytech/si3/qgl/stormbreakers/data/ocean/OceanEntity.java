package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Surface;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.bumps.VisibleEntity;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")

@JsonSubTypes({ @JsonSubTypes.Type(value = Courant.class, name = "stream"),
    @JsonSubTypes.Type(value = OtherBoat.class, name = "ship"),
    @JsonSubTypes.Type(value = Recif.class, name = "reef") })

public abstract class OceanEntity implements Surface , VisibleEntity {
  private String type;
  protected Position position;
  protected Shape shape;

  @JsonCreator
  OceanEntity(@JsonProperty("type") String type, @JsonProperty("position") Position position,
      @JsonProperty("shape") Shape shape) {
    this.type = type;
    this.position = position;
    this.shape = shape;
    shape.setAnchor(position);
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public abstract OceanEntityType getEnumType();

  @JsonProperty("position")
  public Position getPosition() {
    return this.position;
  }

  @JsonProperty("shape")
  public Shape getShape() {
    return shape;
  }

  @Override
  public double x() {
    return this.position.x();
  }

  @Override
  public double y() {
    return this.position.y();
  }

}
