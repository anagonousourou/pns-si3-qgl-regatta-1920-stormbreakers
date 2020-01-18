package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Deck {
    private int width;
    private int length;

    @JsonCreator
    Deck(
            @JsonProperty("width") int width,
            @JsonProperty("length") int length
    ) {
        this.width = width;
        this.length = length;
    }

    @JsonProperty("width")
    public int getWidth() {
        return width;
    }

    @JsonProperty("length")
    public int getLength() {
        return length;
    }
}
