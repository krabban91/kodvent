package krabban91.kodvent.kodvent.utilities;

import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Input {

    public static List<String> getLines(String inputPath) {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static String getSingleLine(String inputPath) {
        try (Stream<String> stream = Files.lines(Paths.get(new ClassPathResource(inputPath).getFile().getPath()))) {
            return stream.findFirst().orElse(Strings.EMPTY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Strings.EMPTY;
    }
}
