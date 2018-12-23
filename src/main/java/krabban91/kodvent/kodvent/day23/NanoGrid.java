package krabban91.kodvent.kodvent.day23;

import javafx.geometry.Point3D;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NanoGrid {

    List<NanoBot> nanoBots;

    public NanoGrid(Stream<String> input){
        this.nanoBots = input.map(NanoBot::new).collect(Collectors.toList());
    }

    public long numberOfNanobotsInRangeOfStrongest(){
        Optional<NanoBot> max = nanoBots.stream().max(Comparator.comparingInt(b -> b.getSignalRadius()));
        return max.map(nanoBot -> nanoBots.stream().filter(b -> b.manhattandistanceTo(nanoBot) <= nanoBot.getSignalRadius()).count()).orElse(0L);
    }


}
