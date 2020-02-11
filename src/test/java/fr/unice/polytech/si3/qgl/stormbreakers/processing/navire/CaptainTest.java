package fr.unice.polytech.si3.qgl.stormbreakers.processing.navire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Equipment;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Oar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.MoveAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.SailorAction;
import fr.unice.polytech.si3.qgl.stormbreakers.data.actions.Turn;

class CaptainTest {
    private Captain rogers;
    private Oar r1, r2, r3, r4, r5, r6;
    private Marin m1, m2, m3, m4;

    @BeforeEach
    void setUp() {
        rogers = new Captain();
        r1 = new Oar(0, 0);
        r2 = new Oar(0, 2);
        r3 = new Oar(2, 0);
        r4 = new Oar(2, 2);
        r5 = new Oar(4, 0);
        r6 = new Oar(4, 2);
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

    @Test
    void toActivateTest() {
		int widthship = 2;
		List<Equipment> leftOars;
		List<Equipment> rightOars;
		List<Equipment> oars=new ArrayList<>();
		oars.addAll(List.of(r1,r2,r3,r4));
		if (widthship % 2 == 1) {// impair
			leftOars = oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
			rightOars = oars.stream().filter(oar -> oar.getY() > widthship / 2).collect(Collectors.toList());
		} else {
			leftOars = oars.stream().filter(oar -> oar.getY() < widthship / 2).collect(Collectors.toList());
			rightOars = oars.stream().filter(oar -> oar.getY() >= widthship / 2).collect(Collectors.toList());
		}
		int rameGauche=0;
		int rameDroite=0;
		
		List<Marin> allsailors= new ArrayList<>();
		allsailors.addAll(List.of(m1,m2,m3,m4));
    	var listAction= rogers.toActivate(leftOars, rightOars, new ArrayList<Marin>(), allsailors);
		var usedSailors= new ArrayList<Marin>();
    	List<SailorAction> listActionDroite=rogers.activateNbOars(rightOars, 4, usedSailors, allsailors);
    	List<SailorAction> listActionGauche=rogers.activateNbOars(rightOars, 4, usedSailors, allsailors);
    	for(SailorAction actionLeft:listActionGauche) {
    		if(listAction.contains(actionLeft)) {
    			rameGauche++;
    		}
    	}
    	for(SailorAction actionRight:listActionDroite) {
    		if(listAction.contains(actionRight)) {
    			rameDroite++;
    		}
    	}
    	assertEquals(rameDroite, rameGauche);
    }
    
    @Test
    void activateRudderTest() {
    	List<SailorAction> toTest1 = rogers.activateRudder(new ArrayList<Marin>(), List.of(m1, m2, m3, m4), new Gouvernail(0, 1), Math.PI/5);
    	assertEquals(0, toTest1.get(0).getSailorId());
    	assertEquals(0, toTest1.get(1).getSailorId());
    	assertTrue(toTest1.get(0) instanceof MoveAction);
    	assertTrue(toTest1.get(1) instanceof Turn);
    	assertEquals(Math.PI/5, ((Turn) toTest1.get(1)).getRotation());
    	
    	List<SailorAction> toTest2 = rogers.activateRudder(new ArrayList<Marin>(), List.of(m1, m2), new Gouvernail(0, 1), -Math.PI/2);
    	assertEquals(-Math.PI/4, ((Turn) toTest2.get(1)).getRotation());
    	
    	List<SailorAction> toTest3 = rogers.activateRudder(new ArrayList<Marin>(), List.of(m1), new Gouvernail(0, 1), Math.PI/2);
    	assertEquals(Math.PI/4, ((Turn) toTest3.get(1)).getRotation());
    }

    @Test
    void marinsProcheGouvernailTest() {
    	List<Marin> toTest1 = rogers.marinsProcheGouvernail(new Gouvernail(4, 5), List.of(m1, m2, m3, m4));
    	assertTrue(toTest1.contains(m3));
    	assertTrue(toTest1.contains(m4));
    	assertEquals(2, toTest1.size());
    	List<Marin> toTest2 = rogers.marinsProcheGouvernail(new Gouvernail(8, 9), List.of(m1, m2, m3, m4));
    	assertTrue(toTest2.isEmpty());
    	
    }
}