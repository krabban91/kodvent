package krabban91.kodvent.kodvent.y2018.d20;

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
    }

    public Room(Room cameFrom, Point point, Maze maze) {
        if (cameFrom == null) {
            this.isStart = true;
        } else {

        }
        this.point = point;
        this.maze = maze;
        this.maze.AddRoomToExploration(this);
    }

    public static void exploreMap(Room startingRoom, String map, Maze maze) {
        String mapToHandle = map;
        Room currentRoom = startingRoom;
        while (!mapToHandle.isEmpty()) {
            switch (mapToHandle.charAt(0)) {
                case '(':
                    //branch
                    int end = indexOfEnclosingParenthesis(mapToHandle);
                    String substring = mapToHandle.substring(0, end);
                    // do branching.
                    List<String> strings = splitOnlyOnSameParenthesisLevel(substring);
                    for (String m : strings) {
                        exploreMap(currentRoom, m, maze);
                    }
                    mapToHandle = mapToHandle.substring(end);

                    break;
                case 'N':
                    mapToHandle = mapToHandle.substring(1);
                    currentRoom = exploreNorth(currentRoom, maze);
                    break;
                case 'W':
                    mapToHandle = mapToHandle.substring(1);
                    currentRoom = exploreWest(currentRoom, maze);
                    break;
                case 'S':
                    mapToHandle = mapToHandle.substring(1);
                    currentRoom = exploreSouth(currentRoom, maze);
                    break;
                case 'E':
                    mapToHandle = mapToHandle.substring(1);
                    currentRoom = exploreEast(currentRoom, maze);
                    break;
            }
        }
    }

    private static Room exploreNorth(Room startingRoom, Maze maze) {
        Point target = new Point(startingRoom.point.x, startingRoom.point.y - 1);
        startingRoom.north = new Room(startingRoom, target, maze);
        startingRoom.north.south = startingRoom;
        List<Room> roomCopiesAtPoint = maze.getRoomCopiesAtPoint(startingRoom.north.point);
        if (roomCopiesAtPoint.size() > 1) {
            startingRoom.north = joinDuplicateRooms(roomCopiesAtPoint);
            //handle duplicates
        }
        return startingRoom.north;
    }

    private static Room exploreWest(Room startingRoom, Maze maze) {
        Point target = new Point(startingRoom.point.x - 1, startingRoom.point.y);
        startingRoom.west = new Room(startingRoom, target, maze);
        startingRoom.west.east = startingRoom;
        List<Room> roomCopiesAtPoint = maze.getRoomCopiesAtPoint(startingRoom.west.point);
        if (roomCopiesAtPoint.size() > 1) {
            startingRoom.west = joinDuplicateRooms(roomCopiesAtPoint);
            //handle duplicates
        }
        return startingRoom.west;
    }

    private static Room exploreSouth(Room startingRoom, Maze maze) {
        Point target = new Point(startingRoom.point.x, startingRoom.point.y + 1);
        startingRoom.south = new Room(startingRoom, target, maze);
        startingRoom.south.north = startingRoom;
        List<Room> roomCopiesAtPoint = maze.getRoomCopiesAtPoint(startingRoom.south.point);
        if (roomCopiesAtPoint.size() > 1) {
            startingRoom.south = joinDuplicateRooms(roomCopiesAtPoint);
            //handle duplicates
        }
        return startingRoom.south;
    }

    private static Room exploreEast(Room startingRoom, Maze maze) {
        Point target = new Point(startingRoom.point.x + 1, startingRoom.point.y);
        startingRoom.east = new Room(startingRoom, target, maze);
        startingRoom.east.west = startingRoom;

        List<Room> roomCopiesAtPoint = maze.getRoomCopiesAtPoint(startingRoom.east.point);
        if (roomCopiesAtPoint.size() > 1) {
            startingRoom.east = joinDuplicateRooms(roomCopiesAtPoint);
            //handle duplicates
        }
        return startingRoom.east;
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
                    if (!indexes.contains(i - 1)) {
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
