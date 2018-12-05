package krabban91.kodvent.kodvent.day5;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class AlchemicalReactor {

    private static String inputPath = "day5.txt";
    private String inputPolymer;
    private static String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private Map<String, Integer> score = new HashMap<>();

    public AlchemicalReactor() {
        System.out.println("::: Starting Day 5:::");
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            readInput(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int part1 = getPart1();
        System.out.println(": answer to part 1 :");
        System.out.println(part1);
        int part2 = getPart2();
        System.out.println(": answer to part 2 :");
        System.out.println(part2);
    }

    public int getPart1() {
        return this.applyReactionFully(inputPolymer).length();
    }

    public int getPart2() {
        IntStream.range(0,alphabet.length()).forEach(this::retrieveResultForUnit);
        return score.entrySet()
                .stream()
                .min(Comparator.comparingInt(e-> e.getValue()))
                .get().getValue();
    }

    private void retrieveResultForUnit(int i) {
        String character = alphabet.substring(i, i + 1);
        score.put(character, applyReactionFully(inputPolymer.replaceAll(character,"").replaceAll(character.toUpperCase(),"")).length());
    }



    private String applyReactionFully(String s){
        int length= Integer.MAX_VALUE;
        while (length != s.length()){
            length = s.length();
            s = this.performReaction(s);
        }
        return s;
    }

    private String performReaction(String s){
        for(int i = 0; i<s.length()-1; i++){
            if(isMatchingUnit(s.charAt(i),s.charAt(i+1))){
                return s.substring(0,i)+s.substring(i+2);
            }
        }
        return s;
    }

    private boolean isMatchingUnit(Character l, Character r){
        return !l.equals(r) && (l.equals(Character.toLowerCase(r)) || l.equals(Character.toUpperCase(r)));
    }



    private void readInput(Stream<String> stream) {
        this.inputPolymer = stream.findFirst().get();
    }
}
