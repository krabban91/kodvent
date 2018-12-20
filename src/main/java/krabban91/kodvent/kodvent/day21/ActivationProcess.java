package krabban91.kodvent.kodvent.day21;

import krabban91.kodvent.kodvent.day16.OpCodesALU;
import krabban91.kodvent.kodvent.day19.GpuOperation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ActivationProcess {

    private List<GpuOperation> operations;
    private int ip;
    private OpCodesALU alu;

    public ActivationProcess(List<String> rows) {
        ip = Integer.parseInt(rows.get(0).substring(4));
        operations = rows.subList(1, rows.size()).stream().map(GpuOperation::new).collect(Collectors.toList());
        this.alu = new OpCodesALU();
    }

    public int haltWithAsLowStartingValueAsPossible(){

        int initialValue = 12670166;//9709008;
        {
            valueAtRegisterAfterOperations(0,initialValue);
            System.out.println("It halted:"+initialValue);


        }
        return initialValue;
    }



    public int valueAtRegisterAfterOperations(int i,int initialValueRegister1 ) {
        int[] register = {initialValueRegister1, 0, 0, 0, 0, 0};
        while (register[ip] < operations.size()) {
            GpuOperation r = operations.get((int) register[ip]);
            register = runOpAndIncInstruction(alu, register, r.getOperation(), r.getA(), r.getB(), r.getC());
            System.out.print("\r Registers: "+ Arrays.toString(register));

        }
        return register[i];
    }

    private int[] runOpAndIncInstruction(OpCodesALU processor, int[] before, String operation, int a, int b, int c) {
        int[] ints = OpCodesALU.executeOp(alu, before, operation, a, b, c);
        ints[ip]++;
        return ints;
    }

    /*
#ip 1
seti 123 0 4        # R4=123
bani 4 456 4        # F: R4 &= 456
eqri 4 72 4         # if(R4 == 72) goto E
addr 4 1 1          #   ;
seti 0 0 1          # D: goto F
seti 0 4 4          # E: R4 = 0
bori 4 65536 3      # A: R3 = R4 | 65536
seti 12670166 8 4   # R4 = 12670166
bani 3 255 2        # R2 = R3 & 255
addr 4 2 4          # R4 = R2 + R4
bani 4 16777215 4   # R4 &= 16777215
muli 4 65899 4      # R4 *= 65899
bani 4 16777215 4   # R4 &= 16777215
gtir 256 3 2        # if(256 > 3) goto G
addr 2 1 1          # else        goto F
addi 1 1 1          # ;
seti 27 6 1         # B: goto C
seti 0 0 2          # G: R2 = 0
addi 2 1 5          # R5 = R5 + 2
muli 5 256 5        # R5 = R5 * 5
gtrr 5 3 5          # if(R5 > R3) goto B
addr 5 1 1          # else goto A
addi 1 1 1          # goto break;
seti 25 6 1         # else
addi 2 1 2
seti 17 8 1         # goto B loop
setr 2 5 3          # C:
seti 7 2 1          # go to A loop
eqrr 4 0 2          # if(R4 == 0) break;
addr 2 1 1          # ;
seti 5 8 1          # go to D loop
     */
}
