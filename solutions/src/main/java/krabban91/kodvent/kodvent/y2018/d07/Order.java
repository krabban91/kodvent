package krabban91.kodvent.kodvent.y2018.d07;

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
