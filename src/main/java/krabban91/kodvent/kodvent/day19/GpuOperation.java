package krabban91.kodvent.kodvent.day19;

public class GpuOperation {
    String operation;
    int a;
    int b;
    int c;
    String toString;

    public GpuOperation(String s){
        toString = s;
        String[] operations = s.split(" ");
        this.operation = operations[0];
        this.a = Integer.parseInt(operations[1]);
        this.b = Integer.parseInt(operations[2]);
        this.c = Integer.parseInt(operations[3]);
    }

    public String getOperation() {
        return operation;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    public String toString(){
        return toString;
    }
}
