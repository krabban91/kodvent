package krabban91.kodvent.kodvent.y2018.d07;

public class Worker {
    private boolean working;

    private Character workingOn;
    private int timeLeft;

    public Worker() {
        working = false;
        timeLeft = 0;
        workingOn = null;
    }

    public void assignWork(Character work, int cost) {
        working = true;
        workingOn = work;
        timeLeft = cost;
    }

    public Character getWorkingOn() {
        return workingOn == null ? '.' : workingOn;
    }

    public boolean isAvailable() {
        return !isWorking();
    }

    public boolean isWorking() {
        return working;
    }

    public Character doWork() {
        if (--this.timeLeft <= 0 && isWorking()) {
            return completeWork();
        }
        return null;
    }

    private Character completeWork() {
        working = false;
        Character workingOn = this.workingOn;
        this.workingOn = null;
        return workingOn;
    }
}
