package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;

public class Courant extends OceanEntity {
    private double strength;

    Courant(Position position, Shape shape, double strength) {
        super("stream",position,shape);
        this.strength = strength;
    }

	public double getStrength() {
		return strength;
	}

}
