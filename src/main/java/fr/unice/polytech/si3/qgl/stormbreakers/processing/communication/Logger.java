package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static Logger singleton = new Logger();
    private List<String> messages = new ArrayList<>();

    public static Logger getInstance() {
        return singleton;
    }

    public void log(String msg) {
        if (msg.length() <= 200 && messages.size() < 100) {
            this.messages.add(msg);
        }

    }

    public List<String> getLogs() {
        return this.messages;
    }

}
