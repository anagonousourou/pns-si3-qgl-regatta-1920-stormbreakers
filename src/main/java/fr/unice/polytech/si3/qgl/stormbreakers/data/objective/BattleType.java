package fr.unice.polytech.si3.qgl.stormbreakers.data.objective;

public enum BattleType {
    REGATTA("REGATTA"), BATTLE("BATTLE");

    BattleType(String key) {this.key = key;}

    public final String key;
}