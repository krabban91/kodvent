package krabban91.kodvent.kodvent.y2015.d08;

import java.util.List;

public class Parser {
    private final List<Loc> linesOfCode;

    public Parser(List<Loc> linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public long totalCodeCharacters() {
        return linesOfCode.stream().map(Loc::codeChars).reduce(0, Integer::sum);
    }

    public long totalInMemoryCharacters() {
        return linesOfCode.stream().map(Loc::inMemoryChars).reduce(0, Integer::sum);
    }

    public long totalSizeOfEncodedCharacters() {
        return linesOfCode.stream().map(Loc::encodedChars).reduce(0, Integer::sum);
    }
}
