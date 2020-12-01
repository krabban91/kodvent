package krabban91.kodvent.kodvent.y2018.d21;

import krabban91.kodvent.kodvent.y2018.d16.OpCodesALU;
import krabban91.kodvent.kodvent.y2018.d19.GpuOperation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ActivationProcess {

    public static final int COMPARING_STEP = 28;
    private List<GpuOperation> operations;
    private int ip;
    private OpCodesALU alu;

    public ActivationProcess(List<String> rows) {
        ip = Integer.parseInt(rows.get(0).substring(4));
        operations = rows.subList(1, rows.size()).stream().map(GpuOperation::new).collect(Collectors.toList());
        this.alu = new OpCodesALU();
    }

    public int firstHaltingValue(){
        int[] register = {0, 0, 0, 0, 0, 0};
        while (register[ip] < operations.size()) {
            if (register[ip] == COMPARING_STEP) {
                return register[4];
            }
            GpuOperation r = operations.get((int) register[ip]);
            register = runOpAndIncInstruction(register, r.getOperation(), r.getA(), r.getB(), r.getC());

        }
        return 0;
    }

    public int lastHaltingValue(){

        int[] register = {0, 0, 0, 0, 0, 0};

        Set<Integer> checkedValues = new HashSet<>();
        Integer lastCheckedValue = 0;
        while (register[ip] < operations.size()) {
            if (register[ip] == COMPARING_STEP) {
                if (!checkedValues.add(register[4])) {
                    return lastCheckedValue;
                }
                lastCheckedValue = register[4];
            }
            GpuOperation r = operations.get((int) register[ip]);
            register = runOpAndIncInstruction(register, r.getOperation(), r.getA(), r.getB(), r.getC());

        }
        return 0;
    }

    private int[] runOpAndIncInstruction(int[] before, String operation, int a, int b, int c) {
        int[] ints = OpCodesALU.executeOp(alu, before, operation, a, b, c);
        ints[ip]++;
        return ints;
    }
}
