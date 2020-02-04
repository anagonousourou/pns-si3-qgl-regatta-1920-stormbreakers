package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaptainTest {
    private Captain rogers;
    private Rame r1, r2, r3, r4, r5, r6;
    private Marin m1, m2, m3, m4;

    @BeforeEach
    void setUp() {
        rogers = new Captain();
        r1 = new Rame(0, 0);
        r2 = new Rame(0, 2);
        r3 = new Rame(2, 0);
        r4 = new Rame(2, 2);
        r5 = new Rame(4, 0);
        r6 = new Rame(4, 2);
        m1 = new Marin(0, 0, 1, "Jose Martin");
        m2 = new Marin(1, 1, 0, "Gabriel Luu");
        m3 = new Marin(2, 4, 0, "Sandra Spinney");
        m4 = new Marin(3, 4, 2, "Deborah Carter");

    }

    @Test
    void marinsDisponiblesTest() {
        var result = this.rogers.marinsDisponibles(List.of(r1, r2, r3, r4, r5, r6), List.of(m1, m2, m3, m4));
        assertTrue(result.get(r1).containsAll(List.of(m1, m2, m3)));
        assertFalse(result.get(r1).contains(m4), "Marin 4 est trop loin");
        assertTrue(result.get(r4).containsAll(List.of(m1, m2, m3, m4)));
    }

    @Test
    void ramesAccessiblesTest() {

        var result = this.rogers.ramesAccessibles(List.of(m1, m2, m3, m4), List.of(r1, r2, r3, r4, r5, r6));
        assertNotNull(result, "Pas null");
        assertTrue(result.get(m1).containsAll(List.of(r1, r2, r3, r4, r5, r6)));

    }

    @Test
    void activateNbOarsTest() {
        var actions = rogers.activateNbOars(List.of(r1, r3, r5), 3, new ArrayList<>(), List.of(m1, m2, m3, m4));

        assertEquals(3 * 2, actions.size());
        actions = rogers.activateNbOars(List.of(r1, r3, r5), 3, new ArrayList<>(), List.of(m1, m2));
        assertEquals(2 * 2, actions.size(), "Si il n'y a pas assez de marins il fait avec ce qu'il a");

        actions = rogers.activateNbOars(List.of(r1, r3, r5), 3, new ArrayList<>(List.of(m3, m4)),
                List.of(m1, m2, m3, m4));

        var idsUsed = actions.stream().map(a -> a.getSailorId()).collect(Collectors.toList());

        assertFalse(idsUsed.contains(m3.getId()), "N'utilise pas les marins dits indisponibles");
        assertFalse(idsUsed.contains(m4.getId()), "N'utilise pas les marins dits indisponibles");

    }
}