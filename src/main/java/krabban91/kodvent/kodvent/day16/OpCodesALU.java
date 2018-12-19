package krabban91.kodvent.kodvent.day16;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OpCodesALU implements OpCodes {

    public static final int ADDR = 0;
    public static final int ADDI = 1;
    public static final int MULR = 2;
    public static final int MULI = 3;
    public static final int BANR = 4;
    public static final int BANI = 5;
    public static final int BORR = 6;
    public static final int BORI = 7;
    public static final int SETR = 8;
    public static final int SETI = 9;
    public static final int GTIR = 10;
    public static final int GTRI = 11;
    public static final int GTRR = 12;
    public static final int EQIR = 13;
    public static final int EQRI = 14;
    public static final int EQRR = 15;

    public static long[] executeOp(OpCodesALU processor, long[] before, int operation, int a, int b, int c){
        switch (operation){
            case ADDR: return processor.addr(before,a,b,c);
            case ADDI: return processor.addi(before,a,b,c);
            case MULR: return processor.mulr(before,a,b,c);
            case MULI: return processor.muli(before,a,b,c);
            case BANR: return processor.banr(before,a,b,c);
            case BANI: return processor.bani(before,a,b,c);
            case BORR: return processor.borr(before,a,b,c);
            case BORI: return processor.bori(before,a,b,c);
            case SETR: return processor.setr(before,a,b,c);
            case SETI: return processor.seti(before,a,b,c);
            case GTIR: return processor.gtir(before,a,b,c);
            case GTRI: return processor.gtri(before,a,b,c);
            case GTRR: return processor.gtrr(before,a,b,c);
            case EQIR: return processor.eqir(before,a,b,c);
            case EQRI: return processor.eqri(before,a,b,c);
            case EQRR: return processor.eqrr(before,a,b,c);
        }
        return null;
    }

    public static long[] executeOp(OpCodesALU processor, long[] before, String operation, int a, int b, int c){
        switch (operation){
            case "addr": return processor.addr(before,a,b,c);
            case "addi": return processor.addi(before,a,b,c);
            case "mulr": return processor.mulr(before,a,b,c);
            case "muli": return processor.muli(before,a,b,c);
            case "banr": return processor.banr(before,a,b,c);
            case "bani": return processor.bani(before,a,b,c);
            case "borr": return processor.borr(before,a,b,c);
            case "bori": return processor.bori(before,a,b,c);
            case "setr": return processor.setr(before,a,b,c);
            case "seti": return processor.seti(before,a,b,c);
            case "gtir": return processor.gtir(before,a,b,c);
            case "gtri": return processor.gtri(before,a,b,c);
            case "gtrr": return processor.gtrr(before,a,b,c);
            case "eqir": return processor.eqir(before,a,b,c);
            case "eqri": return processor.eqri(before,a,b,c);
            case "eqrr": return processor.eqrr(before,a,b,c);
        }
        return null;
    }

    public static Map<Integer, Boolean> sampler(OpCodesALU alu, OpCodeSample sample) {
        Map<Integer, Boolean> matches = new HashMap<>();
        matches.put(ADDI, Arrays.equals(alu.addi(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(ADDR, Arrays.equals(alu.addr(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(BANI, Arrays.equals(alu.bani(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(BANR, Arrays.equals(alu.banr(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(BORI, Arrays.equals(alu.bori(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(BORR, Arrays.equals(alu.borr(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(EQIR, Arrays.equals(alu.eqir(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(EQRI, Arrays.equals(alu.eqri(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(EQRR, Arrays.equals(alu.eqrr(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(GTIR, Arrays.equals(alu.gtir(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(GTRI, Arrays.equals(alu.gtri(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(GTRR, Arrays.equals(alu.gtrr(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(MULI, Arrays.equals(alu.muli(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(MULR, Arrays.equals(alu.mulr(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(SETI, Arrays.equals(alu.seti(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        matches.put(SETR, Arrays.equals(alu.setr(sample.registerBefore, sample.operation.a, sample.operation.b, sample.operation.c), sample.registerAfter));
        return matches;
    }

    @Override
    public long[] addr(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] + register[b];
        return newRegister;
    }

    @Override
    public long[] addi(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] + b;
        return newRegister;
    }

    @Override
    public long[] mulr(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] * register[b];
        return newRegister;
    }

    @Override
    public long[] muli(long[] register, int a, int b, int c) {

        long[] newRegister = register.clone();
        newRegister[c] = register[a] * b;
        return newRegister;
    }

    @Override
    public long[] banr(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] & register[b];
        return newRegister;
    }

    @Override
    public long[] bani(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] & b;
        return newRegister;
    }

    @Override
    public long[] borr(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] | register[b];
        return newRegister;
    }

    @Override
    public long[] bori(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] | b;
        return newRegister;
    }

    @Override
    public long[] setr(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a];
        return newRegister;
    }

    @Override
    public long[] seti(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = a;
        return newRegister;
    }

    @Override
    public long[] gtir(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = a > register[b] ? 1 : 0;
        return newRegister;
    }

    @Override
    public long[] gtri(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] > b ? 1 : 0;
        return newRegister;
    }

    @Override
    public long[] gtrr(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] > register[b] ? 1 : 0;
        return newRegister;
    }

    @Override
    public long[] eqir(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = a == register[b] ? 1 : 0;
        return newRegister;
    }

    @Override
    public long[] eqri(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] == b ? 1 : 0;
        return newRegister;
    }

    @Override
    public long[] eqrr(long[] register, int a, int b, int c) {
        long[] newRegister = register.clone();
        newRegister[c] = register[a] == register[b] ? 1 : 0;
        return newRegister;
    }
}
