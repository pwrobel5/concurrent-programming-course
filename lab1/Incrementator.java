public class Incrementator extends Thread {
    private Counter counter;
    private final int INC_COUNT = 10000000;

    public Incrementator(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for(int i = 0; i < INC_COUNT; i++) {
            this.counter.incrementCounter();
        }
    }
}
