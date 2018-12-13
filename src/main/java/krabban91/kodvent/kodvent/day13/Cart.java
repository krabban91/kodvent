package krabban91.kodvent.kodvent.day13;

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
        if (currentDirection % 2 == 0) {

            //y movement
            this.y += currentDirection == 0 ? -1 : 1;
        } else {
            //x movement
            this.x += currentDirection == 1 ? 1 : -1;
        }
        char c = this.tracks.get(this.y).charAt(this.x);
        if (c == '+') {
            this.currentDirection += this.nextIntersectionAction.getAndUpdate(this::nextDirection);
            if (this.currentDirection < 0) {
                this.currentDirection += 4;
            }
            if (this.currentDirection > 3) {
                this.currentDirection -= 4;
            }
        } else if (c == '/') {
            if (this.currentDirection % 2 == 0) {
                // y
                this.currentDirection += 1;
            } else {
                // x
                this.currentDirection -= 1;
            }
        } else if (c == '\\') {
            if (this.currentDirection % 2 == 0) {
                // y
                this.currentDirection -= 1;
                if (this.currentDirection < 0) {
                    this.currentDirection += 4;
                }
            } else {
                // x
                this.currentDirection += 1;
                if (this.currentDirection > 3) {
                    this.currentDirection -= 4;
                }
            }
        }
    }

    private int nextDirection(int currentDirection) {
        return currentDirection == 1 ? -1 : currentDirection + 1;
    }

    public static void reportLocation(Cart cart) {
        System.out.println("x:" + cart.x + ",y:" + cart.y + ",dir:" + cart.currentDirection);
    }

    public static int compare(Cart l, Cart r) {
        return l.getY() == r.getY() ?
                (l.getX() == r.getX() ?
                        0 :
                        l.getX() - r.getX()) :
                l.getY() - r.getY();
    }
}
