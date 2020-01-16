package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

public abstract class Equipment implements Deckable {
    private String type;
    private int x;
    private int y;

    Equipment(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
