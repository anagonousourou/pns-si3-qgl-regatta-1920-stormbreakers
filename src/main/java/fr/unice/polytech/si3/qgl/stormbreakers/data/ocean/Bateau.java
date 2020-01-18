package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Deck;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;

import java.util.List;

public class Bateau extends AutreBateau {
    private String name;
    private Deck deck;
    private List<Equipment> entities;

    Bateau(Position position, Shape shape, int life, String name, Deck deck, List<Equipment> entities) {
        super(position,shape,life);
        this.name = name;
        this.deck = deck;
        this.entities = entities;
    }

}
