package fr.unice.polytech.si3.qgl.stormbreakers.tools.runner;

import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.math.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Rudder;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Stream;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.*;
import fr.unice.polytech.si3.qgl.stormbreakers.math.LineSegment2D;
import fr.unice.polytech.si3.qgl.stormbreakers.math.Surface;
import fr.unice.polytech.si3.qgl.stormbreakers.tools.visuals.draw.Displayer;

import java.util.List;

public class EngineUtils {

    private EngineUtils() {}
    
    public static double windAdditionnalSpeed(int nbsail, int nbSailOpenned, Wind wind, double shipOrientation){
        if(nbsail==0 || wind==null)return 0;
        double linearWindSpeed;
        double angleBetweenWindDirectionAndBoatDirection;
        angleBetweenWindDirectionAndBoatDirection = (wind.getOrientation()>0) ? wind.getOrientation() - shipOrientation : shipOrientation-wind.getOrientation();
        linearWindSpeed = (double) nbSailOpenned/nbsail * wind.getStrength() * Math.cos(angleBetweenWindDirectionAndBoatDirection);
        return linearWindSpeed;
    }

    public static double oarSpeed(int nbOars, int nbOarsLeftActive, int nbOarsRightActive){
        return 165.0*(nbOarsRightActive+nbOarsLeftActive)/nbOars;
    }

    public static double streamSpeedX(List<Stream> streams, Boat ship){
        if(streams==null)return 0;
        double res = 0;
        for(Stream stream1 : streams) {
            if (ship.collidesWith(stream1)) {
                res +=stream1.getStrength()*Math.cos(stream1.getPosition().getOrientation());
            }
        }
        return res;
    }

    public static double streamSpeedY(List<Stream>streams, Boat ship){
        if(streams==null)return 0;
        double res = 0;
        for(Stream stream1 : streams) {
            if (ship.collidesWith(stream1)) {
                res += stream1.getStrength() * Math.sin(stream1.getPosition().getOrientation());
            }
        }
        return res;
    }

    public static Position nextPosition(Position positionInit, int nbOarsRightActive, int nbOarsLeftActive, int nbOars,
                                        Rudder rudder, Wind wind, List<Stream> streams, int nbsail, int nbSailOpenned,
                                        Shape shipShape, List<Reef> reef, int nbStep, Displayer displayer){
        double x = positionInit.x();
        double y = positionInit.y();
        double orientation = positionInit.getOrientation();

        if(rudder ==null) rudder = new Rudder(-1,-1);
        double angleGouvernail = rudder.getOrientation();
        double vitesseOarLineaire = oarSpeed(nbOars,nbOarsLeftActive,nbOarsRightActive);
        double vitesseWindLineaire = windAdditionnalSpeed(nbsail,nbSailOpenned,wind,orientation);
        double vitesseLineaire = vitesseOarLineaire + vitesseWindLineaire;
        double vitesseOrientation = Math.PI*(nbOarsRightActive-nbOarsLeftActive)/nbOars + angleGouvernail;

        for(int i = 0;i<nbStep;i++){
            if(displayer!=null) displayer.addDot(new Position(x,y,orientation), Displayer.SPECIAL_COLOR);
            Position positionAtStep = new Position(x,y,orientation);
            Boat shipAtState = new Boat(positionAtStep,0,0,0,null,shipShape);
            x=x+ Math.cos(orientation)*vitesseLineaire/nbStep + streamSpeedX(streams,shipAtState)/nbStep;
            y=y+ Math.sin(orientation)*vitesseLineaire/nbStep + streamSpeedY(streams,shipAtState)/nbStep;
            orientation = orientation + vitesseOrientation/nbStep;
            vitesseLineaire = vitesseOarLineaire + windAdditionnalSpeed(nbsail,nbSailOpenned,wind,orientation);
            for(OceanEntity reef1 : reef) {
                Position currentNextPos = new Position(x, y, orientation);
                Boat newBoat = (new Boat(currentNextPos, 0,0,0,null, shipShape));
                if (newBoat.collidesWith(reef1) || collisionBetweenSteps(positionAtStep,currentNextPos,reef1) ) {
                    System.err.println("Collision near "+ currentNextPos +" after leaving "+positionInit);
                    return positionAtStep;
                }
            }
        }
        orientation = orientation - (Math.ceil((orientation + Math.PI)/(2*Math.PI))-1)*2*Math.PI;
        return new Position(x,y, orientation);
    }

    private static boolean collisionBetweenSteps(Position posBefore, Position posAfter, Surface obstacle) {
        return posBefore.distanceTo(posAfter)!=0
                && obstacle.getShape().collidesWith(new LineSegment2D(posBefore,posAfter));
    }
}
