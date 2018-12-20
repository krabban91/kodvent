package krabban91.kodvent.kodvent.day20;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Maze {

    private Room startingPoint;

    private Map<Point, List<Room>> roomCopies = new HashMap<>();

    private Map<Point, DistanceToRoom> distances = new HashMap<>();

    public Maze(String regex) {
        String map = regex.substring(regex.indexOf("^") + 1, regex.lastIndexOf("$"));
        startingPoint = new Room(null, map, new Point(0, 0), this);
        Room.exploreMap(startingPoint,map, this);
    }

    public List<Room> getRoomCopiesAtPoint(Point point) {
        return roomCopies.get(point);
    }

    public void AddRoomToExploration(Room room) {
        Point point = room.getPoint();
        if (!roomCopies.containsKey(point)) {
            roomCopies.put(point, new ArrayList<>());
        }
        roomCopies.get(point).add(room);
    }

    private void performBFS(){
        PriorityQueue<DistanceToRoom> frontier = new PriorityQueue<>(Comparator.comparingInt(DistanceToRoom::getDistance));
        frontier.add(new DistanceToRoom(startingPoint,0));
        while (!frontier.isEmpty()){
            DistanceToRoom pop = frontier.poll();
            distances.put(pop.getRoom().getPoint(), pop);
            if(pop.room.getNorth() != null && !distances.containsKey(pop.room.getNorth().getPoint())){
                frontier.add(new DistanceToRoom(pop.room.getNorth(), pop.getDistance()+1));
            }
            if(pop.room.getWest() != null && !distances.containsKey(pop.room.getWest().getPoint())){
                frontier.add(new DistanceToRoom(pop.room.getWest(), pop.getDistance()+1));
            }
            if(pop.room.getSouth() != null && !distances.containsKey(pop.room.getSouth().getPoint())){
                frontier.add(new DistanceToRoom(pop.room.getSouth(), pop.getDistance()+1));
            }
            if(pop.room.getEast() != null && !distances.containsKey(pop.room.getEast().getPoint())){
                frontier.add(new DistanceToRoom(pop.room.getEast(), pop.getDistance()+1));

            }
        }
    }

    public int getDistanceToFurthestRoom(){
        this.performBFS();
        return distances.entrySet().stream().max(Comparator.comparingInt(e-> e.getValue().getDistance())).get().getValue().getDistance();
    }

    public long getCountOfRoomsWithDistanceAtleast(int distance){
        this.performBFS();
        return distances.entrySet().stream().filter(e-> e.getValue().getDistance()>=distance).count();
    }

}
