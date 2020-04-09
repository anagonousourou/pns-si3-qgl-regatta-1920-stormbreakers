package fr.unice.polytech.si3.qgl.stormbreakers.math;

import java.util.Objects;

public class IntPosition {
    private int x;
    private int y;

    public IntPosition(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public IntPosition(IntPosition other) {
        this.x = other.x;
        this.y = other.y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void add(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public MovementPath getPathTo(IntPosition target) {
        return new MovementPath(this, target);
    }

    @Override
    public String toString() {
        return String.format("%s(x: %d,y: %d)", this.getClass().getSimpleName(), x, y);
    }

    public int distanceTo(IntPosition position) {
        return Math.abs(this.x - position.x) + Math.abs(this.y - position.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof IntPosition))
            return false;
        IntPosition other = (IntPosition) obj;
        return other.x() == x && other.y() == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}