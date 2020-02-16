package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.List;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.ElementsConstructor;

public class Cockpit implements ICockpit {
	
	private ElementsConstructor elementsConstructor;
	public void initGame(String game) {

		this.elementsConstructor=new ElementsConstructor(game);
	}

	public String nextRound(String round) {
		return this.elementsConstructor.sendActions(round);
		
	}

	@Override
	public List<String> getLogs() {
		return Logger.getInstance().getSavedData();
	}

}
