package krabban91.kodvent.kodvent.day16;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class OpCodeSample {

    Map<Integer, Boolean> worksForOperation = new HashMap();
    long[] registerBefore = new long[4];
    long[] registerAfter = new long[4];
    Operation operation;

    public OpCodeSample(String before, String operation, String after) {
        String[] bReg = before.split("\\[")[1].split("]")[0].split(", ");
        String[] aReg = after.split("\\[")[1].split("]")[0].split(", ");
        IntStream.range(0, bReg.length).forEach(i -> registerBefore[i] = Integer.parseInt(bReg[i]));
        IntStream.range(0, aReg.length).forEach(i -> registerAfter[i] = Integer.parseInt(aReg[i]));

        this.operation = new Operation(operation);
        this.worksForOperation = OpCodesALU.sampler(new OpCodesALU(), this);
    }


    public Map<Integer, Boolean> getWorksForOperation() {
        return worksForOperation;
    }


}
