package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

/**
 * Analog to Vector but of ints
 * Represents a path from an IntPosition to another
 */

public class MovementPath {
    private int deltaX;
    private int deltaY;

    MovementPath(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    MovementPath(IntPosition start, IntPosition end) {
        this.deltaX = end.getX() - start.getX();
        this.deltaY = end.getY() - start.getY();
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }
}
