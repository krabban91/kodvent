package krabban91.kodvent.kodvent.y2019.d03;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Wire {

    List<WirePath> instructions;

    public Wire(String s){
        instructions = Stream.of(s.split(",")).map(WirePath::new).collect(Collectors.toList());
    }
}
