package krabban91.kodvent.kodvent.y2019.d06;

public class OrbitRelation {
    String main;
    String orbiter;
    public OrbitRelation(String s ){
        String[] split = s.split("\\)");
        main = split[0];
        orbiter = split[1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrbitRelation that = (OrbitRelation) o;

        if (!main.equals(that.main)) return false;
        return orbiter.equals(that.orbiter);
    }

    @Override
    public int hashCode() {
        int result = main.hashCode();
        result = 31 * result + orbiter.hashCode();
        return result;
    }
}
