package krabban91.kodvent.kodvent.day16;

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
        IntStream.range(0, bReg.length).forEach(i -> registerBefore[i] = Integer.parseInt(bReg[i]));
        IntStream.range(0, aReg.length).forEach(i -> registerAfter[i] = Integer.parseInt(aReg[i]));

        String[] operations = register.split(" ");
        this.operation = Integer.parseInt(operations[0]);
        this.a = Integer.parseInt(operations[1]);
        this.b = Integer.parseInt(operations[2]);
        this.c = Integer.parseInt(operations[3]);
        this.worksForOperation = OpCodesALU.sampler(new OpCodesALU(), this);
    }


    public Map<Integer, Boolean> getWorksForOperation() {
        return worksForOperation;
    }


}
