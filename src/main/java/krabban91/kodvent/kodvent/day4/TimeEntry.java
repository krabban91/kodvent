package krabban91.kodvent.kodvent.day4;

public class TimeEntry {

    private Integer guardId;
    private String day;
    private int minute;
    //0= begin shift, 1= fall asleep, 2= wake up
    private int typeOfEntry;


    public TimeEntry(String entry){
        String[] split = entry.split("]");
        String[] s = split[0].split(" ");
        this.day = s[0].split(" ")[0];
        this.minute = Integer.parseInt(s[1].split(":")[1]);
        if(entry.contains("wake")){
            typeOfEntry = 2;
        } else if(entry.contains("falls")){
            typeOfEntry = 1;
        }
        else {
            typeOfEntry = 0;
            this.guardId = Integer.parseInt(split[1].split("#")[1].split(" ")[0]);
        }
    }

    public Integer getGuardId() {
        return guardId;
    }

    public String getDay() {
        return day;
    }

    public int getMinute() {
        return minute;
    }

    public int getTypeOfEntry() {
        return typeOfEntry;
    }


}
