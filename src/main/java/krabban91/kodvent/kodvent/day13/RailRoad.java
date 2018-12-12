package krabban91.kodvent.kodvent.day13;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class RailRoad {
    List<Cart> carts = new ArrayList();
int tick = 0;
    public RailRoad(List<String> rows) {
        IntStream.range(0, rows.size()).forEach(y -> IntStream.range(0, rows.get(y).length()).forEach(x -> {
            char c = rows.get(y).charAt(x);
            if (c == '^') {
                carts.add(new Cart(x, y, 0, rows));
            } else if (c == '<') {
                carts.add(new Cart(x, y, 3, rows));
            } else if (c == 'v') {
                carts.add(new Cart(x, y, 2, rows));
            } else if (c == '>') {
                carts.add(new Cart(x, y, 1, rows));
            }
        }));
    }

    public Point runUntilCollision(){
        while (!collisionOccured()){
            System.out.println(tick);
            carts.forEach(Cart::reportLocation);
            carts.forEach(Cart::moveOneTick);
            tick++;
        }
        System.out.println(tick);
        carts.forEach(Cart::reportLocation);
        return getPointOfCollision();
    }

    private Point getPointOfCollision() {
        return carts.stream()
                .filter(l -> carts.stream()
                        .anyMatch(r -> l.equals(r))).findAny().get().getCoordinate();
    }

    private boolean collisionOccured() {
        return carts.stream().anyMatch(l -> carts.stream().anyMatch(r -> l.equals(r)));
    }
}
