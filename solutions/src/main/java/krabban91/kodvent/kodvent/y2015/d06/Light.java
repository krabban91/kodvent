package krabban91.kodvent.kodvent.y2015.d06;

public class Light {

    private boolean isOn;
    private int brightness;

    public Light() {
    }

    public void toggle() {
        isOn = !isOn;
        brightness += 2;
    }

    public void turnOff() {
        isOn = false;
        brightness = Math.max(brightness - 1, 0);
    }

    public void turnOn() {
        isOn = true;
        brightness += 1;
    }

    public void actOnInstruction(Instruction instruction) {
        switch (instruction.getAction()) {
            case TURN_ON:
                turnOn();
                break;
            case TURN_OFF:
                turnOff();
                break;
            case TOGGLE:
                toggle();
                break;
        }
    }

    public int isOn() {
        return isOn ? 1 : 0;
    }

    public int getBrightness() {
        return brightness;
    }
}
