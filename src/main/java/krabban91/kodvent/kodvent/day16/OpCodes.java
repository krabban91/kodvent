package krabban91.kodvent.kodvent.day16;

public interface OpCodes {

    long[] addr(long[] register, int a, int b, int c);

    long[] addi(long[] register, int a, int b, int c);

    long[] mulr(long[] register, int a, int b, int c);

    long[] muli(long[] register, int a, int b, int c);

    long[] banr(long[] register, int a, int b, int c);

    long[] bani(long[] register, int a, int b, int c);

    long[] borr(long[] register, int a, int b, int c);

    long[] bori(long[] register, int a, int b, int c);

    long[] setr(long[] register, int a, int b, int c);

    long[] seti(long[] register, int a, int b, int c);

    long[] gtir(long[] register, int a, int b, int c);

    long[] gtri(long[] register, int a, int b, int c);

    long[] gtrr(long[] register, int a, int b, int c);

    long[] eqir(long[] register, int a, int b, int c);

    long[] eqri(long[] register, int a, int b, int c);

    long[] eqrr(long[] register, int a, int b, int c);
}
