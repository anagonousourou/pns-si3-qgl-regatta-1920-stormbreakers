package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

/**
 * Classe pour representer l'action de Ramer
 */
public class OarAction {

    int id;

    OarAction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}