package krabban91.kodvent.kodvent.y2015.d02;

import krabban91.kodvent.kodvent.utilities.Point3D;

public class Gift {

    Point3D box;

    public Gift(String dimensions) {
        String[] split = dimensions.split("x");
        this.box = new Point3D(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public int wrappingPaperArea() {
        int a = box.getX() * box.getY();
        int b = box.getX() * box.getZ();
        int c = box.getY() * box.getZ();
        int area = 2 * a + 2 * b + 2 * c;
        int slack = Math.min(a, Math.min(b, c));
        return area + slack;
    }

    public int ribbonLength() {
        int max = Math.max(box.getX(), Math.max(box.getY(), box.getZ()));
        int wrap = 2*(box.getX()+ box.getY()+ box.getZ() - max);
        int bow = box.getX()* box.getY()* box.getZ();
        return wrap + bow;
    }
}
