package krabban91.kodvent.kodvent.day9;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class MarbleBall {

    private int value;


    public MarbleBall(int value) {
        this.value= value;
    }

    public int getValue() {
        return value;
    }
    public String toString(){
        return ""+this.value;
    }
}
