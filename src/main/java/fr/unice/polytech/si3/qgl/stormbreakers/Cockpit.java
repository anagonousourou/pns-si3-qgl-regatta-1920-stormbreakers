package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.List;

import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.stormbreakers.data.processing.MainLinker;
import fr.unice.polytech.si3.qgl.stormbreakers.io.Logger;

public class Cockpit implements ICockpit {

	private MainLinker elementsConstructor;

	public void initGame(String game) {
		Logger.getInstance().addSeparatorThenLog("INIT");
		Logger.getInstance().addSeparator();
		this.elementsConstructor = new MainLinker(game);
	}

	public String nextRound(String round) {
		Logger.getInstance().addSeparatorThenLog("N_RND");
		Logger.getInstance().addSeparator();
		return this.elementsConstructor.sendActions(round);
	}

	@Override
	public List<String> getLogs() {
		return Logger.getInstance().getSavedData();
	}

}
