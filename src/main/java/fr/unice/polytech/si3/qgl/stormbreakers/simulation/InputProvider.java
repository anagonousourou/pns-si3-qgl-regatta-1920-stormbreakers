package fr.unice.polytech.si3.qgl.stormbreakers.simulation;

import java.io.IOException;

public interface InputProvider {

    String provideInit() throws IOException;
    String provideFirstRound() throws IOException;
}
