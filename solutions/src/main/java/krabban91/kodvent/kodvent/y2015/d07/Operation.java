package krabban91.kodvent.kodvent.y2015.d07;

import krabban91.kodvent.kodvent.y2015.d07.expressions.Gate;
import krabban91.kodvent.kodvent.y2015.d07.expressions.gates.*;

public class Operation {

    Gate input;

    String result;

    public Operation(String input) {
        String[] split = input.split(" -> ");
        result = split[1];

        if (split[0].contains("AND")) {
            this.input = new And(split[0]);

        } else if (split[0].contains("OR")) {
            this.input = new Or(split[0]);
        } else if (split[0].contains("LSHIFT")) {
            this.input = new LeftShift(split[0]);
        } else if (split[0].contains("RSHIFT")) {
            this.input = new RightShift(split[0]);
        } else if (split[0].contains("NOT")) {
            this.input = new Not(split[0]);
        } else {
            this.input = new Set(split[0]);
        }
    }

    public Gate getInput() {
        return input;
    }

    public String getResult() {
        return result;
    }
}
