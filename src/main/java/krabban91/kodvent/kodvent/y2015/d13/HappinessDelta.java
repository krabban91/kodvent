package krabban91.kodvent.kodvent.y2015.d13;

public class HappinessDelta {
    private final int change;
    private final String person;
    private final String nextTo;

    public HappinessDelta(int change, String person, String nextTo) {
        this.change = change;
        this.person = person;
        this.nextTo = nextTo;
    }

    public HappinessDelta(String input) {
        this(
                Integer.parseInt(input.split(" ")[3]) * (input.split(" ")[2].equals("gain") ? 1 : -1),
                input.split(" ")[0],
                input.split(" ")[10].replace(".", ""));
    }

    public int getChange() {
        return change;
    }

    public String getPerson() {
        return person;
    }

    public String getNextTo() {
        return nextTo;
    }

}
