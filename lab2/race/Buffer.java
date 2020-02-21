package race;

public class Buffer {
    private int state;

    public Buffer(int state) {
        this.state = state;
    }

    public void increment() {
        this.state += 1;
    }

    public void decrement() {
        this.state -= 1;
    }

    public void printState() {
        System.out.println("race.Buffer state: " + this.state);
    }
}
