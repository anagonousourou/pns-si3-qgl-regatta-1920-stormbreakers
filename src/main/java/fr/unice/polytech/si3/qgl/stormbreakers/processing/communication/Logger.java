package fr.unice.polytech.si3.qgl.stormbreakers.processing.communication;

public class Logger {
    private StringBuilder sb;
    private static Logger singleton = new Logger();

    private Logger() {
        reset();
    }

    static Logger getInstance() {
        return singleton;
    }

    void reset() {
        sb = new StringBuilder();
    }

    void log(String msg) {
        sb.append(msg);
    }

    String getLogs() {
        return sb.toString();
    }


}
