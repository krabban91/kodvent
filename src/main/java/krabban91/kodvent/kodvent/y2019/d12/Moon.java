package krabban91.kodvent.kodvent.y2019.d12;

import krabban91.kodvent.kodvent.utilities.Point3D;

import java.util.List;
import java.util.stream.Collectors;

public class Moon {
    private Point3D location;
    private Point3D velocity;

public Moon(int lx, int ly, int lz, int vx, int vy, int vz){
    this.location = new Point3D(lx,ly,lz);
    this.velocity = new Point3D(vx,vy,vz);
}

    public Moon(String s){
        String[] s1 = s.split("=");
        int x = Integer.parseInt(s1[1].split(",")[0]);
        int y = Integer.parseInt(s1[2].split(",")[0]);
        int z = Integer.parseInt(s1[3].split(">")[0]);
        location = new Point3D(x, y, z);
        velocity = new Point3D(0,0,0);

    }

    public void applyGravity(List<Moon> moons){
        final List<Moon> other = moons.stream().filter(m -> !m.equals(this)).collect(Collectors.toList());
        int x = this.velocity.getX();
        int y = this.velocity.getY();
        int z = this.velocity.getZ();
        for(Moon m : other){
            x +=Integer.compare(m.location.getX(), this.location.getX());
            y +=Integer.compare(m.location.getY(), this.location.getY());
            z +=Integer.compare(m.location.getZ(), this.location.getZ());
        }
        this.velocity = new Point3D(x,y,z);
    }
    public void move(){
        this.location = new Point3D(this.velocity.getX()+this.location.getX(), this.velocity.getY()+this.location.getY(), this.velocity.getZ()+ this.location.getZ());
    }

    @Override
    protected Object clone() {
        return new Moon(location.getX(), location.getY(), location.getZ(), velocity.getX(), velocity.getY(), velocity.getZ());
    }

    public int pot(){
        return Math.abs(location.getX()) + Math.abs(location.getY()) + Math.abs(location.getZ());
    }

    public int kin(){
        return Math.abs(velocity.getX()) + Math.abs(velocity.getY()) + Math.abs(velocity.getZ());
    }
    public int tot(){
    return this.pot() * this.kin();
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
