package fr.unice.polytech.si3.qgl.stormbreakers.runner;

import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Position;
import fr.unice.polytech.si3.qgl.stormbreakers.data.metrics.Shape;
import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Gouvernail;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Boat;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Courant;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.OceanEntity;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Wind;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.Displayer;
import fr.unice.polytech.si3.qgl.stormbreakers.visuals.draw.drawings.DotDrawing;

import java.awt.*;
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

    public static double streamSpeedX(List<Courant> streams, Boat ship){
        if(streams==null)return 0;
        double res = 0;
        for(Courant stream1 : streams) {
            if (ship.collidesWith(stream1)) {
                res +=stream1.getStrength()*Math.cos(stream1.getPosition().getOrientation());
            }
        }
        return res;
    }

    public static double streamSpeedY(List<Courant>streams, Boat ship){
        if(streams==null)return 0;
        double res = 0;
        for(Courant stream1 : streams) {
            if (ship.collidesWith(stream1)) {
                res += stream1.getStrength() * Math.sin(stream1.getPosition().getOrientation());
            }
        }
        return res;
    }

    public static Position nextPosition(Position positionInit, int nbOarsRightActive, int nbOarsLeftActive, int nbOars,
                                        Gouvernail rudder, Wind wind, List<Courant> streams, int nbsail, int nbSailOpenned,
                                        Shape shipShape, List<OceanEntity> reef, int nbStep, Displayer displayer){
        double x = positionInit.x();
        double y = positionInit.y();
        double orientation = positionInit.getOrientation();
        if(rudder ==null) rudder = new Gouvernail(-1,-1);
        double angleGouvernail = rudder.getOrientation();
        double vitesseOarLineaire = oarSpeed(nbOars,nbOarsLeftActive,nbOarsRightActive);
        double vitesseWindLineaire = windAdditionnalSpeed(nbsail,nbSailOpenned,wind,orientation);
        double vitesseLineaire = vitesseOarLineaire + vitesseWindLineaire;
        double vitesseOrientation = Math.PI*(nbOarsRightActive-nbOarsLeftActive)/nbOars + angleGouvernail;
        for(int i = 0;i<nbStep;i++){
            if(displayer!=null) displayer.addDrawing(new DotDrawing(new Position(x,y,orientation), Color.GRAY));
            Position positionAtStep = new Position(x,y,orientation);
            Boat shipAtState = new Boat(positionAtStep,0,0,0,null,shipShape);
            // TODO: 02/04/2020 Join speedX and speedY using vectors
            x=x+ Math.cos(orientation)*vitesseLineaire/nbStep + streamSpeedX(streams,shipAtState)/nbStep;
            y=y+ Math.sin(orientation)*vitesseLineaire/nbStep + streamSpeedY(streams,shipAtState)/nbStep;
            orientation = orientation + vitesseOrientation/nbStep;
            vitesseLineaire = vitesseOarLineaire + windAdditionnalSpeed(nbsail,nbSailOpenned,wind,orientation);
            for(OceanEntity reef1 : reef) {
                Boat newBoat = (new Boat(new Position(x, y, orientation), 0,0,0,null, shipShape));
                if (newBoat.collidesWith(reef1)) {
                    System.out.println("Collision !");
                    return positionAtStep;
                }
            }
        }
        orientation = orientation - (Math.ceil((orientation + Math.PI)/(2*Math.PI))-1)*2*Math.PI;
        return new Position(x,y, orientation);
    }
}
