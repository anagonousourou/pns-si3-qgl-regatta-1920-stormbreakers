package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.List;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.processing.communication.Logger;
import fr.unice.polytech.si3.qgl.stormbreakers.refactoring.Control;

public class Cockpit implements ICockpit {
	
	private Control control;
	public void initGame(String game) {

		this.control=new Control(game);
	}

	public String nextRound(String round) {
		return this.control.sendActions(round);
		
	}

	@Override
	public List<String> getLogs() {
		return Logger.getInstance().getSavedData();
	}

}
