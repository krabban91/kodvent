package krabban91.kodvent.kodvent.day13;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RailRoad {
    List<Cart> carts = new ArrayList();
    boolean collisionOccured;
    Point firstCollision;

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

    public Point runUntilOnlyOneRemains() {
        this.removeChrashedCarts();
        while (carts.size() > 1) {
            carts.stream()
                    .sorted(Cart::compare)
                    .forEach(this::moveCartAndRemoveIfCollisionOccurs);
        }
        return getPointOfLastRemainingCart();
    }

    public Point runUntilCollision() {
        while (!collisionOccured()) {
            carts.stream()
                    .sorted(Cart::compare)
                    .forEach(this::moveCartAndRemoveIfCollisionOccurs);
        }
        return getPointOfFirstCollision();
    }

    private void moveCartAndRemoveIfCollisionOccurs(Cart cart) {
        if (cart.isCrashed()) {
            return;
        }
        cart.moveOneTick();
        removeChrashedCarts();
    }

    private void removeChrashedCarts() {
        carts.stream()
                .filter(l -> carts.stream()
                        .anyMatch(r -> l.equals(r)))
                .forEach(Cart::crash);

        if (!collisionOccured) {
            Optional<Cart> any = carts.stream()
                    .filter(c -> c.isCrashed())
                    .findAny();
            if (any.isPresent()) {
                collisionOccured = true;
                Cart cart = any.get();
                firstCollision = new Point(cart.getX(), cart.getY());
            }
        }
        carts = carts.stream()
                .filter(c -> !c.isCrashed())
                .collect(Collectors.toList());
    }

    private Point getPointOfLastRemainingCart() {
        return carts.get(0).getCoordinate();
    }

    private Point getPointOfFirstCollision() {
        return firstCollision;
    }

    private boolean collisionOccured() {
        return collisionOccured;
    }
}
