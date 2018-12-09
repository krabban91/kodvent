package krabban91.kodvent.kodvent.day9;

public class MarbleRules {

    int numberOfPlayers;
    int numberOfMarbles;

    public MarbleRules(String input) {
        String[] words = input.split(" ");
        this.numberOfPlayers = Integer.parseInt(words[0]);
        this.numberOfMarbles = Integer.parseInt(words[6]);
    }

    public MarbleRules(int numberOfPlayers, int highestMarble) {
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfMarbles = highestMarble;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getHighestMarbleValue() {
        return numberOfMarbles;
    }

}
