package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

import fr.unice.polytech.si3.qgl.stormbreakers.Logable;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static Logger singleton = new Logger();
    private StringBuilder currentLine;
    private List<String> messages;

    private Logger() {
        currentLine = new StringBuilder();
        messages = new ArrayList<>();
    }

    public static Logger getInstance() {
        return singleton;
    }

    public void logLine(String line) {
        // Msg ignored if logger 'full'
        if (messages.size() < 100) {
            // Msg truncated if too long
            this.messages.add( (line.length() <= 200)? line: line.substring(0,200));
        }

    }

    public void log(String data) {
        currentLine.append(data);
    }

    public void nextLine() {
        // On log la ligne courante
        logLine(currentLine.toString());
        // On reset la ligne courante
        currentLine = new StringBuilder();
    }

    public List<String> getLogs() {
        return this.messages;
    }

    public void reset() {
        messages.clear();
    }

}
