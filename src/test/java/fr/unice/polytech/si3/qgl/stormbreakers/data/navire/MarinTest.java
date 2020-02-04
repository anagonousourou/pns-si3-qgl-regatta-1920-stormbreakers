package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarinTest {

    /*
     * Tests for equals
     */

    @Test
    void testEqualsWhenWrongObject() {
        Marin marin = new Marin(42,0,0,"Capt Haddock");
        Integer other = 0;
        assertNotEquals(marin,other);
    }

    @Test void testEqualsWhenNullObject() {
        Marin marin = new Marin(42,0,0,"Capt Haddock");
        Marin other = null;
        assertNotEquals(marin,other);
    }

    @Test void testEqualsWhenSameObject() {
        Marin marin1 = new Marin(42,0,0,"Capt Haddock");
        assertEquals(marin1,marin1);
    }

    @Test void testEqualsWhenSameValues() {
        Marin marin1 = new Marin(42,0,0,"Capt Haddock");
        Marin marin2 = new Marin(42,0,0,"Capt Haddock");
        assertEquals(marin1,marin2);
    }

    @Test void testEqualsWhenSimilar() {
        Marin marin1 = new Marin(42,0,0,"Capt Haddock");
        Marin marin2 = new Marin(42,10,10,"Capt Haddock");
        assertEquals(marin1,marin2);
    }

    @Test void testEqualsWhenDifferent() {
        Marin marin1 = new Marin(42,0,0,"Capt Haddock");
        Marin marin2 =  new Marin(17,0,0,"Jack Sparrow");
        assertNotEquals(marin1,marin2);
    }

    /*
     * End of tests for equals
     */

}