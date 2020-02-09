package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;

public class Boat {
    private final int MAX_DISTANCE = 5;
    private Position position = null;
    private int deckwidth = 0;
    private int decklength = 0;
    private int life = 0;
    // TODO add a Shape field

    public Boat(Position position, int decklength, int deckwidth, int life) {
        this.position = position;
        this.decklength = decklength;
        this.deckwidth = deckwidth;
        this.life = life;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getDeckwidth() {
        return deckwidth;
    }

    public void setDeckwidth(int deckwidth) {
        this.deckwidth = deckwidth;
    }

    public int getDecklength() {
        return decklength;
    }

    public void setDecklength(int decklength) {
        this.decklength = decklength;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

}