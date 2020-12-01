package krabban91.kodvent.kodvent.y2018.d22;

public class Mode {
    private Region region;
    private Tool tool;

    public Mode(Region region, Tool tool) {
        this.region = region;
        this.tool = tool;
    }

    public Region getRegion() {
        return region;
    }

    public Tool getTool() {
        return tool;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Mode) {
            Mode other = (Mode) obj;
            return getRegion().equals(other.getRegion()) && getTool().equals(other.getTool());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 2 * region.hashCode() + 3 * tool.hashCode();
    }
}
