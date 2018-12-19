package krabban91.kodvent.kodvent.day16;

public class OpCodesALU implements OpCodes {

    @Override
    public int[] addr(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]+register[b];
        return newRegister;
    }

    @Override
    public int[] addi(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]+b;
        return newRegister;
    }

    @Override
    public int[] mulr(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]*register[b];
        return newRegister;
    }

    @Override
    public int[] muli(int[] register, int a, int b, int c) {

        int[] newRegister = register.clone();
        newRegister[c] = register[a]*b;
        return newRegister;
    }

    @Override
    public int[] banr(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]&register[b];
        return newRegister;
    }

    @Override
    public int[] bani(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]&b;
        return newRegister;
    }

    @Override
    public int[] borr(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]|register[b];
        return newRegister;
    }

    @Override
    public int[] bori(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]|b;
        return newRegister;
    }

    @Override
    public int[] setr(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a];
        return newRegister;
    }

    @Override
    public int[] seti(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = a;
        return newRegister;
    }

    @Override
    public int[] gtir(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = a>register[b] ? 1 : 0;
        return newRegister;
    }

    @Override
    public int[] gtri(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]>b ? 1: 0;
        return newRegister;
    }

    @Override
    public int[] gtrr(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]>register[b] ? 1:0;
        return newRegister;
    }

    @Override
    public int[] eqir(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = a==register[b] ? 1:0;
        return newRegister;
    }

    @Override
    public int[] eqri(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]==b ? 1:0;
        return newRegister;
    }

    @Override
    public int[] eqrr(int[] register, int a, int b, int c) {
        int[] newRegister = register.clone();
        newRegister[c] = register[a]==register[b] ? 1:0;
        return newRegister;
    }
}
