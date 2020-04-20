package fr.unice.polytech.si3.qgl.stormbreakers.staff.tactical;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Fraction;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter.OarsConfig;

class NavigatorTest {

    private Navigator navigator;
    private OarsConfig noOars;

    @BeforeEach
    void setUp(){
        navigator = new Navigator();
        noOars = new OarsConfig(new Fraction(),0);
    }

    /*
     * Tests for orientationNeeded
     */

    @Test
    void testOrientationNeededWhenBoatOnTarget() {
        //bateau et cible confondus
        assertEquals(0,navigator.additionalOrientationNeeded(new Position(8,8,Double.NaN),new Point2D(8,8)) );
    }

    @Test
    void testOrientationNeededWhenTargetInFront() {
        //cible pile enface du bateau
        assertEquals(0,navigator.additionalOrientationNeeded(new Position(2,1,Math.PI/4),new Point2D(4,3)) );
        //cible a droite du bateau
        assertEquals(-Math.PI/2,navigator.additionalOrientationNeeded(new Position(0,0,0),new Point2D(0,-1)) );
        //cible a gauche de pi/4 par rapport au bateau
        assertEquals(Math.PI/2,navigator.additionalOrientationNeeded(new Position(2,1,0),new Point2D(2,2)) );


        //checkpoint a 2,1 bateau 1,1 orienté pi/2
        assertEquals(-Math.PI/2,navigator.additionalOrientationNeeded(new Position(1,1,Math.PI/2),new Point2D(2,1)) );
        //checkpoint a 0,-4  bateau -2,-2 orienté pi/2
        assertEquals(-Math.PI/4,navigator.additionalOrientationNeeded(new Position(-2,-2,Math.PI/2),new Point2D(0,0)) );
    }

    @Test
    void testOrientationNeededWhenTargetBehindBoat() {
        // Defaults to left when completely behind
        assertEquals(0.75 * Math.PI,navigator.additionalOrientationNeeded(new Position(1,1,Math.PI/2),new Point2D(1,-2)) );
        // When behind to the left
        assertEquals(0.75 * Math.PI,navigator.additionalOrientationNeeded(new Position(1,1,Math.PI/2),new Point2D(-1,-1)) );
        // When behind to the left but unreachable
        assertEquals(0.75 * Math.PI,navigator.additionalOrientationNeeded(new Position(1,1,Math.PI/2),new Point2D(0,-2)) );
        // When behind to the right
        assertEquals(-0.75 * Math.PI,navigator.additionalOrientationNeeded(new Position(1,1,Math.PI/2),new Point2D(3,-1)) );
        // When behind to the right but unreachable
        assertEquals(-0.75 * Math.PI,navigator.additionalOrientationNeeded(new Position(1,1,Math.PI/2),new Point2D(2,-2)) );
    }

    /*
     * Tests for possibleOarConfigs
     */

    @Test
    void testPossibleOarConfigsHasAtLeastNoOars() {
        Set<OarsConfig> oarsConfigs = navigator.possibleOarConfigs(1,1);
        assertTrue(oarsConfigs.contains(noOars));
    }

    @Test
    void testPossibleOarConfigsHasNoMoreElementsThanNeeded() {
        int nbLeftOars = 10;
        int nbRightOars = 10;
        Set<OarsConfig> oarsConfigs = navigator.possibleOarConfigs(nbLeftOars,nbRightOars);
        int totalOars = nbLeftOars + nbRightOars;
        assertTrue(oarsConfigs.size() <= totalOars + 1); // Don't forget case noOars
    }

    @Test
    void testPossibleOarConfigsAnglesAreInBoundaries() {
        Set<OarsConfig> oarsConfigs = navigator.possibleOarConfigs(5,5);
        oarsConfigs.forEach( (OarsConfig config) ->
                assertTrue(Math.abs(config.getAngle()) <= Math.PI/2)
        );
    }

    @Test
    void testPossibleOarConfigsWhen() {
        Set<OarsConfig> oarsConfigs = navigator.possibleOarConfigs(5,5);
        oarsConfigs.forEach( (OarsConfig config) ->
                assertTrue(Math.abs(config.getAngle()) <= Math.PI/2)
        );
    }

    @Test
    void fromAngleToDiffEquilibre(){
        assertEquals(1, navigator.fromAngleToDiff(0.65, 2, 2),"il faut 1 personne de plus a gauche qu'a droite");
        assertEquals(0, navigator.fromAngleToDiff(0.0, 2, 2),"il faut 1 personne de plus a gauche qu'a droite");
    }

}