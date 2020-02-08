package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

class Oar {
    int x, y;
    Marine marin;
    boolean used = false;

    Oar(int x, int y) {
        this.x = x;
        this.y = y;
    }

    boolean marinPresent() {
        return false;

    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    void setMarin(Marine m) {
        this.marin = m;
    }

    @Override
    public String toString() {
        return "Oar(x: " + x + ", " + "y: " + y + " )";
    }

    boolean isUsed() {
        return this.used;
    }

    void setUsed(boolean b) {
        this.used = b;
    }

    void resetUsed() {
        this.used = false;
    }
}
