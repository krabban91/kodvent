package krabban91.kodvent.kodvent.y2019.shared;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class IntCodeComputer {

    public static final int ADD_CODE = 1;
    public static final int ADD_SIZE = 4;
    public static final int MUL_CODE = 2;
    public static final int MUL_SIZE = 4;
    public static final int HALT_CODE = 99;
    public static final int HALT_SIZE = 1;
    private final List<Integer> program;
    private int pointer;

    public IntCodeComputer(@NotNull List<Integer> program) {
        this(program,0,0);
    }

    public IntCodeComputer(@NotNull List<Integer> program, int noun, int verb) {
        this.program = new ArrayList<>(program);
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

    public int getAddress(int i) {
        return program.get(i);
    }

    public int getOutput(){
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
        switch (this.getAddress(pointer)) {
            case ADD_CODE:
                add();
                break;
            case MUL_CODE:
                multiply();
                break;
            case HALT_CODE:
                break;
        }
    }

    private void add() {
        program.set(program.get(pointer + 3), program.get(program.get(pointer + 1)) + program.get(program.get(pointer + 2)));
        pointer += ADD_SIZE;
    }

    private void multiply() {
        program.set(program.get(pointer + 3), program.get(program.get(pointer + 1)) * program.get(program.get(pointer + 2)));
        pointer += MUL_SIZE;
    }

}
