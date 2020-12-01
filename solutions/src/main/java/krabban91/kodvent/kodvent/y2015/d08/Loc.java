package krabban91.kodvent.kodvent.y2015.d08;

public class Loc {

    private String code;

    public Loc(String s) {
        this.code = s;
    }

    public int codeChars() {
        return this.code.length();
    }

    public int inMemoryChars() {
        String substring = code.substring(1, code.length() - 1);
        int count = 0;
        for (int i = 0; i < substring.length(); i++) {
            char c = substring.charAt(i);
            if (c == '\\') {
                if (substring.charAt(i + 1) == 'x') {
                    i += 2;
                }
                i++;
            }
            count++;
        }
        return count;
    }

    public int encodedChars() {
        return code.chars()
                .mapToObj(i -> {
                    if (i == (int) '\\') {
                        return "\\\\";
                    } else if (i == (int) '\"') {
                        return "\\\"";
                    } else {
                        return "" + (char) i;
                    }
                })
                .reduce(
                        new StringBuilder(),
                        StringBuilder::append,
                        (l, r) -> l.append(r.toString()))
                .toString().length() + 2;
    }
}
