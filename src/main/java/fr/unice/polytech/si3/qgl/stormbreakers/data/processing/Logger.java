package fr.unice.polytech.si3.qgl.stormbreakers.data.processing;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static Logger singleton = new Logger();
    private StringBuilder currentLogLine;
    private List<String> allLogs;

    private static final int MAX_LOG_LINES = 100;
    private static final int MAX_LINE_CHARS = 200;
    private static final String SEPARATOR = "$";

    private Logger() {
        currentLogLine = new StringBuilder();
        allLogs = new ArrayList<>();
    }

    public static Logger getInstance() {
        return singleton;
    }

    /**
     * Saves string into current log line creates a new line to add to the logs list
     * if overflow spotted
     * 
     * @param msg string to be saved
     */
    public void log(String msg) {
        if (msg == null)
            return;

        while (msg.length() > 0) {
            msg = addOnCurrentLine(msg);
            if (msg.length() != 0)
                push(); // if something remains we're on a new log line
        }
    }

    public void addSeparatorThenLog(String msg){
        addSeparator();
        log(msg);
    }

    public void logErrorMsg(Exception error){
        addSeparator();
        log(error.getMessage());
    }

    /**
     * Logs a predefined separator
     * 
     * @see Logger log
     */
    public void addSeparator() {
        log(SEPARATOR);
    }

    /**
     * Adds msg to the current log line uses substring if too long
     * 
     * @param msg string to log on current log line
     * @return String: the remaining part of the msg
     */
    private String addOnCurrentLine(String msg) {
        int msgLenght = msg.length();
        int spaceLeft = MAX_LINE_CHARS - currentLogLine.length();
        String remaining = "";

        if (msgLenght > spaceLeft) {
            String keep = msg.substring(0, spaceLeft);
            currentLogLine.append(keep);

            remaining = msg.substring(spaceLeft, msgLenght);
        } else {
            currentLogLine.append(msg);
        }

        return remaining;
    }

    /**
     * Stores last line as logs
     */
    private void push() {
        if (currentLogLine.length() == 0)
            return; // No need to push empty line
        allLogs.add(currentLogLine.toString());
        clearCurrentLogLine();
    }

    private void clearCurrentLogLine() {
        currentLogLine = new StringBuilder();
    }

    public List<String> getSavedData() {
        if (allLogs.size() < MAX_LOG_LINES)
            push();
        return allLogs;
    }

    void reset() {
        clearCurrentLogLine();
        allLogs.clear();
    }
}