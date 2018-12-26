package krabban91.kodvent.kodvent.y2018.d13;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Cart {

    private final List<String> tracks;
    private int y;
    private int x;
    private boolean crashed = false;
    //up = 0, left = 3, down = 2, right = 1
    private int currentDirection;
    //left = -1, straight = 0, right = 1
    private AtomicInteger nextIntersectionAction = new AtomicInteger(-1);

    public Cart(int startX, int startY, int startDirection, List<String> tracks) {
        this.x = startX;
        this.y = startY;
        this.currentDirection = startDirection;
        this.tracks = tracks;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Point getCoordinate() {
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Cart) {
            Cart other = (Cart) o;
            return !super.equals(other) && this.getX() == other.getX() && this.getY() == other.getY();
        }
        return false;
    }

    public void crash() {
        this.crashed = true;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void moveOneTick() {
        moveOneStep();
        char c = this.tracks.get(this.y).charAt(this.x);
        rotateBasedOnTile(c);
    }

    private void moveOneStep() {
        if (currentDirection % 2 == 0) {
            // vertical movement
            this.y += (currentDirection - 1);
        } else {
            // horizontal movement
            this.x -= (currentDirection - 2);
        }
    }

    private void rotateBasedOnTile(char c) {
        if (c == '+') {
            this.currentDirection = addWithMod4(this.currentDirection, this.nextIntersectionAction.getAndUpdate(this::nextDirection));
        } else if (c == '/') {
            if (this.currentDirection % 2 == 0) {
                // was moving vertically
                this.currentDirection += 1;
            } else {
                // was moving horizontally
                this.currentDirection -= 1;
            }
        } else if (c == '\\') {
            if (this.currentDirection % 2 == 0) {
                // was moving vertically
                this.currentDirection = addWithMod4(this.currentDirection, -1);
            } else {
                // was moving horizontally
                this.currentDirection = addWithMod4(this.currentDirection, 1);
            }
        }
    }

    private int addWithMod4(int a, int b) {
        return (a + b + 4) % 4;
    }

    private int nextDirection(int currentDirection) {
        return currentDirection == 1 ? -1 : currentDirection + 1;
    }

    public static int compare(Cart l, Cart r) {
        int yCompare = Integer.compare(l.getY(), r.getY());
        if(yCompare == 0){
            return Integer.compare(l.getX(), r.getX());
        }
        return yCompare;
    }
}
