package krabban91.kodvent.kodvent.y2019.d12;

import krabban91.kodvent.kodvent.utilities.Point3D;

import java.util.List;
import java.util.stream.Collectors;

public class Moon {
    private Point3D location;
    private Point3D velocity;

    private Moon(Point3D location, Point3D velocity) {
        this.location = location;
        this.velocity = velocity;
    }

    public Moon(String s) {
        String[] s1 = s.split("=");
        int x = Integer.parseInt(s1[1].split(",")[0]);
        int y = Integer.parseInt(s1[2].split(",")[0]);
        int z = Integer.parseInt(s1[3].split(">")[0]);
        location = new Point3D(x, y, z);
        velocity = new Point3D(0, 0, 0);

    }

    public void measureGravity(List<Moon> moons) {
        final List<Moon> other = moons.stream()
                .filter(m -> !m.equals(this))
                .collect(Collectors.toList());
        int x = this.velocity.getX();
        int y = this.velocity.getY();
        int z = this.velocity.getZ();
        for (Moon m : other) {
            x += Integer.compare(m.location.getX(), this.location.getX());
            y += Integer.compare(m.location.getY(), this.location.getY());
            z += Integer.compare(m.location.getZ(), this.location.getZ());
        }
        this.velocity = new Point3D(x, y, z);
    }

    public void move() {
        this.location = this.location.add(this.velocity);
    }

    protected Moon copy() {
        return new Moon(location.copy(), velocity.copy());
    }

    public int potentialEnergy() {
        return Math.abs(location.getX()) + Math.abs(location.getY()) + Math.abs(location.getZ());
    }

    public int kineticEnergy() {
        return Math.abs(velocity.getX()) + Math.abs(velocity.getY()) + Math.abs(velocity.getZ());
    }

    public int totalEnergy() {
        return this.potentialEnergy() * this.kineticEnergy();
    }

    public boolean xMatch(Moon o) {
        return this.location.getX() == o.location.getX() && this.velocity.getX() == o.velocity.getX();
    }

    public boolean yMatch(Moon o) {
        return this.location.getY() == o.location.getY() && this.velocity.getY() == o.velocity.getY();
    }

    public boolean zMatch(Moon o) {
        return this.location.getZ() == o.location.getZ() && this.velocity.getZ() == o.velocity.getZ();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Moon moon = (Moon) o;

        if (!location.equals(moon.location)) return false;
        return velocity.equals(moon.velocity);
    }

    @Override
    public int hashCode() {
        int result = location.hashCode();
        result = 31 * result + velocity.hashCode();
        return result;
    }
}
