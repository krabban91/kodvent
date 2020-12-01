package krabban91.kodvent.kodvent.y2015.d19;

import java.util.ArrayList;
import java.util.List;

public class Transition {
    private final String from;
    private final String to;

    public Transition(String transition) {
        String[] split = transition.split("=>");
        from = split[0].trim();
        to = split[1].trim();
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public List<String> potentialMolecules(String molecule) {
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < molecule.length(); i++) {
            builder.append(molecule.charAt(i));
            String s = builder.toString();
            if (s.contains(from) && s.lastIndexOf(from) + from.length() == s.length()) {
                list.add(s.substring(0, s.lastIndexOf(from)) + to + molecule.substring(i + 1));
            }
        }
        return list;
    }
}
