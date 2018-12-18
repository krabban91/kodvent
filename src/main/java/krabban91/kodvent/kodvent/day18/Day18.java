package krabban91.kodvent.kodvent.day18;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class Day18 {
    private static String inputPath = "day18.txt";
    List<List<SettlerTile>> in;
    int second = 0;

    public long getPart1() {
        tickSeconds(10);
        return getScoreOfSecond();
    }

    void tickSeconds(int second){
        while(this.second<second){
            visualizeLand();
            tickOneSecond();
        }
    }

    void visualizeLand(){
        System.out.println("Second "+second);
        IntStream.range(0,in.size()).forEach(y-> {
            StringBuilder builder = new StringBuilder();
            in.get(y).forEach(t -> builder.append(t));
            System.out.println(builder.toString());
        });
    }

    void tickOneSecond(){

        IntStream.range(0, in.size()).forEach(y -> IntStream.range(0,in.get(y).size()).forEach(x->{
            SettlerTile current = in.get(y).get(x);
            if(current.oldState == SettlerTile.Type.OPEN){
                int adjacentTrees = 0;
                if(x>0 && y>0){
                    adjacentTrees+= in.get(y-1).get(x-1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(y>0){
                    adjacentTrees+= in.get(y-1).get(x).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(y>0 && x<in.get(y).size()-1){
                    adjacentTrees+= in.get(y-1).get(x+1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(x<in.get(y).size()-1){
                    adjacentTrees+= in.get(y).get(x+1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(y<in.size()-1 && x<in.get(y).size()-1){
                    adjacentTrees+= in.get(y+1).get(x+1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if (y < in.size()-1){
                    adjacentTrees+= in.get(y+1).get(x).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(y<in.size()-1 && x>0){
                    adjacentTrees+= in.get(y+1).get(x-1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(x>0){
                    adjacentTrees+= in.get(y).get(x-1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(adjacentTrees >=3){
                    current.newState = SettlerTile.Type.FOREST;
                }
            } else if(current.oldState == SettlerTile.Type.FOREST){
                int adjacentMills = 0;
                if(x>0 && y>0){
                    adjacentMills+= in.get(y-1).get(x-1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                }
                if(y>0){
                    adjacentMills+= in.get(y-1).get(x).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                }
                if(y>0 && x<in.get(y).size()-1){
                    adjacentMills+= in.get(y-1).get(x+1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                }
                if(x<in.get(y).size()-1){
                    adjacentMills+= in.get(y).get(x+1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                }
                if(y<in.size()-1 && x<in.get(y).size()-1){
                    adjacentMills+= in.get(y+1).get(x+1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                }
                if (y < in.size()-1){
                    adjacentMills+= in.get(y+1).get(x).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                }
                if(y<in.size()-1 && x>0){
                    adjacentMills+= in.get(y+1).get(x-1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                }
                if(x>0){
                    adjacentMills+= in.get(y).get(x-1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                }
                if(adjacentMills >=3){
                    current.newState = SettlerTile.Type.LUMBERMILL;
                }
            }
            else if(current.oldState == SettlerTile.Type.LUMBERMILL){
                int adjacentMills = 0;
                int adjacentTrees = 0;
                if(x>0 && y>0){
                    adjacentMills+= in.get(y-1).get(x-1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    adjacentTrees+= in.get(y-1).get(x-1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(y>0){
                    adjacentMills+= in.get(y-1).get(x).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    adjacentTrees+= in.get(y-1).get(x).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(y>0 && x<in.get(y).size()-1){
                    adjacentMills+= in.get(y-1).get(x+1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    adjacentTrees+= in.get(y-1).get(x+1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(x<in.get(y).size()-1){
                    adjacentMills+= in.get(y).get(x+1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    adjacentTrees+= in.get(y).get(x+1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(y<in.size()-1 && x<in.get(y).size()-1){
                    adjacentMills+= in.get(y+1).get(x+1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    adjacentTrees+= in.get(y+1).get(x+1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if (y < in.size()-1){
                    adjacentMills+= in.get(y+1).get(x).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    adjacentTrees+= in.get(y+1).get(x).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(y<in.size()-1 && x>0){
                    adjacentMills+= in.get(y+1).get(x-1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    adjacentTrees+= in.get(y+1).get(x-1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(x>0){
                    adjacentMills+= in.get(y).get(x-1).oldState == SettlerTile.Type.LUMBERMILL ? 1 : 0;
                    adjacentTrees+= in.get(y).get(x-1).oldState == SettlerTile.Type.FOREST ? 1 : 0;
                }
                if(adjacentMills >=1 && adjacentTrees >=1 ){
                    current.newState = SettlerTile.Type.LUMBERMILL;
                } else{
                    current.newState = SettlerTile.Type.OPEN;
                }
            }

        }));
        in.forEach(l -> l.forEach(SettlerTile::oldToNew));
        second++;
    }

    long getScoreOfSecond(){
        Long lumberMills = in.stream().map(l -> l.stream().filter(t -> t.oldState == SettlerTile.Type.LUMBERMILL).count()).reduce(0L, Long::sum);
        Long forests = in.stream().map(l -> l.stream().filter(t -> t.oldState == SettlerTile.Type.FOREST).count()).reduce(0L, Long::sum);
        return lumberMills*forests;
    }

    public long getPart2() {
        return -1;
    }


    public List<List<SettlerTile>> readInput(Stream<String> stream) {
        return stream
                .map(s -> s.chars().mapToObj(SettlerTile::new).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public Day18() {
        System.out.println("::: Starting Day 18 :::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            in = readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        long part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }
}
