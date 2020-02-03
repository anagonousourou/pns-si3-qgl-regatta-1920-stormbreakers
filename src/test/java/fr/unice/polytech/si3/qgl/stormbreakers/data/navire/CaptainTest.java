package fr.unice.polytech.si3.qgl.stormbreakers.data.navire;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaptainTest{
    Captain rogers;
    Rame r1,r2,r3,r4,r5,r6;
    Marin m1,m2,m3,m4;
    @BeforeEach
    void setUp(){
        rogers=new Captain();
        r1=new Rame(0, 0);
        r2=new Rame(0, 2);
        r3=new Rame(2, 0);
        r4=new Rame(2, 2);
        r5=new Rame(4, 2);
        r6=new Rame(4, 2);
        m1=new Marin(0, 0, 1, "Jose Martin");
        m2=new Marin(1, 1, 0, "Gabriel Luu");
        m3=new Marin(2, 4, 0, "Sandra Spinney");
        m4=new Marin(3, 4, 2, "Deborah Carter");


        
    }

    @Test
    void marinsDisponiblesTest(){
        var result= this.rogers.marinsDisponibles(List.of(r1,r2,r3,r4,r5,r6), List.of(m1,m2,m3,m4));
        assertTrue(result.get(r1).containsAll(List.of(m1,m2,m3)));
        assertFalse(result.get(r1).contains(m4),"Marin 4 est trop loin");
        assertTrue(result.get(r4).containsAll(List.of(m1,m2,m3,m4)));
    }
}