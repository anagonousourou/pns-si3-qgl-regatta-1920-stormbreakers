package fr.unice.polytech.si3.qgl.stormbreakers.math;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UtilsTest {
	
	Double d0, d1, d2, d3, d4, d5, d6, d7;
	int n;
	
    @BeforeEach
    void setUp(){
    	d0 = 0.1758325446;
    	d1 = 0.17583254451;
    	d2 = 0.1758;
    	d3 = 0.17575;
    	d4 = 0.176;
    	d5 = 0.18;
    	d6 = 0.2;
    	d7 = 0.3;
    	n = 1;
    }
    
    @Test
    void almostEqualsDoublestest() {
    	//Basic tests
    	assertTrue(Utils.almostEquals(d0, d1));
    	assertFalse(Utils.almostEquals(-d0, d1));
    	assertFalse(Utils.almostEquals(d1, d2));
    	assertFalse(Utils.almostEquals(d4, d3));
    	assertFalse(Utils.almostEquals(d5, d4));
    	assertFalse(Utils.almostEquals(d6, n));
    	
    	//Strict test
    	assertFalse(Utils.almostEquals(d0*10, d1*10));
    }
    
    @Test
    void almostEqualsDoublesEpstest() {
    	//Basic tests
    	assertTrue(Utils.almostEquals(d4, d5, 0.01));
    	assertTrue(Utils.almostEquals(d2, d6, 0.1));
    	assertTrue(Utils.almostEquals(d2, d0, 0.0001));
    	assertFalse(Utils.almostEquals(d1, d3, 0.00001));
    	assertFalse(Utils.almostEquals(n, d6, 0.1));
    	assertFalse(Utils.almostEquals(-d2, d6, 0.1));
    	
    	//Strict test
    	assertFalse(Utils.almostEquals(d6, d7, 0.1));
    }

    @Test
    void almostEqualsBoundsIncludedEpsilonTest() {
    	//Basic tests
    	assertTrue(Utils.almostEqualsBoundsIncluded(d4, d5, 0.01));
    	assertTrue(Utils.almostEqualsBoundsIncluded(d2, d6, 0.1));
    	assertTrue(Utils.almostEqualsBoundsIncluded(d2, d0, 0.0001));
    	assertFalse(Utils.almostEqualsBoundsIncluded(d1, d3, 0.00001));
    	assertFalse(Utils.almostEqualsBoundsIncluded(n, d6, 0.1));
    	assertFalse(Utils.almostEqualsBoundsIncluded(-d2, d6, 0.1));
    	
    	//Strict test
    	assertTrue(Utils.almostEqualsBoundsIncluded(d6, d7, 0.1));
    }
    
    @Test
    void almostEqualsBoundsIncludedTest() {
    	//Basic tests
    	assertTrue(Utils.almostEqualsBoundsIncluded(d0, d1));
    	assertFalse(Utils.almostEqualsBoundsIncluded(-d0, d1));
    	assertFalse(Utils.almostEqualsBoundsIncluded(d1, d2));
    	assertFalse(Utils.almostEqualsBoundsIncluded(d4, d3));
    	assertFalse(Utils.almostEqualsBoundsIncluded(d5, d4));
    	assertFalse(Utils.almostEqualsBoundsIncluded(d6, n));
    	
    	//Strict test
    	assertFalse(Utils.almostEqualsBoundsIncluded(d0*10, d1*10));
    }
    
    @Test
    void almostEqualsPoints2DTest() {
    	Point2D p1 = new Point2D(d1/10, d1/10);
    	Point2D p2 = new Point2D(d0/10, d0/10);
    	
    	Point2D p3 = new Point2D(d1,  d1);
    	Point2D p4 = new Point2D(d0,  d0);
    	
    	Point2D p5 = new Point2D(-d1,  -d1);
    	Point2D p6 = new Point2D(d0,  d0);
    	
    	assertTrue(Utils.almostEquals(p1, p2));
    	assertFalse(Utils.almostEquals(p3, p4));
    	assertFalse(Utils.almostEquals(p5, p6));
    }
    
    @Test
    void withinTest() {
    	//Basic tests
    	assertTrue(Utils.within(d1, d0));
    	assertTrue(Utils.within(-d1, d0));
    	assertTrue(Utils.within(d5, d6));
    	assertFalse(Utils.within(d1, -d0));
    	assertFalse(Utils.within(d2, d3));
    	assertFalse(Utils.within(n, d7));
    	
    	//Strict test
    	assertTrue(Utils.within(d6 + 0.0999, d7));
    }
}
