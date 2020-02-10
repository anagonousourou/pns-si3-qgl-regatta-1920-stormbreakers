package fr.unice.polytech.si3.qgl.stormbreakers.processing.navire;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NavigatorTest {

    private Navigator navigator;

    @BeforeEach
    void setUp(){
        navigator = new Navigator();
    }

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
}