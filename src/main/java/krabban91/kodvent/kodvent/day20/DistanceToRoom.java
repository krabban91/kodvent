package krabban91.kodvent.kodvent.day20;


public class DistanceToRoom {
    Room room;
    int distance;

    public DistanceToRoom(Room room, int distance){
        this.room = room;
        this.distance = distance;
    }

    public Room getRoom() {
        return room;
    }

    public int getDistance() {
        return this.distance;
    }
}
