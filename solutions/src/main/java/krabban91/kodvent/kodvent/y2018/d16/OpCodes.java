package krabban91.kodvent.kodvent.y2018.d16;

public interface OpCodes {

    int[] addr(int[] register, int a, int b, int c);

    int[] addi(int[] register, int a, int b, int c);

    int[] mulr(int[] register, int a, int b, int c);

    int[] muli(int[] register, int a, int b, int c);

    int[] banr(int[] register, int a, int b, int c);

    int[] bani(int[] register, int a, int b, int c);

    int[] borr(int[] register, int a, int b, int c);

    int[] bori(int[] register, int a, int b, int c);

    int[] setr(int[] register, int a, int b, int c);

    int[] seti(int[] register, int a, int b, int c);

    int[] gtir(int[] register, int a, int b, int c);

    int[] gtri(int[] register, int a, int b, int c);

    int[] gtrr(int[] register, int a, int b, int c);

    int[] eqir(int[] register, int a, int b, int c);

    int[] eqri(int[] register, int a, int b, int c);

    int[] eqrr(int[] register, int a, int b, int c);
}
