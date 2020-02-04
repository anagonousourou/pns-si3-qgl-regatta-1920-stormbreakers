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
        // Msg truncated if logger 'full'
        if (messages.size() < 100) {
            // Msg truncated if too long
            this.messages.add( (msg.length() <= 200)? msg: msg.substring(0,200));
        }

    }

    public List<String> getLogs() {
        return this.messages;
    }

    public void reset() {
        messages.clear();
    }

}
