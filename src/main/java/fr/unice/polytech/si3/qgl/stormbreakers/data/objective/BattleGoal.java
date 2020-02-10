package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

public class BattleGoal extends Goal {

    BattleGoal() {
        super("BATTLE");
    }

    @Override
    public String toLogs() {
        return getMode();
    }
}
