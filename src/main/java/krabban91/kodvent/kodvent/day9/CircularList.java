package krabban91.kodvent.kodvent.day9;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class CircularList<E> extends LinkedList<E> {

    public int lastIndex = 0;

    public void rotate(int steps){
        if (steps == 0){
            return;
        }
        if (steps <0){
            IntStream.range(0,-steps).forEach(this::rotateBackward);
        } else {
            IntStream.range(0,steps).forEach(this::rotateForward);
        }
    }

    private void rotateForward(int ignored) {
        this.addLast(this.removeFirst());
    }
    private void rotateBackward(int ignored) {
        this.addFirst(this.removeLast());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0, this.size()).forEach(i -> stringBuilder.append(this.entryToString(i)));
        return stringBuilder.toString();
    }

    private String entryToString(int i) {
        String format = " %s";
        if (i == lastIndex) {
            format = "(%s)";
        }
        return String.format(format, get(i));
    }

}
