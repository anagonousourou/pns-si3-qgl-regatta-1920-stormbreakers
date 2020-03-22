package fr.unice.polytech.si3.qgl.stormbreakers.staff.reporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Circle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.IPoint;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Polygon;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Rectangle;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Recif;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.InputParser;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Point2D;

public class StreamManagerTest {

    private StreamManager manager;
    
    private InputParser parser = new InputParser();
    private Courant courant1=new Courant(new Position(500.0, 0.0,0.0), new Rectangle(300, 600, 0.0) , 40.0);
    private Courant courant2=new Courant(new Position(900.0, 900.0,-0.52), new Rectangle(300, 600, 0.0) , 80.0);
    private Courant courant3=new Courant(new Position(500.0, 0.0,0.0), new Rectangle(400, 600, 0.0) , 40.0);
    private Courant courant4=new Courant(new Position(500.0, 500.0,0.78539), new Rectangle(400, 400, 0.0) , 80.0);
    private Recif recifTriangle=new Recif(new Position(-200, 300.0,0), 
    new Polygon(
        0.0,List.of(new Point2D(100,100),new Point2D(0, -100),new Point2D(-100,100))
    ));

    private Recif recifCercle=new Recif(new Position(300, 600), 
    new Circle(200)
    );
    private Recif recifCarre=new Recif(
        new Position(500,200), 
        new Rectangle(200, 200,0)
    );

    private Courant courantCarre=new Courant(
        new Position(500,200), 
        new Rectangle(200, 200,0),
        100
    );



    Position pointA=new Position(200, 200);
    Position pointB=new Position(500, 400);
    Position pointC=new Position(800, 200);
    Position pointD=new Position(-300, 300);
    Position pointE=new Position(200, 900);
    Position pointF=new Position(-100, 600);
    Position pointG=new Position(-200, 700);
    Position pointH=new Position(200, 300);
    Position pointI=new Position(800, 800);

    

    @Test
    void insideStreamTest(){
        Boat boat=mock(Boat.class);
        List<Courant> courants=List.of(
            courant1,courant2
        );
        manager=new StreamManager(parser, boat);
        manager.setCourants(courants);

       
        when( boat.x() ).thenReturn(300.0);
        when( boat.y() ).thenReturn(300.0);
        when( boat.getOrientation() ).thenReturn(0.0);

        assertFalse(manager.insideStream());

        
        when( boat.x() ).thenReturn(300.0);
        when( boat.y() ).thenReturn(100.0);
        


        assertTrue(manager.insideStream());

        when( boat.x() ).thenReturn(500.0);
        when( boat.y() ).thenReturn(0.0);
        
        

        assertTrue(manager.insideStream());
    }

    @Test
    void streamAroundBoatTest(){
        Boat boat=mock(Boat.class);
        List<Courant> courants=List.of(
            courant1,courant2
        );
        manager=new StreamManager(parser, boat);
        manager.setCourants(courants);

        
        when( boat.x() ).thenReturn(300.0);
        when( boat.y() ).thenReturn(300.0);

        assertEquals(null,manager.streamAroundBoat());

        when( boat.x() ).thenReturn(800.0);
        when( boat.y() ).thenReturn(100.0);

        

        assertEquals(courant1,manager.streamAroundBoat());

        when( boat.x() ).thenReturn(500.0);
        when( boat.y() ).thenReturn(0.0);

        

        assertEquals(courant1,manager.streamAroundBoat());
        
    }

    @Test
    void thereIsStreamBetweenTest(){
        Boat boat=mock(Boat.class);
        List<Courant> courants=List.of(
            courant1,courant2
        );
        manager=new StreamManager(parser, boat);
        manager.setCourants(courants);

        when( boat.getPosition() ).thenReturn(new Position(0.0, 0.0));

        assertFalse(manager.thereIsStreamBetween(new Position(300, 300)));
        assertTrue(manager.thereIsStreamBetween(new Position(900, 100)));
        assertTrue(manager.thereIsStreamBetween(new Position(500, 100)));
        assertFalse(manager.thereIsStreamBetween(new Position(-300, 100)));


    }
    @Test
    void firstStreamBetweenTest(){

        Boat boat=mock(Boat.class);
        List<Courant> courants=List.of(
            courant1,courant2
        );
        manager=new StreamManager(parser, boat);
        manager.setCourants(courants);

        when( boat.x() ).thenReturn(700.0);
        when( boat.y() ).thenReturn(-300.0);

        

        assertEquals(courant1, manager.firstStreamBetween(Position.create(700, 1500)));

        assertEquals(null, manager.firstStreamBetween(Position.create(1800, 300)));

        when( boat.x() ).thenReturn(700.0);
        when( boat.y() ).thenReturn(1300.0);
        

        assertEquals(courant2, manager.firstStreamBetween(Position.create(700, 600)));

    }
    @Test
    void thereIsObstacleBetweenTest(){
         
        StreamManager streamManager=new StreamManager(parser, null);
        streamManager.setObstacles(List.of(recifCarre,recifCercle,recifTriangle));

        assertTrue(streamManager.thereIsObstacleBetween(pointA, pointC));
        //assertTrue(streamManager.thereIsObstacleBetween(pointA, pointE));
        assertTrue(streamManager.thereIsObstacleBetween(pointA, pointD));
        assertFalse(streamManager.thereIsObstacleBetween(pointA, pointB));

        
    }

