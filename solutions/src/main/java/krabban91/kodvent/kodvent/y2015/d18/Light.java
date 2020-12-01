package krabban91.kodvent.kodvent.y2015.d18;

import krabban91.kodvent.kodvent.utilities.logging.Loggable;

import java.util.List;

public class Light implements Loggable {
    private boolean currentState;
    private Boolean nextState;

    public Light(int size) {
        this.currentState = size == '#';
    }

    public int getState() {
        return this.currentState ? 1 : 0;
    }

    @Override
    public String showTile() {
        return this.currentState ? "#" : " ";
    }

    public void setNextState(List<Light> neighbors) {
        long count = neighbors.stream()
                .filter(Light::isCurrentState)
                .count();
        if (currentState) {
            nextState = count == 2 || count == 3;
        } else {
            nextState = count == 3;
        }
    }

    public void setNextState(boolean on) {
        nextState = on;
    }

    public boolean isCurrentState() {
        return currentState;
    }

    public void moveForwardInTime() {
        currentState = nextState;
        nextState = null;
    }
}
