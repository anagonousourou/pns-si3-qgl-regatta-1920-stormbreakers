package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static Logger singleton = new Logger();
    private StringBuilder currentLogLine;
    private List<String> allLogs;

    private static final int MAX_LOG_LINES = 100;
    private static final int MAX_LINE_CHARS = 200;

    private Logger() {
        currentLogLine = new StringBuilder();
        allLogs = new ArrayList<>();
    }

    public static Logger getInstance() {
        return singleton;
    }

    /**
     * Saves string into current log line
     * 
     * @param str string to be saved
     */
    public void log(String str) {
        int spaceLeft = MAX_LINE_CHARS - currentLogLine.length();
        if (str.length() > spaceLeft) {
            str = str.substring(0, spaceLeft);
        }
        currentLogLine.append(str);
    }

    /**
     * Prepares for next log line
     */
    public void next() {
        if (allLogs.size() <= MAX_LOG_LINES)
            allLogs.add(currentLogLine.toString());
        clearCurrentLogLine();
    }

    private void clearCurrentLogLine() {
        currentLogLine = new StringBuilder();
    }

    public List<String> getSavedData() {
        return allLogs;
    }

    void reset() {
        clearCurrentLogLine();
        allLogs.clear();
    }

}
