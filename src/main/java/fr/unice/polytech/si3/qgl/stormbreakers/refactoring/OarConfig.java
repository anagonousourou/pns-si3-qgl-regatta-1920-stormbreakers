package fr.unice.polytech.si3.qgl.stormbreakers.refactoring;

import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;

import java.util.Objects;

/**
 * Configuration de rames
 * aka différence de rames à activer entre gauche et droite du bateau
 * et angle associé
 */

public class OarConfig {
    private Fraction fractionOfPi;
    private int oarSidesDifference;

    /**
     *
     * @param fractionOfPi fraction qui multipliee par pi donne l'angle correspondant
     * @param oarSidesDifference difference de rames nécessaires : droite - gauche
     */
    public OarConfig(Fraction fractionOfPi, int oarSidesDifference) {
        this.fractionOfPi = fractionOfPi;
        this.oarSidesDifference = oarSidesDifference;
    }

    public double getAngle() {
        return Math.PI * fractionOfPi.eval();
    }

    public Fraction getAngleFraction(){
        return fractionOfPi;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof OarConfig))
            return false;
        OarConfig other = (OarConfig) obj;
        return fractionOfPi.equals(other.fractionOfPi) 
                && oarSidesDifference == other.oarSidesDifference;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fractionOfPi, oarSidesDifference);
    }
}
