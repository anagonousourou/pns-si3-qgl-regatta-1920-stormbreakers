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
     * creates a new line to add to the logs list if overflow spotted
     * @param str string to be saved
     */
    public void log(String str) {
        int spaceLeft = MAX_LINE_CHARS - currentLogLine.length();
        int strLenght = str.length();
        if (allLogs.size() < MAX_LOG_LINES) {
            if (strLenght > spaceLeft) {
                // push
                String keep = str.substring(0, spaceLeft);
                currentLogLine.append(keep);
                allLogs.add(currentLogLine.toString());

                clearCurrentLogLine();
                currentLogLine.append(str.substring(spaceLeft, strLenght));
            } else {
                currentLogLine.append(str);
            }
        }
    }

    private void clearCurrentLogLine(){
        currentLogLine = new StringBuilder();
    }

    public List<String> getSavedData() {
        if (currentLogLine.length() > 0 && allLogs.size() < MAX_LOG_LINES) {
            allLogs.add(currentLogLine.toString());
            clearCurrentLogLine();
        }
        return allLogs;
    }

    void reset() {
        clearCurrentLogLine();
        allLogs.clear();
    }
}