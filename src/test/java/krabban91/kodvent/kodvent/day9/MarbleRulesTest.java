package krabban91.kodvent.kodvent.day9;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MarbleRulesTest {

    @Test
    public void parsesRulesCorrectly() {
        MarbleRules rules = new MarbleRules("5 players; last marble is worth 25 points");
        assertThat(rules.getHighestMarbleValue()).isEqualTo(25);
        assertThat(rules.getNumberOfPlayers()).isEqualTo(5);
    }
}
