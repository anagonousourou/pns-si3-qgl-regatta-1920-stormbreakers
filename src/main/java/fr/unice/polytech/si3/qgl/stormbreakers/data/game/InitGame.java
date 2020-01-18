package fr.unice.polytech.si3.qgl.stormbreakers.data.game;

import fr.unice.polytech.si3.qgl.stormbreakers.data.navire.Marin;
import fr.unice.polytech.si3.qgl.stormbreakers.data.objective.Goal;
import fr.unice.polytech.si3.qgl.stormbreakers.data.ocean.Bateau;

import java.util.ArrayList;
import java.util.List;

public class InitGame {
    Goal goal;
    Bateau ship;
    List<Marin> sailors;
    int shipCount;
    
    public InitGame(Goal goal,Bateau ship,List<Marin> sailors,int shipCount) {
    	this.goal=goal;
    	this.ship=ship;
    	this.sailors=sailors;
    	this.shipCount=shipCount;
    }
}
