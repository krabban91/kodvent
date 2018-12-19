package krabban91.kodvent.kodvent.day16;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class OpCodeSample {

    Map<Integer, Boolean> worksForOperation = new HashMap();
    int[] registerBefore = new int[4];
    int[] registerAfter = new int[4];
    int operation;
    int a;
    int b;
    int c;
    public OpCodeSample(String before, String register, String after) {
        String[] bReg = before.split("\\[")[1].split("]")[0].split(", ");
        String[] aReg = after.split("\\[")[1].split("]")[0].split(", ");
        IntStream.range(0,bReg.length).forEach(i-> registerBefore[i] = Integer.parseInt(bReg[i]));
        IntStream.range(0,aReg.length).forEach(i-> registerAfter[i] = Integer.parseInt(aReg[i]));

        String[] operations = register.split(" ");
        this.operation = Integer.parseInt(operations[0]);
        this.a= Integer.parseInt(operations[1]);
        this.b= Integer.parseInt(operations[2]);
        this.c= Integer.parseInt(operations[3]);
    }

    public static int matchesForNumberOfOpCodes(OpCodesALU alu, OpCodeSample sample){
        return (int)opCodeSampler(alu, sample).values().stream().filter(e->e).count();
    }

    public static Map<Integer, Boolean> opCodeSampler(OpCodesALU alu, OpCodeSample sample) {
        Map<Integer, Boolean> matches = new HashMap<>();
        Map<Integer, int[]> produces = new HashMap<>();
        matches.put(0,Arrays.equals(alu.addi(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(1,Arrays.equals(alu.addr(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(2,Arrays.equals(alu.bani(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(3,Arrays.equals(alu.banr(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(4,Arrays.equals(alu.bori(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(5,Arrays.equals(alu.borr(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(6,Arrays.equals(alu.eqir(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(7,Arrays.equals(alu.eqri(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(8,Arrays.equals(alu.eqrr(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(9,Arrays.equals(alu.gtir(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(10,Arrays.equals(alu.gtri(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(11,Arrays.equals(alu.gtrr(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(12,Arrays.equals(alu.muli(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(13,Arrays.equals(alu.mulr(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(14,Arrays.equals(alu.seti(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        matches.put(15,Arrays.equals(alu.setr(sample.registerBefore, sample.a, sample.b, sample.c), sample.registerAfter));
        produces.put(0,alu.addi(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(1,alu.addr(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(2,alu.bani(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(3,alu.banr(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(4,alu.bori(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(5,alu.borr(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(6,alu.eqir(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(7,alu.eqri(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(8,alu.eqrr(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(9,alu.gtir(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(10,alu.gtri(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(11,alu.gtrr(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(12,alu.muli(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(13,alu.mulr(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(14,alu.seti(sample.registerBefore, sample.a, sample.b, sample.c));
        produces.put(15,alu.setr(sample.registerBefore, sample.a, sample.b, sample.c));
        return matches;
    }

}
