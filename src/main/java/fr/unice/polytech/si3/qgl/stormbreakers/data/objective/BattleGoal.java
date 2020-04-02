package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

public class BattleGoal extends Goal {

    BattleGoal() {
        super(BattleType.BATTLE.key);
    }

    @Override
    public String toLogs() {
        return getMode();
    }
}
