package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

public class Calculator {
    double nextX(double x,double vitesse,int step,double orientation,double rotation,int nbstep){
        if(step==0){
            return x;
        }
        else{
            return nextX(x +(vitesse/nbstep)*Math.cos(orientation), vitesse, step-1, orientation+rotation/nbstep, rotation,nbstep);
        }
    }

    double nextY(double y,double vitesse,int step,double orientation,double rotation,int nbstep){
        if(step==0){
            return y;
        }
        else{
            return nextY(y +(vitesse/nbstep)*Math.sin(orientation), vitesse, step-1, orientation+rotation/nbstep, rotation,nbstep);
        }
    }
}