package krabban91.kodvent.kodvent.y2018.d19;

import krabban91.kodvent.kodvent.y2018.d16.OpCodesALU;

import java.util.List;
import java.util.stream.Collectors;

public class GpuUnit {

    private List<GpuOperation> operations;
    private int ip;
    private OpCodesALU alu;

    public GpuUnit(List<String> rows) {
        ip = Integer.parseInt(rows.get(0).substring(4));
        operations = rows.subList(1, rows.size()).stream().map(GpuOperation::new).collect(Collectors.toList());
        this.alu = new OpCodesALU();
    }


    public long valueAtRegisterAfterOperations(int i) {
        int[] register = {0, 0, 0, 0, 0, 0};
        while (register[ip] < operations.size()) {
            GpuOperation r = operations.get((int) register[ip]);
            register = runOpAndIncInstruction(alu, register, r.getOperation(), r.getA(), r.getB(), r.getC());
        }
        return register[i];
    }

    private int[] runOpAndIncInstruction(OpCodesALU processor, int[] before, String operation, int a, int b, int c) {
        int[] longs = OpCodesALU.executeOp(alu, before, operation, a, b, c);
        longs[ip]++;
        return longs;
    }

    public int valueAtRegisterAfterOperationsPart2(int i) {
        // this action takes 2*5*5*41*5147 iterations to finish.
        int[] register = {1, 0, 0, 0, 0, 0};
        while (register[ip] < operations.size()) {
            GpuOperation r = operations.get((int) register[ip]);
            register = runOpAndIncInstruction(alu, register, r.getOperation(), r.getA(), r.getB(), r.getC());
        }
        return register[i];
    }
/*
(1) +
2 +
5 +
2*5 +
5*5 +
2*5*5 +
41 +
41*2 +
41*5 +
41*2*5 +
41*5*5 +
41*2*5*5 +
5147 +
5147*2 +
5147*5 +
5147*2*5 +
5147*5*5 +
5147*2*5*5 +
5147*41 +
5147*41*2 +
5147*41*5 +
5147*41*2*5 +
5147*41*5*5 +
5147*41*2*5*5 = 20108088
 */
}
