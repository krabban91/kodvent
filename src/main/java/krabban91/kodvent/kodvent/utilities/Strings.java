package krabban91.kodvent.kodvent.utilities;

public class Strings {

    public static String repeated(String input, int times){
        return new String(new char[times]).replace("\0", input);
    }

    public static final String ALPHABBET = "abcdefghijklmnopqrstuvwxyz";
}
