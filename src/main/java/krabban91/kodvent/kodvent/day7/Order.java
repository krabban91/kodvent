package krabban91.kodvent.kodvent.day7;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private Character self;
    private Character blocker;
    public Order(String row){
        self = row.charAt(36);
        blocker = row.charAt(5);
    }

    public Character getSelf() {
        return self;
    }

    public Character getOther() {
        return blocker;
    }
}
