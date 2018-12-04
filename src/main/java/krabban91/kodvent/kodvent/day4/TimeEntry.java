package krabban91.kodvent.kodvent.day4;

import java.util.regex.Pattern;

public class TimeEntry {

    private static Pattern pattern = Pattern.compile("[( )(:)(#)(^\\])]");
    private Integer guardId;
    private int minute;
    //0= begin shift, 1= fall asleep, 2= wake up
    private int typeOfEntry;

    public TimeEntry(String entry) {
        String[] patternSplit = pattern.split(entry);
        this.minute = Integer.parseInt(patternSplit[2]);
        if (entry.contains("wake")) {
            typeOfEntry = 2;
        } else if (entry.contains("falls")) {
            typeOfEntry = 1;
        } else {
            typeOfEntry = 0;
            this.guardId = Integer.parseInt(patternSplit[6]);
        }
    }

    public Integer getGuardId() {
        return guardId;
    }

    public int getMinute() {
        return minute;
    }

    public int getTypeOfEntry() {
        return typeOfEntry;
    }
}
