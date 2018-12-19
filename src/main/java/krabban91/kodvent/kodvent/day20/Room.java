package krabban91.kodvent.kodvent.day20;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Room {

    private boolean isStart;
    private Room north;
    private Room west;
    private Room south;
    private Room east;
    private Point point;

    private Maze maze;

    public Room(Room cameFrom, String map, Point point, Maze maze) {
        if (cameFrom == null) {
            this.isStart = true;
        } else {

        }
        this.point = point;
        this.maze = maze;
        this.maze.AddRoomToExploration(this);
        if (map.length() == 0) {
            return;
        }
        explore(map);
    }

    private void explore(String map) {
        switch (map.charAt(0)) {
            case '(': {
                //branch
                int end = indexOfEnclosingParenthesis(map);
                String substring = map.substring(0, end);
                // do branching.
                List<String> strings = splitOnlyOnSameParenthesisLevel(substring);
                strings.stream().
                        filter(s -> !s.isEmpty())
                        .forEach(s -> explore(s));

                if (substring.charAt(substring.length() - 2) == '|') {
                    // edge case, carry on from here
                    if (map.length() > end + 1) {
                        explore(map.substring(end));
                    }
                }
                break;
            }
            case 'N': {
                exploreNorth(map, maze);
                break;
            }
            case 'W': {
                exploreWest(map, maze);
                break;
            }
            case 'S': {
                exploreSouth(map, maze);
                break;
            }
            case 'E': {
                exploreEast(map, maze);
                break;
            }
        }
    }

    private static List<String> splitOnlyOnSameParenthesisLevel(String map) {
        Stack<Character> parenthesises = new Stack<>();
        List<Integer> indexes = new ArrayList<>();
        AtomicBoolean isDone = new AtomicBoolean(false);
        indexes.add(0);
        IntStream.range(0, map.length()).forEach(i -> {
            if (!isDone.get()) {

                if (map.charAt(i) == '(') {
                    parenthesises.push((char) i);
                } else if (map.charAt(i) == ')') {
                    parenthesises.pop();
                }
                if (parenthesises.empty()) {
                    isDone.set(true);
                    if(!indexes.contains(i-1)){
                        indexes.add(i);
                    }
                }
                if (parenthesises.size() == 1 && map.charAt(i) == '|') {
                    indexes.add(i);
                }
            }
        }); //asd|sda
        List<String> splits = new ArrayList<>();
        IntStream.range(1, indexes.size()).forEach(i -> splits.add(map.substring(indexes.get(i - 1) + 1, indexes.get(i))));
        return splits;
    }

    private static int indexOfEnclosingParenthesis(String map) {
        Stack<Character> parenthesises = new Stack<>();
        AtomicInteger index = new AtomicInteger(-1);
        AtomicBoolean isDone = new AtomicBoolean(false);
        IntStream.range(0, map.length()).forEach(i -> {
            if (!isDone.get()) {
                if (map.charAt(i) == '(') {
                    parenthesises.push((char) i);
                } else if (map.charAt(i) == ')') {
                    parenthesises.pop();
                }
                if (parenthesises.empty()) {
                    isDone.set(true);
                    index.set(i + 1);
                }
            }
        });
        return index.get();
    }

    private void exploreEast(String map, Maze maze) {
        Point target = new Point(this.point.x + 1, this.point.y);
        east = new Room(this, map.substring(1), target, maze);
        List<Room> roomCopiesAtPoint = maze.getRoomCopiesAtPoint(east.point);
        if (roomCopiesAtPoint.size() > 1) {
            east = joinDuplicateRooms(roomCopiesAtPoint);
            //handle duplicates
        }
        maze.updateMap(east);
    }

    private void exploreSouth(String map, Maze maze) {
        Point target = new Point(this.point.x, this.point.y + 1);
        south = new Room(this, map.substring(1), target, maze);
        List<Room> roomCopiesAtPoint = maze.getRoomCopiesAtPoint(south.point);
        if (roomCopiesAtPoint.size() > 1) {
            south = joinDuplicateRooms(roomCopiesAtPoint);
            //handle duplicates
        }
        maze.updateMap(south);
    }

    private void exploreWest(String map, Maze maze) {
        Point target = new Point(this.point.x - 1, this.point.y);
        west = new Room(this, map.substring(1), target, maze);
        List<Room> roomCopiesAtPoint = maze.getRoomCopiesAtPoint(west.point);
        if (roomCopiesAtPoint.size() > 1) {
            west = joinDuplicateRooms(roomCopiesAtPoint);
            //handle duplicates
        }
        maze.updateMap(west);
    }

    private void exploreNorth(String map, Maze maze) {
        Point target = new Point(this.point.x, this.point.y - 1);
        north = new Room(this, map.substring(1), target, maze);
        List<Room> roomCopiesAtPoint = maze.getRoomCopiesAtPoint(north.point);
        if (roomCopiesAtPoint.size() > 1) {
            north = joinDuplicateRooms(roomCopiesAtPoint);
            //handle duplicates
        }
        maze.updateMap(north);
    }

    public Point getPoint() {
        return point;
    }

    private static Room joinDuplicateRooms(List<Room> rooms) {
        Room first = rooms.get(0);
        List<Room> rooms1 = rooms.subList(1, rooms.size());
        rooms1.forEach(r -> {
            first.isStart = first.isStart || r.isStart;
            if (r.north != null) {
                r.north.south = first;
            }
            if (r.west != null) {
                r.west.east = first;
            }
            if (r.south != null) {
                r.south.north = first;
            }
            if (r.east != null) {
                r.east.west = first;
            }
        });
        return first;
    }

    public Room getNorth() {
        return north;
    }

    public Room getWest() {
        return west;
    }

    public Room getSouth() {
        return south;
    }

    public Room getEast() {
        return east;
    }
}
