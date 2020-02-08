package fr.unice.polytech.si3.qgl.stormbreakers;

import java.util.List;
import java.util.stream.Collectors;

public interface Logable {
    String toLogs();

    static String listToLogs(List<Logable> logables, String delimiter, String prefix, String suffix) {
        return logables.stream()
                .map((Logable s)->(s.toLogs()))
                .collect(Collectors.joining(delimiter, prefix, suffix));
    }

}
