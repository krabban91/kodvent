package krabban91.kodvent.kodvent.day9;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class CircularList<E> extends ArrayList<E> {

    public int lastIndex = 0;

    @Override
    public void add(int index, E value) {
        if (index == 0 || index == size()) {
            lastIndex = size();
            add(value);
        } else if (index < 0) {
            add(index + size(), value);
        } else if (index > size()) {
            add(index - size(), value);
        } else {
            lastIndex = index;
            super.add(index, value);
        }
    }

    @Override
    public E remove(int index) {
        if (index < 0) {
            return remove(index + size());
        }
        if (index >= size()) {
            return remove(index - size());
        }
        lastIndex = index;
        return super.remove(index);
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
