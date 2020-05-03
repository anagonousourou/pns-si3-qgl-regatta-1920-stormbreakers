package fr.unice.polytech.si3.qgl.stormbreakers.data.ocean;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;



import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;

public class ReefTest {


    private Reef reef1=new Reef(
        new Position(1137.3333333333333,1329.3333333333333), 
    new Polygon(0.0, List.of(
        new Point2D(-93.33333333333326, 2.6666666666667425),
        new Point2D(62.66666666666674, -129.33333333333326),
        new Point2D(30.666666666666742, 126.66666666666674)
    )));

    private Position pos=new Position(1174.2952148887728,1462.503813698916);

    @org.junit.jupiter.api.Test
    void isInsideTest(){
       assertTrue(reef1.getShape().wrappingShape(7.5).isPtInside(pos));
       assertTrue(reef1.isInsideWrappingSurface(8, pos) );

    }
    

}