    @Test
    void pointIsInsideStreamTest(){
        StreamManager streamManager=new StreamManager(parser, null);
        streamManager.setObstacles(List.of(recifCarre,recifCercle,recifTriangle));
        streamManager.setCourants(List.of(courantCarre));
        assertFalse(streamManager.pointIsInsideStream(pointA));
        assertFalse(streamManager.pointIsInsideStream(pointB));
        assertFalse(streamManager.pointIsInsideStream(pointC));
        assertFalse(streamManager.pointIsInsideStream(pointE));
        assertTrue(streamManager.pointIsInsideStream(new Position(500, 200) ));
        assertTrue(streamManager.pointIsInsideStream(new Position(510, 190) ));
    }

    @Test
    void streamAroundPointTest(){
        StreamManager streamManager=new StreamManager(parser, null);
        
        streamManager.setCourants(List.of(courant3,courant4));
        assertTrue(streamManager.streamAroundPoint(new Position(300, 300)).isEmpty() );

        var optc3=streamManager.streamAroundPoint(new Position(500, 100));
        assertTrue( optc3.isPresent());
        assertEquals(courant3, optc3.get(), "Le point est dans le courant 3");

        var optc4=streamManager.streamAroundPoint(new Position(500, 300));
        assertTrue( optc4.isPresent());
        assertEquals(courant4, optc4.get(), "Le point est dans le courant 4");
        
    }

    @Test
    void firstObstacleBetweenTest(){
        StreamManager streamManager=new StreamManager(parser, null);
        streamManager.setObstacles(List.of(recifCarre,recifCercle,recifTriangle));

        assertEquals(recifTriangle, streamManager.firstObstacleBetween(pointD, pointC));
        assertEquals(recifCarre, streamManager.firstObstacleBetween(pointC, pointD));
        assertEquals(recifCarre, streamManager.firstObstacleBetween(pointA, pointC));

        
    }

    @Test
    void trajectoryToAvoidObstaclesTest(){
        StreamManager streamManager=new StreamManager(parser, null);
        streamManager.setObstacles(List.of(recifCarre,recifCercle,recifTriangle));

       List<IPoint> trajectory= streamManager.trajectoryToAvoidObstacles(pointA, pointC);

       assertEquals(4, trajectory.size());
       assertEquals(pointA,trajectory.get(0) ,"Le premier élément de la trajectoire doit etre le depart");
       assertEquals(pointC,trajectory.get(trajectory.size()-1));
       trajectory=streamManager.trajectoryToAvoidObstacles(pointA, pointB);

       assertEquals(2, trajectory.size());
       assertEquals(pointA,trajectory.get(0) ,"Le premier élément de la trajectoire doit etre le depart");
       assertEquals(pointB,trajectory.get(1));

        //we set with streams to explore another branch of if/else
       streamManager.setObstacles(List.of(courant3,courant4,recifTriangle));

       trajectory=streamManager.trajectoryToAvoidObstacles(pointI, pointH);

       assertEquals(4, trajectory.size());

       trajectory=streamManager.trajectoryToAvoidObstacles(pointH, pointI);
       assertEquals(2, trajectory.size());
       
       

    }

    @Test
    void trajectoryLeaveStreamAndReachPointTest(){
       StreamManager streamManager=new StreamManager(parser, null);
        
       streamManager.setCourants(List.of(courant3,courant4));

       var trajectory= streamManager.trajectoryLeaveStreamAndReachPoint(new Position(600, 600), pointH);

       
       assertEquals(3, trajectory.size());

       assertFalse( streamManager.pointIsInsideStream(trajectory.get(1)) );

       trajectory= streamManager.trajectoryLeaveStreamAndReachPoint(new Position(600, 600), pointI);

       assertEquals(2, trajectory.size());
    }

    @Test
    void trajectoryBoatAndCheckpointInsideStreamTest(){
        StreamManager streamManager=new StreamManager(parser, null);
        
        streamManager.setCourants(List.of(courant3,courant4));

        var trajectory=streamManager.trajectoryBoatAndCheckpointInsideStream(new Position(400,400), new Position(600,600));

        assertEquals(2,trajectory.size());

    }

    @Test
    public void speedProvidedLimitsTest(){

        StreamManager streamManager=new StreamManager(parser, null);
        
        streamManager.setCourants(List.of(courant3,courant4));

        assertEquals(0.0, streamManager.speedProvidedLimits(new Point2D(300,200),new Point2D(350, 200)), 1e-3);

        assertTrue(streamManager.speedProvidedLimits(new Point2D(300,200),new Point2D(350, 100)) > 1e-3);

    }
}