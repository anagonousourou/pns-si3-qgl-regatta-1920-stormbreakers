package fr.unice.polytech.si3.qgl.stormbreakers.tools.runner.serializing;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Deck;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;

import java.util.List;

public class Ship {
    @JsonProperty("type") static final String TYPE = "ship";

    @JsonProperty("life") int life;
    @JsonProperty("position") Position position;
    @JsonProperty("name") String name;
    @JsonProperty("deck") Deck deck;
    @JsonProperty("entities") List<Equipment> entities;
    @JsonProperty("shape") Shape shape;

    public Boat buildBoat() {
        return new Boat(position,deck.getLength(),deck.getWidth(),life,null,shape);
    }

    List<Equipment> getEquipments() {return entities;}

    public Position getPosition() { return position; }

    public void setPosition(Position newPos) {
        position = newPos;
    }
}