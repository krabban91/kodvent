package krabban91.kodvent.kodvent.y2018.d20;

import krabban91.kodvent.kodvent.utilities.search.Edge;
import krabban91.kodvent.kodvent.utilities.search.Graph;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Maze {

    private Room startingPoint;

    private Map<Point, List<Room>> roomCopies = new HashMap<>();

    private Map<Point, DoorsToRoom> distances = new HashMap<>();

    public Maze(String regex) {
        String map = regex.substring(regex.indexOf("^") + 1, regex.lastIndexOf("$"));
        startingPoint = new Room(null, map, new Point(0, 0), this);
        Room.exploreMap(startingPoint,map, this);
    }

    public List<Room> getRoomCopiesAtPoint(Point point) {
        return roomCopies.get(point);
    }

    public void addRoomToExploration(Room room) {
        Point point = room.getPoint();
        if (!roomCopies.containsKey(point)) {
            roomCopies.put(point, new ArrayList<>());
        }
        roomCopies.get(point).add(room);
    }

    private void performBFS(){
        PriorityQueue<DoorsToRoom> frontier = new PriorityQueue<>(Comparator.comparingInt(DoorsToRoom::cost));
        frontier.add(new DoorsToRoom(startingPoint));
        Graph<DoorsToRoom, Edge, Room> graph = new Graph<>();
        graph.search(frontier, this::exploreRoom, null);
    }

    private Collection<DoorsToRoom> exploreRoom(DoorsToRoom path, Map<Room,Collection<Edge>> ignored) {
        distances.put(path.destination().getPoint(), path);
        return path.destination().getDoors().stream()
                .filter(r -> !distances.containsKey(r.getPoint()))
                .map(r -> new DoorsToRoom(path, r))
                .collect(Collectors.toSet());
    }

    public int getDistanceToFurthestRoom(){
        this.performBFS();
        return distances.entrySet().stream().max(Comparator.comparingInt(e-> e.getValue().cost())).get().getValue().cost();
    }

    public long getCountOfRoomsWithDistanceAtleast(int distance){
        this.performBFS();
        return distances.entrySet().stream().filter(e-> e.getValue().cost()>=distance).count();
    }

}
