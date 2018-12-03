package krabban91.kodvent.kodvent.day3;

public class Claim {

    private int id;
    private int x;
    private int y;
    private int width;
    private int height;

    public Claim(String claim){
        String[] split = claim.split(" @ ");
        this.id = Integer.parseInt(split[0].replace("#",""));
        split = split[1].split(",");
        this.x = Integer.parseInt(split[0]);
        split = split[1].split(": ");
        this.y = Integer.parseInt(split[0]);
        split = split[1].split("x");
        this.width = Integer.parseInt(split[0]);
        this.height = Integer.parseInt(split[1]);
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
