package krabban91.kodvent.kodvent.y2019.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class IntCodeComputer implements Runnable {

    private static final int ADD_CODE = 1;
    private static final int ADD_SIZE = 4;
    private static final int MUL_CODE = 2;
    private static final int MUL_SIZE = 4;
    private static final int HALT_CODE = 99;
    private static final int HALT_SIZE = 1;
    private static final int OUTPUT_CODE = 4;
    private static final int OUTPUT_SIZE = 2;
    private static final int INPUT_CODE = 3;
    private static final int INPUT_SIZE = 2;
    private static final int IFTRUE_CODE = 5;
    private static final int IF_TRUE_SIZE = 3;
    private static final int IF_FALSE_CODE = 6;
    private static final int LESS_THAN_CODE = 7;
    private static final int EQUALS_CODE = 8;
    private static final int RELATIVE_CHANGE_CODE = 9;
    private static final int RELATIVE_CHANGE_SIZE = 2;
    private static final int POSITION_MODE = 0;
    private static final int IMEDIATE_MODE = 1;
    private static final int RELATIVE_MODE = 2;
    private static final int IF_FALSE_SIZE = 3;
    private static final int LESS_THAN_SIZE = 4;
    private static final int EQUALS_SIZE = 4;
    private final List<Long> program;
    private final Map<Integer, Long> extraMemory = new HashMap<>();

    private final LinkedBlockingDeque<Long> inputs;
    private final LinkedBlockingDeque<Long> outputs;
    private boolean verbose = false;
    private int pointer;
    private int relativeBase = 0;

    public IntCodeComputer(List<Long> program, LinkedBlockingDeque<Long> inputs, LinkedBlockingDeque<Long> outputs) {
        this.program = new ArrayList<>(program);
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public IntCodeComputer(List<Long> program, LinkedBlockingDeque<Long> inputs) {
        this(program, inputs, new LinkedBlockingDeque<>());
    }


    public IntCodeComputer(List<Long> program, int noun, int verb) {
        this(program, new LinkedBlockingDeque<>(), new LinkedBlockingDeque<>());
        this.setNoun(noun);
        this.setVerb(verb);
    }

    public boolean hasHalted() {
        return this.program.get(pointer) == HALT_CODE;
    }

    @Override
    public void run() {
        while (!hasHalted()) {
            step();
        }
    }

    public void addInput(long input) {
        this.inputs.addLast(input);
    }

    public Long pollOutput(long timeoutSeconds) throws InterruptedException {
        return outputs.pollFirst(timeoutSeconds, TimeUnit.SECONDS);
    }

    public long getAddress(int index) {
        if (index >= program.size()) {
            return extraMemory.computeIfAbsent(index, k -> 0L);
        } else {
            return program.get(index);
        }
    }

    public long getOutput() {
        return getAddress(0);
    }

    public void setAddress(int index, long value) {
        if (index >= program.size()) {
            extraMemory.put(index, value);
        } else {
            program.set(index, value);
        }
    }

    public void setNoun(int value) {
        setAddress(1, value);
    }

    public void setVerb(int value) {
        setAddress(2, value);
    }

    private void step() {

        long address = this.getAddress(pointer);
        int opCode = (int) (address % 100);
        int modes = (int) (address / 100);
        int mode1 = modes % 10;
        int mode2 = (modes / 10) % 10;
        int mode3 = (modes / 100) % 10;
        switch (opCode) {
            case ADD_CODE:
                add(mode1, mode2, mode3);
                break;
            case MUL_CODE:
                multiply(mode1, mode2, mode3);
                break;
            case INPUT_CODE:
                input(mode1);
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
                lessThan(mode1, mode2, mode3);
                break;
            case EQUALS_CODE:
                isEqual(mode1, mode2, mode3);
                break;
            case RELATIVE_CHANGE_CODE:
                changeRelativeBase(mode1);
                break;
            case HALT_CODE:
                break;
            default:
                throw new RuntimeException("Invalid Opcode: " + opCode);
        }
    }

    private void changeRelativeBase(int mode1) {
        long a = getValue(mode1, pointer + 1);
        this.relativeBase += a;
        pointer += RELATIVE_CHANGE_SIZE;
    }

    private void lessThan(int mode1, int mode2, int mode3) {
        long a = getValue(mode1, pointer + 1);
        long b = getValue(mode2, pointer + 2);
        this.setValue(mode3, pointer + 3, a < b ? 1L : 0L);
        pointer += LESS_THAN_SIZE;
    }

    private void isEqual(int mode1, int mode2, int mode3) {
        long a = getValue(mode1, pointer + 1);
        long b = getValue(mode2, pointer + 2);
        this.setValue(mode3, pointer + 3, a == b ? 1L : 0L);
        pointer += EQUALS_SIZE;
    }

    private void ifFalse(int mode1, int mode2) {
        long a = getValue(mode1, pointer + 1);
        long b = getValue(mode2, pointer + 2);
        if (a == 0) {
            pointer = (int) b;
        } else {
            pointer += IF_FALSE_SIZE;
        }
    }

    private void ifTrue(int mode1, int mode2) {
        long a = getValue(mode1, pointer + 1);
        long b = getValue(mode2, pointer + 2);
        if (a != 0) {
            pointer = (int) b;
        } else {
            pointer += IF_TRUE_SIZE;
        }
    }

    public Long lastOutput() {
        return this.outputs.peekLast();
    }

    private void output(int mode1) {
        long a = getValue(mode1, pointer + 1);
        this.outputs.addLast(a);
        if (verbose) {
            System.out.println("output: " + a);
        }
        pointer += OUTPUT_SIZE;
    }

    private void input(int mode) {
        if (verbose) {
            System.out.print("input: ");
        }
        try {
            Long in = inputs.pollFirst(10, TimeUnit.SECONDS);
            if (in == null) {
                if (verbose) {
                    System.out.println("<EMPTY>");
                }
                return;
            }
            if (verbose) {
                System.out.println(in);
            }
            this.setValue(mode, pointer + 1, in);
            pointer += INPUT_SIZE;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void add(int mode1, int mode2, int mode3) {
        long a = getValue(mode1, pointer + 1);
        long b = getValue(mode2, pointer + 2);
        this.setValue(mode3, pointer + 3, a + b);
        pointer += ADD_SIZE;
    }

    private void multiply(int mode1, int mode2, int mode3) {
        long a = getValue(mode1, pointer + 1);
        long b = getValue(mode2, pointer + 2);
        this.setValue(mode3, pointer + 3, a * b);
        pointer += MUL_SIZE;
    }

    private void setValue(int mode, int address, long value) {
        if (mode == POSITION_MODE) {
            setAddress((int) getAddress(address), value);
        } else if (mode == RELATIVE_MODE) {
            setAddress((relativeBase + (int) getAddress(address)), value);
        } else {
            throw new RuntimeException("Invalid Mode in set: " + mode);
        }
    }

    private long getValue(int mode, int address) {
        int index;
        if (mode == POSITION_MODE) {
            index = (int) getAddress(address);
        } else if (mode == IMEDIATE_MODE) {
            index = address;
        } else if (mode == RELATIVE_MODE) {
            index = (int) (relativeBase + getAddress(address));
        } else {
            throw new RuntimeException("Invalid Mode in get: " + mode);
        }
        return getAddress(index);
    }
}
