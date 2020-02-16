package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.unice.polytech.si3.qgl.stormbreakers.Logable;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.IntPosition;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Oar.class, name = "oar") ,
        // @JsonSubTypes.Type(value = Sail.class, name="sail"),
        @JsonSubTypes.Type(value = Gouvernail.class, name="rudder")//,
        // @JsonSubTypes.Type(value = Vigie.class, name="watch")
})

public abstract class Equipment implements Logable {
    private String type;
    private boolean used = false;
    protected int x;
    protected int y;
    private final IntPosition position;

    @JsonCreator
    Equipment(@JsonProperty("type") String type, @JsonProperty("x") int x, @JsonProperty("y") int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        position=new IntPosition(x,y);
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("x")
    public int getX() {
        return x;
    }

    @JsonProperty("y")
    public int getY() {
        return y;
    }

    public String getPosLog() {
        return x+"."+y;
    }

    public boolean isUsed() {
		return this.used;
    }
    public IntPosition getPosition() {
        return position;
    }

    
    public void setUsed(boolean b) {
        this.used = b;
    }

    public void resetUsed() {
        this.used = false;
    }

}
