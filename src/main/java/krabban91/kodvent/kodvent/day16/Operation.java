package krabban91.kodvent.kodvent.day16;

public class Operation {
    int operation;
    int a;
    int b;
    int c;

    public Operation(String s){
        String[] operations = s.split(" ");
        this.operation = Integer.parseInt(operations[0]);
        this.a = Integer.parseInt(operations[1]);
        this.b = Integer.parseInt(operations[2]);
        this.c = Integer.parseInt(operations[3]);
    }
}
