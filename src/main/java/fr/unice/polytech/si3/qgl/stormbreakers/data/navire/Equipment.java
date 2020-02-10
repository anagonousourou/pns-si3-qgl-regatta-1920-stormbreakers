package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.unice.polytech.si3.qgl.stormbreakers.Logable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Oar.class, name = "oar") ,
        // @JsonSubTypes.Type(value = Voile.class, name="sail"),
        @JsonSubTypes.Type(value = Gouvernail.class, name="rudder")//,
        // @JsonSubTypes.Type(value = Vigie.class, name="watch")
})

public abstract class Equipment implements Deckable, Logable {
    protected String type;
    protected int x;
    protected int y;
    protected boolean used =false;

    @JsonCreator
    Equipment(@JsonProperty("type") String type, @JsonProperty("x") int x, @JsonProperty("y") int y) {
        this.type = type;
        this.x = x;
        this.y = y;
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
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

}
