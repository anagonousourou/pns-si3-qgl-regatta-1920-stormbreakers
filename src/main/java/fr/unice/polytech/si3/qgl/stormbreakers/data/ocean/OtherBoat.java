package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public class OtherBoat extends OceanEntity {
    private int life;

    public OtherBoat(@JsonProperty("position") Position position, @JsonProperty("shape") Shape shape, @JsonProperty("life") int life) {
        super("ship", position, shape);
        this.life = life;
    }

    @Override
    public double getOrientation() {
        return this.position.getOrientation();
    }

    @Override
    public OceanEntityType getEnumType() {
        return OceanEntityType.BOAT;
    }

    /**
     * @return int return the life
     */
    public int getLife() {
        return life;
    }

    /**
     * @param life the life to set
     */
    public void setLife(int life) {
        this.life = life;
    }
    
    

}