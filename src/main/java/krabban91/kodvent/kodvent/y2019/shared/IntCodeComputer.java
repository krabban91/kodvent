package krabban91.kodvent.kodvent.y2019.shared;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;

public class IntCodeComputer {

    public static final int ADD_CODE = 1;
    public static final int ADD_SIZE = 4;
    public static final int MUL_CODE = 2;
    public static final int MUL_SIZE = 4;
    public static final int HALT_CODE = 99;
    public static final int HALT_SIZE = 1;
    public static final int OUTPUT_CODE = 4;
    public static final int OUTPUT_SIZE = 2;
    public static final int INPUT_CODE = 3;
    public static final int INPUT_SIZE = 2;
    public static final int IFTRUE_CODE = 5;
    public static final int IF_TRUE_SIZE = 3;
    public static final int IF_FALSE_CODE = 6;
    public static final int LESS_THAN_CODE = 7;
    public static final int EQUALS_CODE = 8;
    public static final int MEMORY_MODE = 0;
    public static final int IMEDIATE_MODE = 1;
    private static final int IF_FALSE_SIZE = 3;
    private static final int LESS_THAN_SIZE = 4;
    private static final int EQUALS_SIZE = 4;
    private final List<Integer> program;
    private final Deque<Integer> inputs;
    private final Deque<Integer> outputs;
    private boolean verbose = false;
    private int pointer;

    public IntCodeComputer(@NotNull List<Integer> program, Deque<Integer> inputs, Deque<Integer> outputs) {
        this.program = new ArrayList<>(program);
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public IntCodeComputer(@NotNull List<Integer> program, Deque<Integer> inputs) {
        this(program, inputs, new LinkedBlockingDeque<>());
    }


    public IntCodeComputer(@NotNull List<Integer> program, int noun, int verb) {
        this(program, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        this.setNoun(noun);
        this.setVerb(verb);
    }

    public boolean hasHalted() {
        return this.program.get(pointer) == HALT_CODE;
    }

    public void run() {
        while (!hasHalted()) {
            step();
        }
    }

    public void runUntilOutputSize(int i) {
        while (!hasHalted() && this.outputs.size() < i) {
            step();
        }
    }

    public void addInput(int input) {
        this.inputs.addLast(input);
    }

    public int getAddress(int i) {
        return program.get(i);
    }

    public int getOutput() {
        return getAddress(0);
    }

    public void setAddress(int i, int value) {
        program.set(i, value);
    }

    public void setNoun(int value) {
        setAddress(1, value);
    }

    public void setVerb(int value) {
        setAddress(2, value);
    }

    private void step() {

        int address = this.getAddress(pointer);
        int opCode = address % 100;
        int modes = address / 100;
        int mode1 = modes % 10;
        int mode2 = (modes / 10) % 10;
        switch (opCode) {
            case ADD_CODE:
                add(mode1, mode2);
                break;
            case MUL_CODE:
                multiply(mode1, mode2);
                break;
            case INPUT_CODE:
                input();
                break;
            case OUTPUT_CODE:
                output(mode1);
                break;
            case IFTRUE_CODE:
                ifTrue(mode1, mode2);
                break;
            case IF_FALSE_CODE:
                ifFalse(mode1, mode2);
                break;
            case LESS_THAN_CODE:
                lessThan(mode1, mode2);
                break;
            case EQUALS_CODE:
                isEqual(mode1, mode2);
                break;
            case HALT_CODE:
                break;
        }
    }

    private void lessThan(int mode1, int mode2) {
        int a = getValue(mode1, pointer + 1);
        int b = getValue(mode2, pointer + 2);
        program.set(getValue(IMEDIATE_MODE, pointer + 3), a < b ? 1 : 0);
        pointer += LESS_THAN_SIZE;
    }

    private void isEqual(int mode1, int mode2) {
        int a = getValue(mode1, pointer + 1);
        int b = getValue(mode2, pointer + 2);
        program.set(getValue(IMEDIATE_MODE, pointer + 3), a == b ? 1 : 0);
        pointer += EQUALS_SIZE;
    }

    private void ifFalse(int mode1, int mode2) {
        int a = getValue(mode1, pointer + 1);
        int b = getValue(mode2, pointer + 2);
        if (a == 0) {
            pointer = b;
        } else {
            pointer += IF_FALSE_SIZE;
        }
    }

    private void ifTrue(int mode1, int mode2) {
        int a = getValue(mode1, pointer + 1);
        int b = getValue(mode2, pointer + 2);
        if (a != 0) {
            pointer = b;
        } else {
            pointer += IF_TRUE_SIZE;
        }
    }

    public Integer lastOutput() {
        return this.outputs.peekLast();
    }

    private void output(int mode1) {
        int a = getValue(mode1, pointer + 1);
        this.outputs.addLast(a);
        if(verbose){
            System.out.println("output: " + a);
        }
        pointer += OUTPUT_SIZE;
    }

    private void input() {
        if(verbose){
            System.out.print("input: ");
        }

        int in;
        if (inputs.isEmpty()) {
            Scanner scan = new Scanner(System.in);
            in = scan.nextInt();
        } else {
            in = inputs.pop();
        }
        if(verbose){
            System.out.println(in);
        }
        program.set(getValue(IMEDIATE_MODE, pointer + 1), in);
        pointer += INPUT_SIZE;
    }

    private void add(int mode1, int mode2) {
        int a = getValue(mode1, pointer + 1);
        int b = getValue(mode2, pointer + 2);
        program.set(getValue(IMEDIATE_MODE, pointer + 3), a + b);
        pointer += ADD_SIZE;
    }

    private void multiply(int mode1, int mode2) {
        int a = getValue(mode1, pointer + 1);
        int b = getValue(mode2, pointer + 2);
        program.set(getValue(IMEDIATE_MODE, pointer + 3), a * b);
        pointer += MUL_SIZE;
    }

    private Integer getValue(int mode, int address) {
        return mode == MEMORY_MODE ? program.get(program.get(address)) : program.get(address);
    }

}
