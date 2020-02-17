package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.List;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.ElementsConstructor;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.Logger;

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
